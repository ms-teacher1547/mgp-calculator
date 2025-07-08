package com.uy1.mgpcalculator.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.uy1.mgpcalculator.model.ResultatMGP;
import com.uy1.mgpcalculator.model.UE;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Service
public class PDFService {

    // Police personnalisée pour l'en-tête
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font SUBHEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC, BaseColor.GRAY);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

    /**
     * Génère un PDF à partir d'un résultat MGP
     * 
     * @param resultat Le résultat à convertir en PDF
     * @return Tableau de bytes contenant le PDF
     * @throws DocumentException en cas d'erreur de génération
     */
    public byte[] genererBulletinMGP(ResultatMGP resultat) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        PdfWriter.getInstance(document, outputStream);
        document.open();
        
        // Ajouter l'en-tête
        ajouterEnTete(document, resultat);
        
        // Ajouter les informations générales
        ajouterInfosGenerales(document, resultat);
        
        // Ajouter le tableau des UE
        ajouterTableauUEs(document, resultat);
        
        // Ajouter le résumé final
        ajouterResumeFinal(document, resultat);
        
        document.close();
        return outputStream.toByteArray();
    }

    private void ajouterEnTete(Document document, ResultatMGP resultat) throws DocumentException {
        Paragraph universite = new Paragraph("Université de Yaoundé I", HEADER_FONT);
        universite.setAlignment(Element.ALIGN_CENTER);
        document.add(universite);
        
        Paragraph faculte = new Paragraph("Faculté des Sciences", SUBHEADER_FONT);
        faculte.setAlignment(Element.ALIGN_CENTER);
        document.add(faculte);
        
        Paragraph titre = new Paragraph("Bulletin de Notes - Calcul MGP", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        titre.setAlignment(Element.ALIGN_CENTER);
        titre.setSpacingAfter(20f);
        document.add(titre);
    }

    private void ajouterInfosGenerales(Document document, ResultatMGP resultat) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        
        // Configuration des largeurs de colonnes
        float[] columnWidths = {1f, 2f};
        table.setWidths(columnWidths);
        
        ajouterCellule(table, "Étudiant:", BOLD_FONT);
        ajouterCellule(table, resultat.getNomEtudiant() != null ? resultat.getNomEtudiant() : "Non spécifié", NORMAL_FONT);
        
        ajouterCellule(table, "Date de calcul:", BOLD_FONT);
        ajouterCellule(table, resultat.getDateCalcul().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), NORMAL_FONT);
        
        ajouterCellule(table, "Nombre d'UE:", BOLD_FONT);
        ajouterCellule(table, String.valueOf(resultat.getNombreUE()), NORMAL_FONT);
        
        document.add(table);
    }

    private void ajouterTableauUEs(Document document, ResultatMGP resultat) throws DocumentException {
        Paragraph titreSection = new Paragraph("Détail des Unités d'Enseignement", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
        titreSection.setSpacingBefore(15f);
        titreSection.setSpacingAfter(10f);
        document.add(titreSection);
        
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(15f);
        
        // En-têtes du tableau
        Stream.of("UE", "Crédits", "Note", "Côte", "Points Qualité", "Mention")
            .forEach(header -> {
                PdfPCell cell = new PdfPCell(new Phrase(header, BOLD_FONT));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            });
        
        // Remplissage des données
        for (UE ue : resultat.getUes()) {
            table.addCell(new Phrase(ue.getNom(), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(ue.getCredits()), NORMAL_FONT));
            table.addCell(new Phrase(String.format("%.2f", ue.getNote()), NORMAL_FONT));
            table.addCell(new Phrase(ue.getCote(), NORMAL_FONT));
            table.addCell(new Phrase(String.format("%.1f", ue.getQualitePoints()), NORMAL_FONT));
            table.addCell(new Phrase(ue.getMention(), NORMAL_FONT));
        }
        
        document.add(table);
    }

    private void ajouterResumeFinal(Document document, ResultatMGP resultat) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(60);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setSpacingBefore(20f);
        
        ajouterCellule(table, "Total Crédits:", BOLD_FONT);
        ajouterCellule(table, String.valueOf(resultat.getTotalCredits()), NORMAL_FONT);
        
        ajouterCellule(table, "Total Points:", BOLD_FONT);
        ajouterCellule(table, String.format("%.2f", resultat.getTotalPoints()), NORMAL_FONT);
        
        ajouterCellule(table, "Moyenne Générale (MGP):", BOLD_FONT);
        PdfPCell mgpCell = new PdfPCell(new Phrase(String.format("%.2f/4.00", resultat.getMgp()), NORMAL_FONT));
        mgpCell.setBackgroundColor(new BaseColor(220, 220, 220));
        table.addCell(mgpCell);
        
        ajouterCellule(table, "Mention Générale:", BOLD_FONT);
        PdfPCell mentionCell = new PdfPCell(new Phrase(resultat.getMention(), NORMAL_FONT));
        mentionCell.setBackgroundColor(new BaseColor(220, 220, 220));
        table.addCell(mentionCell);
        
        ajouterCellule(table, "Décision Finale:", BOLD_FONT);
        PdfPCell decisionCell = new PdfPCell(new Phrase(resultat.getAdmis() ? "ADMIS" : "NON ADMIS", 
            new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, 
                resultat.getAdmis() ? BaseColor.GREEN : BaseColor.RED)));
        decisionCell.setBackgroundColor(new BaseColor(240, 240, 240));
        table.addCell(decisionCell);
        
        document.add(table);
    }

    private void ajouterCellule(PdfPTable table, String texte, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texte, font));
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
}