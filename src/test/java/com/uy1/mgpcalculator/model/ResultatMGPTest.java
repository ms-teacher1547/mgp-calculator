// src/test/java/com/uy1/mgpcalculator/model/ResultatMGPTest.java
package com.uy1.mgpcalculator.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ResultatMGPTest {
    
    @Test
    public void testResultatMGPCreation() {
        // Créer quelques UE de test
        UE ue1 = new UE("Java", 6, 85.0);
        ue1.setQualitePoints(4.0);
        
        UE ue2 = new UE("Math", 6, 65.0);
        ue2.setQualitePoints(3.0);
        
        List<UE> ues = Arrays.asList(ue1, ue2);
        
        ResultatMGP resultat = new ResultatMGP(ues, 3.5, "Très Bien", 12, 42.0);
        
        assertEquals(3.5, resultat.getMgp());
        assertEquals("Très Bien", resultat.getMention());
        assertEquals(12, resultat.getTotalCredits());
        assertEquals(42.0, resultat.getTotalPoints());
        assertEquals(2, resultat.getNombreUE());
        assertTrue(resultat.getAdmis());
        assertEquals("ADMIS", resultat.getDecisionFinale());
    }
    
    @Test
    public void testPourcentageReussite() {
        UE ue1 = new UE("UE1", 6, 85.0); // Validée
        UE ue2 = new UE("UE2", 6, 30.0); // En échec
        
        List<UE> ues = Arrays.asList(ue1, ue2);
        ResultatMGP resultat = new ResultatMGP(ues, 2.0, "Passable", 12, 24.0);
        
        assertEquals(50.0, resultat.getPourcentageReussite());
        assertEquals(1, resultat.getNombreUEValidees());
        assertEquals(1, resultat.getNombreUEEnEchec());
    }
}