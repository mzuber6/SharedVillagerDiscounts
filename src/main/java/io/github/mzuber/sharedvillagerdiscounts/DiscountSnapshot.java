package io.github.mzuber.sharedvillagerdiscounts;

public record DiscountSnapshot(int majorPositive, int minorPositive) {
    public boolean hasAnyDiscount() {
        return majorPositive > 0 || minorPositive > 0;
    }
}
