package com.uy1.mgpcalculator.service;

import com.uy1.mgpcalculator.model.ResultatMGP;
import com.uy1.mgpcalculator.model.UE;
import com.uy1.mgpcalculator.repository.ResultatMGPRepository;
import com.uy1.mgpcalculator.repository.UERepository;
import com.uy1.mgpcalculator.utils.NotationUtils;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service principal pour le calcul du MGP
 */
@Service
@Transactional
public class MGPService {

    private final UERepository ueRepository;
    private final ResultatMGPRepository resultatRepository;

    public MGPService(UERepository ueRepository, ResultatMGPRepository resultatRepository) {
        this.ueRepository = ueRepository;
        this.resultatRepository = resultatRepository;
    }

    /**
     * Calcule le résultat complet du MGP à partir d'une liste d'UE
     * 
     * @param ues Liste des unités d'enseignement
     * @return Le résultat complet du calcul
     * @throws IllegalArgumentException si la liste est vide ou contient des UE invalides
     */
    public ResultatMGP calculerResultatMGP(List<UE> ues, String nomEtudiant) {
        // Validation de base
        if (ues == null || ues.isEmpty()) {
            throw new IllegalArgumentException("La liste des UE ne peut pas être vide");
        }

        // Calcul du MGP
        double mgp = NotationUtils.calculerMGP(ues);
        
        // Calcul des totaux
        int totalCredits = ues.stream()
                .mapToInt(UE::getCredits)
                .sum();
        
        double totalPoints = ues.stream()
                .mapToDouble(UE::getPointsTotaux)
                .sum();

        // Création du résultat
        ResultatMGP resultat = new ResultatMGP(
            ues,
            mgp,
            NotationUtils.convertirMgpEnMention(mgp),
            totalCredits,
            totalPoints,
            nomEtudiant
        );

        return resultat;
    }

    /**
     * Vérifie si une liste d'UE est valide pour le calcul
     * 
     * @param ues Liste des UE à vérifier
     * @return true si toutes les UE sont valides, false sinon
     */
    public boolean validerUEs(List<UE> ues) {
        if (ues == null || ues.isEmpty()) {
            return false;
        }

        return ues.stream()
                .allMatch(ue -> ue.getNote() != null 
                    && ue.getCredits() != null 
                    && (ue.getCredits() == 3 || ue.getCredits() == 6));
    }

    /**
     * Calcule le pourcentage de réussite (UE validées)
     * 
     * @param ues Liste des UE
     * @return Pourcentage de réussite
     */
    public double calculerPourcentageReussite(List<UE> ues) {
        if (ues == null || ues.isEmpty()) {
            return 0.0;
        }

        long ueValidees = ues.stream()
                .filter(NotationUtils::isUEValidee)
                .count();

        return (double) ueValidees / ues.size() * 100;
    }

    /**
     * Compte le nombre d'UE en échec
     * 
     * @param ues Liste des UE
     * @return Nombre d'UE en échec
     */
    public long compterUEEchecs(List<UE> ues) {
        if (ues == null) {
            return 0;
        }

        return ues.stream()
                .filter(NotationUtils::isUEEnEchec)
                .count();
    }

    public ResultatMGP sauvegarderResultat(ResultatMGP resultat) {
        // Sauvegarde en cascade des UE d'abord
        resultat.getUes().forEach(ueRepository::save);
        return resultatRepository.save(resultat);
    }

    public List<ResultatMGP> rechercherHistorique(String nomEtudiant) {
        return resultatRepository.findByNomEtudiantContainingIgnoreCase(nomEtudiant);
    }
}