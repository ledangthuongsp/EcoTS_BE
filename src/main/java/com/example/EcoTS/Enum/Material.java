package com.example.EcoTS.Enum;

public enum Material {
    PAPER(150, 0.46),
    METAL(500, 2.1),
    PLASTIC(200, 0.76),
    GLASS(100, 0.38);

    private final double pointsPerKg;
    private final double co2SavedPerKg;

    Material(double pointsPerKg, double co2SavedPerKg) {
        this.pointsPerKg = pointsPerKg;
        this.co2SavedPerKg = co2SavedPerKg;
    }

    public double getPointsPerKg() {
        return pointsPerKg;
    }

    public double getCo2SavedPerKg() {
        return co2SavedPerKg;
    }
}