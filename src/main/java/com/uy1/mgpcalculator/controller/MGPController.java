package com.uy1.mgpcalculator.controller;

import com.itextpdf.text.DocumentException;
import com.uy1.mgpcalculator.model.ResultatMGP;
import com.uy1.mgpcalculator.model.UE;
import com.uy1.mgpcalculator.repository.ResultatMGPRepository;
import com.uy1.mgpcalculator.service.MGPService;
import com.uy1.mgpcalculator.service.PDFService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/mgp")
public class MGPController {

    private final MGPService mgpService;
    private final PDFService pdfService;
    private final ResultatMGPRepository resultatRepository;

    public MGPController(MGPService mgpService, PDFService pdfService, 
                        ResultatMGPRepository resultatRepository) {
        this.mgpService = mgpService;
        this.pdfService = pdfService;
        this.resultatRepository = resultatRepository;
    }

    /**
     * Nouveau endpoint unifié pour calculer et sauvegarder
     * @param payload Données de calcul
     * @param autoSave true pour sauvegarder automatiquement (défaut: true)
     */
    @PostMapping("/calculer")
    public ResponseEntity<ResultatMGP> calculerMGP(
            @RequestBody CalculMGPPayload payload,
            @RequestParam(defaultValue = "true") boolean autoSave) {
        
        try {
            if (!mgpService.validerUEs(payload.getUes())) {
                return ResponseEntity.badRequest().build();
            }

            ResultatMGP resultat = mgpService.calculerResultatMGP(
                payload.getUes(), 
                payload.getNomEtudiant()
            );

            if (autoSave) {
                resultat = mgpService.sauvegarderResultat(resultat);
            }

            return ResponseEntity.ok(resultat);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Génération PDF à partir d'un ID sauvegardé
     */
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> genererPdfParId(@PathVariable Long id) {
        try {
            ResultatMGP resultat = resultatRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            return preparerReponsePdf(pdfService.genererBulletinMGP(resultat), 
                                    "bulletin-" + resultat.getNomEtudiant() + ".pdf");
        } catch (DocumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur de génération PDF");
        }
    }

    /**
     * Génération PDF directe (pour cas spéciaux)
     */
    @PostMapping("/pdf")
    public ResponseEntity<byte[]> genererPdfDirect(@RequestBody ResultatMGP resultat) {
        try {
            return preparerReponsePdf(pdfService.genererBulletinMGP(resultat), 
                                    "bulletin-temporaire.pdf");
        } catch (DocumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur de génération PDF");
        }
    }

    // Méthodes utilitaires et classes internes
    private ResponseEntity<byte[]> preparerReponsePdf(byte[] pdf, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
            ContentDisposition.attachment()
                .filename(filename)
                .build());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/historique/{nom}")
    public ResponseEntity<List<ResultatMGP>> getHistorique(@PathVariable String nom) {
        return ResponseEntity.ok(mgpService.rechercherHistorique(nom));
    }

    // Ancien endpoint gardé pour compatibilité
    @Deprecated
    @PostMapping("/sauvegarder")
    public ResponseEntity<ResultatMGP> sauvegarderResultat(@RequestBody ResultatMGP resultat) {
        return ResponseEntity.ok(mgpService.sauvegarderResultat(resultat));
    }

    public static class CalculMGPPayload {
        private List<UE> ues;
        private String nomEtudiant;

        // Getters & Setters
        public List<UE> getUes() { return ues; }
        public void setUes(List<UE> ues) { this.ues = ues; }
        public String getNomEtudiant() { return nomEtudiant; }
        public void setNomEtudiant(String nomEtudiant) { this.nomEtudiant = nomEtudiant; }
    }
}