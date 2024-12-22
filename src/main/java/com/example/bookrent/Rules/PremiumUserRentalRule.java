package com.example.bookrent.Rules;

public class PremiumUserRentalRule implements RentalLimitRule {

    @Override
    public int getMaxRents() {
        return 10;
    }

    @Override
    public int getMaxRentalDays() {
        return 30;
    }

    @Override
    public double getDiscount() {
        return 0.50;
    }
}
