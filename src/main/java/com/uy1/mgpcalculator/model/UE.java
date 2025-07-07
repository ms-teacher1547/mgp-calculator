package com.uy1.mgpcalculator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Représente une Unité d'Enseignement (UE) avec sa note et ses informations calculées
 * Cette classe encapsule toutes les données d'une matière pour le calcul du MGP
 */
@Entity
@Table(name = "ue")
public class UE {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nom de l'unité d'enseignement
     * Exemple: "Programmation Java", "Mathématiques Discrètes"
     */
    @Column(nullable = false)
    @NotBlank(message = "Le nom de l'UE ne peut pas être vide")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;
    
    /**
     * Nombre de crédits de l'UE
     * À UY1, les UE valent soit 3 crédits soit 6 crédits
     */
    @Column(nullable = false)
    @NotNull(message = "Les crédits sont obligatoires")
    @Min(value = 3, message = "Les crédits doivent être minimum 3")
    @Max(value = 6, message = "Les crédits doivent être maximum 6")
    private Integer credits;
    
    /**
     * Note obtenue sur 100
     * Doit être comprise entre 0 et 100
     */
    @Column(nullable = false)
    @NotNull(message = "La note est obligatoire")
    @DecimalMin(value = "0.0", message = "La note doit être minimum 0")
    @DecimalMax(value = "100.0", message = "La note doit être maximum 100")
    private Double note;
    
    /**
     * Cote calculée automatiquement selon le système UY1
     * Exemples: A, B+, C-, D, E, F
     */
    @Column(length = 2)
    private String cote;
    
    /**
     * Qualité de points calculée automatiquement selon la note
     * Valeurs possibles: 0.0, 1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0
     */
    @Column(name = "qualite_points")
    private Double qualitePoints;
    
    /**
     * Mention calculée automatiquement
     * Exemples: "Très Bien", "Bien", "Assez Bien", "Passable", "CANT", "Échec"
     */
    @Column(length = 20)
    private String mention;
    
    // Constructeurs
    
    /**
     * Constructeur par défaut requis par JPA
     */
    public UE() {}
    
    /**
     * Constructeur avec les données saisies par l'utilisateur
     * Les autres champs (cote, qualitePoints, mention) seront calculés automatiquement
     * 
     * @param nom Le nom de l'UE
     * @param credits Le nombre de crédits (3 ou 6)
     * @param note La note sur 100
     */
    public UE(String nom, Integer credits, Double note) {
        this.nom = nom;
        this.credits = credits;
        this.note = note;
    }
    
    // Getters et Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Integer getCredits() {
        return credits;
    }
    
    public void setCredits(Integer credits) {
        this.credits = credits;
    }
    
    public Double getNote() {
        return note;
    }
    
    public void setNote(Double note) {
        this.note = note;
    }
    
    public String getCote() {
        return cote;
    }
    
    public void setCote(String cote) {
        this.cote = cote;
    }
    
    public Double getQualitePoints() {
        return qualitePoints;
    }
    
    public void setQualitePoints(Double qualitePoints) {
        this.qualitePoints = qualitePoints;
    }
    
    public String getMention() {
        return mention;
    }
    
    public void setMention(String mention) {
        this.mention = mention;
    }
    
    /**
     * Calcule les points totaux pour cette UE
     * Points totaux = qualitePoints × credits
     * 
     * @return Les points totaux de l'UE
     */
    public Double getPointsTotaux() {
        if (qualitePoints == null || credits == null) {
            return 0.0;
        }
        return qualitePoints * credits;
    }
    
    // Méthodes utilitaires
    
    /**
     * Vérifie si l'UE est validée (note >= 50)
     * 
     * @return true si l'UE est validée, false sinon
     */
    public boolean isValidee() {
        return note != null && note >= 50.0;
    }
    
    /**
     * Vérifie si l'UE est en échec (note < 35)
     * 
     * @return true si l'UE est en échec, false sinon
     */
    public boolean isEnEchec() {
        return note != null && note < 35.0;
    }
    
    @Override
    public String toString() {
        return "UE{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", credits=" + credits +
                ", note=" + note +
                ", cote='" + cote + '\'' +
                ", qualitePoints=" + qualitePoints +
                ", mention='" + mention + '\'' +
                '}';
    }
}