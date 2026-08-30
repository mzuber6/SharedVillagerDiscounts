package io.github.mzuber.sharedvillagerdiscounts.mixin;

import io.github.mzuber.sharedvillagerdiscounts.VillagerDiscountLogic;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobConversionMixin {
    @Inject(
        method = "convertTo(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/ConversionParams;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/ConversionParams$AfterConversion;)Lnet/minecraft/world/entity/Mob;",
        at = @At("RETURN")
    )
    private <T extends Mob> void ovd$captureCuredVillager(
        EntityType<T> entityType,
        ConversionParams conversionParams,
        EntitySpawnReason reason,
        ConversionParams.AfterConversion<T> finalizer,
        CallbackInfoReturnable<T> cir
    ) {
        if (!((Object) this instanceof ZombieVillager)) {
            return;
        }
        if (!(cir.getReturnValue() instanceof Villager villager)) {
            return;
        }

        VillagerDiscountLogic.storeCureDiscount(villager);
    }
}
