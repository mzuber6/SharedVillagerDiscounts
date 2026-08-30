package io.github.mzuber.sharedvillagerdiscounts;

public interface VillagerDiscountAccess {
    DiscountSnapshot ovd$getStoredCureDiscount();

    boolean ovd$hasStoredCureDiscount();

    void ovd$setStoredCureDiscount(int majorPositive, int minorPositive);
}
