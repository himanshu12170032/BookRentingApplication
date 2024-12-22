package com.example.bookrent.Rules;

import com.example.bookrent.Entity.Role;

public class RentalRuleStrategy {

    public static RentalLimitRule getRule(Role role) {
        return switch (role) {
            case STUDENTS -> new StudentRentalRule();
            case PROFESSIONAL -> new ProfessionalRentalRule();
            case CASUAL_READER -> new CasualReaderRentalRule();
            case PREMIUM_USER -> new PremiumUserRentalRule();
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
