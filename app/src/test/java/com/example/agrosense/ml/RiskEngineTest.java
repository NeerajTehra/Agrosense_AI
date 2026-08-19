package com.example.agrosense.ml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RiskEngineTest {

    @Test
    public void testCalculateRiskScore_Critical() {
        // High severity, high confidence, high humidity, optimal temp
        int score = RiskEngine.calculateRiskScore("CRITICAL", 0.98, 25.0, 90.0);
        assertTrue("Score should be high for critical conditions", score >= 90);
    }

    @Test
    public void testCalculateRiskScore_Low() {
        // Low severity, low confidence, low humidity, low temp
        int score = RiskEngine.calculateRiskScore("LOW", 0.3, 10.0, 30.0);
        assertTrue("Score should be low for safe conditions", score < 30);
    }

    @Test
    public void testRiskLevelMapping() {
        assertEquals("CRITICAL", RiskEngine.getRiskLevel(85));
        assertEquals("HIGH", RiskEngine.getRiskLevel(65));
        assertEquals("MODERATE", RiskEngine.getRiskLevel(45));
        assertEquals("LOW", RiskEngine.getRiskLevel(20));
    }

    @Test
    public void testScoreBoundaries() {
        int lowScore = RiskEngine.calculateRiskScore("LOW", 0.0, 0.0, 0.0);
        int highScore = RiskEngine.calculateRiskScore("CRITICAL", 1.0, 25.0, 90.0);
        
        assertTrue("Score should be >= 0", lowScore >= 0);
        assertTrue("Score should be <= 100", highScore <= 100);
    }
}
