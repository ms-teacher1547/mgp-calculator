package com.uy1.mgpcalculator.utils;

import com.uy1.mgpcalculator.model.UE;
import java.util.List;

/**
 * Classe utilitaire pour gérer les conversions de notes selon le système UY1
 * Version mise à jour avec les spécifications exactes fournies
 */
public class NotationUtils {

    // Seuils de notation selon le système UY1 (mis à jour)
    private static final double[] NOTE_SEUILS = {0, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80};
    private static final String[] COTES = {"F", "E", "D", "D+", "C-", "C", "C+", "B-", "B", "B+", "A-", "A"};
    private static final double[] QUALITE_POINTS = {0.0, 0.0, 1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0};
    private static final String[] MENTIONS_UE = {
        "Échec", "Échec", "CANT", "CANT", "CANT", 
        "Passable", "Passable", "Assez Bien", "Assez Bien", "Bien", "Bien", "Très Bien"
    };
    
    private static final String[] MENTIONS_MGP = {
        "Échec", "CANT", "Passable", "Assez Bien", "Bien", "Très Bien"
    };

    /**
     * Convertit une note numérique en cote alphabétique selon le système UY1
     */
    public static String convertirNoteEnCote(double note) {
        if (note < 0 || note > 100) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 100");
        }

        for (int i = NOTE_SEUILS.length - 1; i >= 0; i--) {
            if (note >= NOTE_SEUILS[i]) {
                return COTES[i];
            }
        }
        
        return "F";
    }

    /**
     * Convertit une note numérique en points de qualité selon le système UY1
     */
    public static double convertirNoteEnQualitePoints(double note) {
        if (note < 0 || note > 100) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 100");
        }

        for (int i = NOTE_SEUILS.length - 1; i >= 0; i--) {
            if (note >= NOTE_SEUILS[i]) {
                return QUALITE_POINTS[i];
            }
        }
        
        return 0.0;
    }

    /**
     * Convertit une note numérique en mention selon le système UY1
     */
    public static String convertirNoteEnMention(double note) {
        if (note < 0 || note > 100) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 100");
        }

        for (int i = NOTE_SEUILS.length - 1; i >= 0; i--) {
            if (note >= NOTE_SEUILS[i]) {
                return MENTIONS_UE[i];
            }
        }
        
        return "Échec";
    }

    /**
     * Convertit un MGP en mention générale selon le système UY1
     */
    public static String convertirMgpEnMention(double mgp) {
        if (mgp < 0 || mgp > 4.0) {
            throw new IllegalArgumentException("Le MGP doit être compris entre 0 et 4.0");
        }

        if (mgp < 1.0) return MENTIONS_MGP[0];  // Échec
        if (mgp < 1.5) return MENTIONS_MGP[1];   // CANT
        if (mgp < 2.0) return MENTIONS_MGP[2];   // Passable
        if (mgp < 2.5) return MENTIONS_MGP[3];   // Assez Bien
        if (mgp < 3.0) return MENTIONS_MGP[4];   // Bien
        if (mgp < 3.5) return MENTIONS_MGP[5];   // Très Bien
        return MENTIONS_MGP[6];                  // Excellent
    }

    /**
     * Calcule et met à jour les attributs calculés d'une UE
     */
    public static void calculerAttributsUE(UE ue) {
        if (ue == null || ue.getNote() == null) {
            throw new IllegalArgumentException("L'UE et sa note ne peuvent pas être null");
        }

        double note = ue.getNote();
        ue.setCote(convertirNoteEnCote(note));
        ue.setQualitePoints(convertirNoteEnQualitePoints(note));
        ue.setMention(convertirNoteEnMention(note));
    }

    /**
     * Calcule la moyenne générale pondérée (MGP) à partir d'une liste d'UE
     */
    public static double calculerMGP(List<UE> ues) {
        if (ues == null || ues.isEmpty()) {
            throw new IllegalArgumentException("La liste des UE ne peut pas être vide");
        }

        double totalPoints = 0;
        int totalCredits = 0;

        for (UE ue : ues) {
            if (ue.getNote() == null || ue.getCredits() == null) {
                throw new IllegalArgumentException("Toutes les UE doivent avoir une note et des crédits valides");
            }

            calculerAttributsUE(ue);
            totalPoints += ue.getPointsTotaux();
            totalCredits += ue.getCredits();
        }

        if (totalCredits == 0) {
            return 0.0;
        }

        return totalPoints / totalCredits;
    }

    /**
     * Vérifie si une UE est validée (note >= 50)
     */
    public static boolean isUEValidee(UE ue) {
        return ue != null && ue.getNote() != null && ue.getNote() >= 50.0;
    }

    /**
     * Vérifie si une UE est en échec (note < 35)
     */
    public static boolean isUEEnEchec(UE ue) {
        return ue != null && ue.getNote() != null && ue.getNote() < 35.0;
    }
}