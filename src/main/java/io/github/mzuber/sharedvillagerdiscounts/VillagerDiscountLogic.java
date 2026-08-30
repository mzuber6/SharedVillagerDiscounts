package io.github.mzuber.sharedvillagerdiscounts;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.villager.Villager;

public final class VillagerDiscountLogic {
    private VillagerDiscountLogic() {
    }

    public static boolean applyConfiguredDiscount(Villager villager, ServerPlayer player, SharingMode mode) {
        return switch (mode) {
            case SHARE_ALL_EXISTING -> applyDiscount(villager, player, findBestDiscount(villager));
            case CURED_ONLY -> applyDiscount(villager, player, getStoredCureDiscount(villager));
        };
    }

    public static DiscountSnapshot findBestDiscount(Villager villager) {
        GossipContainer gossips = villager.getGossips();
        int bestMajor = 0;
        int bestMinor = 0;

        for (Map.Entry<UUID, Object2IntMap<GossipType>> entry : gossips.getGossipEntries().entrySet()) {
            Object2IntMap<GossipType> values = entry.getValue();
            bestMajor = Math.max(bestMajor, values.getInt(GossipType.MAJOR_POSITIVE));
            bestMinor = Math.max(bestMinor, values.getInt(GossipType.MINOR_POSITIVE));
        }

        return new DiscountSnapshot(bestMajor, bestMinor);
    }

    public static void storeCureDiscount(Villager villager) {
        DiscountSnapshot best = findBestDiscount(villager);
        if (!best.hasAnyDiscount()) {
            return;
        }

        ((VillagerDiscountAccess) villager).ovd$setStoredCureDiscount(best.majorPositive(), best.minorPositive());
    }

    public static DiscountSnapshot getStoredCureDiscount(Villager villager) {
        return ((VillagerDiscountAccess) villager).ovd$getStoredCureDiscount();
    }

    private static boolean applyDiscount(Villager villager, ServerPlayer player, DiscountSnapshot shared) {
        if (!shared.hasAnyDiscount()) {
            return false;
        }

        GossipContainer gossips = villager.getGossips();
        UUID playerId = player.getUUID();
        int currentMajor = gossips.getReputation(playerId, type -> type == GossipType.MAJOR_POSITIVE);
        int currentMinor = gossips.getReputation(playerId, type -> type == GossipType.MINOR_POSITIVE);
        int majorToAdd = Math.max(0, shared.majorPositive() - currentMajor);
        int minorToAdd = Math.max(0, shared.minorPositive() - currentMinor);

        if (majorToAdd == 0 && minorToAdd == 0) {
            return false;
        }

        if (majorToAdd > 0) {
            gossips.add(playerId, GossipType.MAJOR_POSITIVE, majorToAdd);
        }
        if (minorToAdd > 0) {
            gossips.add(playerId, GossipType.MINOR_POSITIVE, minorToAdd);
        }

        return true;
    }
}
