// src/test/java/com/uy1/mgpcalculator/model/UETest.java
package com.uy1.mgpcalculator.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UETest {
    
    @Test
    public void testUECreation() {
        UE ue = new UE("Programmation Java", 6, 85.0);
        
        assertEquals("Programmation Java", ue.getNom());
        assertEquals(6, ue.getCredits());
        assertEquals(85.0, ue.getNote());
        assertTrue(ue.isValidee());
        assertFalse(ue.isEnEchec());
    }
    
    @Test
    public void testPointsTotaux() {
        UE ue = new UE("Test", 6, 70.0);
        ue.setQualitePoints(3.3); // B+
        
        assertEquals(19.8, ue.getPointsTotaux(), 0.0001);
    }
}