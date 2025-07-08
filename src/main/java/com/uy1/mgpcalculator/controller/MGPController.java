package com.uy1.mgpcalculator.controller;

import com.itextpdf.text.DocumentException;
import com.uy1.mgpcalculator.model.ResultatMGP;
import com.uy1.mgpcalculator.model.UE;
import com.uy1.mgpcalculator.service.MGPService;
import com.uy1.mgpcalculator.service.PDFService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mgp")
public class MGPController {

    private final MGPService mgpService;
    private final PDFService pdfService;

    public MGPController(MGPService mgpService, PDFService pdfService) {
        this.mgpService = mgpService;
        this.pdfService = pdfService;
    }

    /**
     * Endpoint pour calculer le MGP
     * 
     * @param ues Liste des UE à calculer
     * @return Le résultat du calcul
     */
    @PostMapping("/calculer")
    public ResponseEntity<ResultatMGP> calculerMGP(@RequestBody CalculMGPPayload payload) {
        try {
            if (!mgpService.validerUEs(payload.getUes())) {
                return ResponseEntity.badRequest().build();
            }

            ResultatMGP resultat = mgpService.calculerResultatMGP(
                payload.getUes(), 
                payload.getNomEtudiant()  // Passez le nom ici
            );
            return ResponseEntity.ok(resultat);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Ajoutez cette classe dans un nouveau fichier ou comme classe interne
    public static class CalculMGPPayload {
        private List<UE> ues;
        private String nomEtudiant;

        // Getters et setters
        public List<UE> getUes() {
            return ues;
        }

        public void setUes(List<UE> ues) {
            this.ues = ues;
        }

        public String getNomEtudiant() {
            return nomEtudiant;
        }

        public void setNomEtudiant(String nomEtudiant) {
            this.nomEtudiant = nomEtudiant;
        }
    }

    /**
     * Endpoint pour vérifier la validité d'une liste d'UE
     * 
     * @param ues Liste des UE à vérifier
     * @return true si la liste est valide
     */
    @PostMapping("/valider")
    public ResponseEntity<Boolean> validerUEs(@RequestBody List<UE> ues) {
        return ResponseEntity.ok(mgpService.validerUEs(ues));
    }

    /**
     * Endpoint pour calculer le pourcentage de réussite
     * 
     * @param ues Liste des UE
     * @return Le pourcentage de réussite
     */
    @PostMapping("/reussite")
    public ResponseEntity<Double> calculerPourcentageReussite(@RequestBody List<UE> ues) {
        return ResponseEntity.ok(mgpService.calculerPourcentageReussite(ues));
    }

    /**
     * Endpoint pour compter les UE en échec
     * 
     * @param ues Liste des UE
     * @return Nombre d'UE en échec
     */
    @PostMapping("/echecs")
    public ResponseEntity<Long> compterUEEchecs(@RequestBody List<UE> ues) {
        return ResponseEntity.ok(mgpService.compterUEEchecs(ues));
    }

    /**
     * Endpoint pour générer un PDF du résultat
     * 
     * @param resultat Le résultat à convertir en PDF
     * @return ResponseEntity contenant le PDF
     */
    @PostMapping("/generer-pdf")
    public ResponseEntity<byte[]> genererPDF(@RequestBody ResultatMGP resultat) {
        try {
            byte[] pdf = pdfService.genererBulletinMGP(resultat);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "bulletin-mgp.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (DocumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/historique/{nom}")
    public ResponseEntity<List<ResultatMGP>> getHistorique(@PathVariable String nom) {
        return ResponseEntity.ok(mgpService.rechercherHistorique(nom));
    }

    @PostMapping("/sauvegarder")
    public ResponseEntity<ResultatMGP> sauvegarderResultat(@RequestBody ResultatMGP resultat) {
        return ResponseEntity.ok(mgpService.sauvegarderResultat(resultat));
    }
}