package io.github.mzuber.sharedvillagerdiscounts.mixin;

import io.github.mzuber.sharedvillagerdiscounts.DiscountSnapshot;
import io.github.mzuber.sharedvillagerdiscounts.VillagerDiscountAccess;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin implements VillagerDiscountAccess {
    @Unique
    private static final String OVD_MARKER_KEY = "SharedVillagerDiscountsCured";
    @Unique
    private static final String OVD_MAJOR_KEY = "SharedVillagerDiscountsMajorPositive";
    @Unique
    private static final String OVD_MINOR_KEY = "SharedVillagerDiscountsMinorPositive";

    @Unique
    private boolean ovd$hasStoredCureDiscount;
    @Unique
    private int ovd$storedMajorPositive;
    @Unique
    private int ovd$storedMinorPositive;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void ovd$writeDiscountData(ValueOutput tag, CallbackInfo ci) {
        if (!ovd$hasStoredCureDiscount) {
            return;
        }

        tag.putBoolean(OVD_MARKER_KEY, true);
        tag.putInt(OVD_MAJOR_KEY, ovd$storedMajorPositive);
        tag.putInt(OVD_MINOR_KEY, ovd$storedMinorPositive);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void ovd$readDiscountData(ValueInput tag, CallbackInfo ci) {
        ovd$hasStoredCureDiscount = tag.getBooleanOr(OVD_MARKER_KEY, false);
        ovd$storedMajorPositive = tag.getIntOr(OVD_MAJOR_KEY, 0);
        ovd$storedMinorPositive = tag.getIntOr(OVD_MINOR_KEY, 0);
    }

    @Override
    public DiscountSnapshot ovd$getStoredCureDiscount() {
        if (!ovd$hasStoredCureDiscount) {
            return new DiscountSnapshot(0, 0);
        }

        return new DiscountSnapshot(ovd$storedMajorPositive, ovd$storedMinorPositive);
    }

    @Override
    public boolean ovd$hasStoredCureDiscount() {
        return ovd$hasStoredCureDiscount;
    }

    @Override
    public void ovd$setStoredCureDiscount(int majorPositive, int minorPositive) {
        ovd$hasStoredCureDiscount = true;
        ovd$storedMajorPositive = majorPositive;
        ovd$storedMinorPositive = minorPositive;
    }
}
