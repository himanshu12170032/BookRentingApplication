package com.example.bookrent.Rules;

public class CasualReaderRentalRule implements RentalLimitRule {

    @Override
    public int getMaxRents() {
        return 2;
    }

    @Override
    public int getMaxRentalDays() {
        return 7;
    }

    @Override
    public double getDiscount() {
        return 0.05; // Discount logic moved inside
    }
}
