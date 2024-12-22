package com.example.bookrent.Rules;

public class StudentRentalRule implements RentalLimitRule{
    @Override
    public int getMaxRents() {
        return 3;
    }

    @Override
    public int getMaxRentalDays() {
        return 14;
    }
    @Override
    public double getDiscount() {
        return 0.20;
    }

}
