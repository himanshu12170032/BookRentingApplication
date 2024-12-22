package com.example.bookrent.Rules;

public class ProfessionalRentalRule implements RentalLimitRule {

    @Override
    public int getMaxRents() {
        return 5;
    }

    @Override
    public int getMaxRentalDays() {
        return 21;
    }

    @Override
    public double getDiscount() {
        return 0.10;
    }
}
