package com.uy1.mgpcalculator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente le résultat complet du calcul de MGP
 * Cette classe encapsule toutes les informations nécessaires pour afficher
 * et générer le rapport PDF du calcul de MGP
 */
@Entity
@Table(name = "resultat_mgp")
public class ResultatMGP {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nom de l'étudiant (optionnel pour l'instant)
     * Peut être utilisé plus tard si on ajoute l'authentification
     */
    @Column(name = "nom_etudiant", length = 100)
    private String nomEtudiant;
    
    /**
     * Liste des UE utilisées pour le calcul
     * Relation OneToMany avec cascade pour sauvegarder automatiquement les UE
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "resultat_mgp_id")
    private List<UE> ues = new ArrayList<>();
    
    /**
     * Moyenne Générale Pondérée calculée sur 4
     * Résultat principal du calcul
     */
    @Column(nullable = false)
    @NotNull(message = "Le MGP ne peut pas être null")
    @DecimalMin(value = "0.0", message = "Le MGP doit être positif")
    @DecimalMax(value = "4.0", message = "Le MGP ne peut pas dépasser 4.0")
    private Double mgp;
    
    /**
     * Mention générale basée sur le MGP
     * Exemples: "Très Bien", "Bien", "Assez Bien", "Passable", "Échec"
     */
    @Column(nullable = false, length = 20)
    @NotBlank(message = "La mention ne peut pas être vide")
    private String mention;
    
    /**
     * Total des crédits de toutes les UE
     */
    @Column(name = "total_credits", nullable = false)
    @NotNull(message = "Le total des crédits ne peut pas être null")
    @Min(value = 1, message = "Le total des crédits doit être positif")
    private Integer totalCredits;
    
    /**
     * Total des points obtenus (somme des qualitePoints × credits)
     */
    @Column(name = "total_points", nullable = false)
    @NotNull(message = "Le total des points ne peut pas être null")
    @DecimalMin(value = "0.0", message = "Le total des points doit être positif")
    private Double totalPoints;
    
    /**
     * Nombre total d'UE dans le calcul
     */
    @Column(name = "nombre_ue", nullable = false)
    @NotNull(message = "Le nombre d'UE ne peut pas être null")
    @Min(value = 1, message = "Le nombre d'UE doit être positif")
    private Integer nombreUE;
    
    /**
     * Date et heure du calcul
     */
    @Column(name = "date_calcul", nullable = false)
    @NotNull(message = "La date de calcul ne peut pas être null")
    private LocalDateTime dateCalcul;
    
    /**
     * Indique si le résultat est admis ou non
     * Généralement admis si MGP >= 2.0
     */
    @Column(nullable = false)
    private Boolean admis;
    
    // Constructeurs
    
    /**
     * Constructeur par défaut requis par JPA
     */
    public ResultatMGP() {
        this.dateCalcul = LocalDateTime.now();
    }
    
    /**
     * Constructeur principal pour créer un résultat de calcul
     * 
     * @param ues Liste des UE utilisées pour le calcul
     * @param mgp La moyenne générale pondérée calculée
     * @param mention La mention correspondante
     * @param totalCredits Le total des crédits
     * @param totalPoints Le total des points
     */
    public ResultatMGP(List<UE> ues, Double mgp, String mention, 
                      Integer totalCredits, Double totalPoints) {
        this();
        this.ues = new ArrayList<>(ues);
        this.mgp = mgp;
        this.mention = mention;
        this.totalCredits = totalCredits;
        this.totalPoints = totalPoints;
        this.nombreUE = ues.size();
        this.admis = mgp >= 2.0; // Seuil d'admission à UY1
    }
    
    // Getters et Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNomEtudiant() {
        return nomEtudiant;
    }
    
    public void setNomEtudiant(String nomEtudiant) {
        this.nomEtudiant = nomEtudiant;
    }
    
    public List<UE> getUes() {
        return ues;
    }
    
    public void setUes(List<UE> ues) {
        this.ues = ues;
        this.nombreUE = ues != null ? ues.size() : 0;
    }
    
    public Double getMgp() {
        return mgp;
    }
    
    public void setMgp(Double mgp) {
        this.mgp = mgp;
    }
    
    public String getMention() {
        return mention;
    }
    
    public void setMention(String mention) {
        this.mention = mention;
    }
    
    public Integer getTotalCredits() {
        return totalCredits;
    }
    
    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }
    
    public Double getTotalPoints() {
        return totalPoints;
    }
    
    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }
    
    public Integer getNombreUE() {
        return nombreUE;
    }
    
    public void setNombreUE(Integer nombreUE) {
        this.nombreUE = nombreUE;
    }
    
    public LocalDateTime getDateCalcul() {
        return dateCalcul;
    }
    
    public void setDateCalcul(LocalDateTime dateCalcul) {
        this.dateCalcul = dateCalcul;
    }
    
    public Boolean getAdmis() {
        return admis;
    }
    
    public void setAdmis(Boolean admis) {
        this.admis = admis;
    }
    
    // Méthodes utilitaires
    
    /**
     * Calcule le pourcentage de réussite (UE validées / total UE)
     * 
     * @return Le pourcentage de réussite
     */
    public Double getPourcentageReussite() {
        if (ues == null || ues.isEmpty()) {
            return 0.0;
        }
        
        long ueValidees = ues.stream()
                .filter(UE::isValidee)
                .count();
        
        return (double) ueValidees / ues.size() * 100;
    }
    
    /**
     * Compte le nombre d'UE validées (note >= 40)
     * 
     * @return Le nombre d'UE validées
     */
    public long getNombreUEValidees() {
        if (ues == null) {
            return 0;
        }
        return ues.stream()
                .filter(UE::isValidee)
                .count();
    }
    
    /**
     * Compte le nombre d'UE en échec (note < 35)
     * 
     * @return Le nombre d'UE en échec
     */
    public long getNombreUEEnEchec() {
        if (ues == null) {
            return 0;
        }
        return ues.stream()
                .filter(UE::isEnEchec)
                .count();
    }
    
    /**
     * Détermine la décision finale selon les règles d'UY1
     * 
     * @return La décision finale ("ADMIS", "CANT", "ECHEC")
     */
    public String getDecisionFinale() {
        if (mgp >= 2.0) {
            return "ADMIS";
        } else if (mgp >= 1.0) {
            return "CANT"; // Crédit Accordé Non Transférable
        } else {
            return "ECHEC";
        }
    }
    
    /**
     * Formate le MGP pour affichage (2 décimales)
     * 
     * @return Le MGP formaté
     */
    public String getMgpFormate() {
        return String.format("%.2f", mgp);
    }
    
    /**
     * Ajoute une UE à la liste
     * 
     * @param ue L'UE à ajouter
     */
    public void ajouterUE(UE ue) {
        if (this.ues == null) {
            this.ues = new ArrayList<>();
        }
        this.ues.add(ue);
        this.nombreUE = this.ues.size();
    }
    
    @Override
    public String toString() {
        return "ResultatMGP{" +
                "id=" + id +
                ", nomEtudiant='" + nomEtudiant + '\'' +
                ", mgp=" + mgp +
                ", mention='" + mention + '\'' +
                ", totalCredits=" + totalCredits +
                ", totalPoints=" + totalPoints +
                ", nombreUE=" + nombreUE +
                ", dateCalcul=" + dateCalcul +
                ", admis=" + admis +
                '}';
    }
}