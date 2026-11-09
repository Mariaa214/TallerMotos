package usta.motos.domain.service;



public class MaintenanceCostCalculator {

    private static final double IVA_PERCENTAGE = 0.19;

    public double calculateTotalCostWithIva(double baseCost) {
        if (baseCost < 0) {
            throw new IllegalArgumentException("El costo base no puede ser negativo");
        }
        return baseCost + (baseCost * IVA_PERCENTAGE);
    }


    public double applyDiscount(double cost, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100");
        }
        return cost - (cost * (discountPercentage / 100.0));
    }
}