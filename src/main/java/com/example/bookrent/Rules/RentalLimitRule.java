package com.example.bookrent.Rules;

public interface RentalLimitRule{
    int getMaxRents();
    int getMaxRentalDays();
    public double getDiscount();
}
