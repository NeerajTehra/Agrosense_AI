package com.example.agrosense.ml;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SeverityEstimatorTest {

    @Test
    public void testEstimateSeverity_Critical() {
        assertEquals("CRITICAL", SeverityEstimator.estimateSeverity(0.96));
        assertEquals("CRITICAL", SeverityEstimator.estimateSeverity(1.0));
    }

    @Test
    public void testEstimateSeverity_High() {
        assertEquals("HIGH", SeverityEstimator.estimateSeverity(0.86));
        assertEquals("HIGH", SeverityEstimator.estimateSeverity(0.94));
    }

    @Test
    public void testEstimateSeverity_Moderate() {
        assertEquals("MODERATE", SeverityEstimator.estimateSeverity(0.71));
        assertEquals("MODERATE", SeverityEstimator.estimateSeverity(0.84));
    }

    @Test
    public void testEstimateSeverity_Low() {
        assertEquals("LOW", SeverityEstimator.estimateSeverity(0.1));
        assertEquals("LOW", SeverityEstimator.estimateSeverity(0.69));
    }

    @Test
    public void testGetSeverityValue() {
        assertEquals(4, SeverityEstimator.getSeverityValue("CRITICAL"));
        assertEquals(3, SeverityEstimator.getSeverityValue("HIGH"));
        assertEquals(2, SeverityEstimator.getSeverityValue("MODERATE"));
        assertEquals(1, SeverityEstimator.getSeverityValue("LOW"));
        assertEquals(0, SeverityEstimator.getSeverityValue("UNKNOWN"));
    }
}
