package io.github.mzuber.sharedvillagerdiscounts;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SharedVillagerDiscountsMod implements ModInitializer {
    public static final String MOD_ID = "sharedvillagerdiscounts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static SharedVillagerDiscountsConfig config;

    @Override
    public void onInitialize() {
        config = SharedVillagerDiscountsConfig.load(FabricLoader.getInstance().getConfigDir());

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClientSide() || hand != InteractionHand.MAIN_HAND) {
                return InteractionResult.PASS;
            }
            if (!(player instanceof ServerPlayer serverPlayer)) {
                return InteractionResult.PASS;
            }
            if (!(entity instanceof Villager villager)) {
                return InteractionResult.PASS;
            }
            if (!config.syncOnInteract()) {
                return InteractionResult.PASS;
            }

            VillagerDiscountLogic.applyConfiguredDiscount(villager, serverPlayer, config.sharingMode());
            return InteractionResult.PASS;
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) ->
            registerCommands(dispatcher)
        );
    }

    public static SharedVillagerDiscountsConfig config() {
        return config;
    }

    public static void reloadConfig() {
        config = SharedVillagerDiscountsConfig.load(FabricLoader.getInstance().getConfigDir());
    }

    public static void saveConfig(SharedVillagerDiscountsConfig updatedConfig) {
        config = updatedConfig;
        config.save(FabricLoader.getInstance().getConfigDir());
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("villagerdiscount")
            .then(Commands.literal("reload")
                .executes(context -> {
                    reloadConfig();
                    context.getSource().sendSuccess(
                        () -> Component.literal("Reloaded SharedVillagerDiscounts config."),
                        false
                    );
                    return 1;
                }))
            .then(Commands.literal("mode")
                .executes(context -> {
                    context.getSource().sendSuccess(
                        () -> Component.literal("Current sharing mode: " + config.sharingMode().configValue()),
                        false
                    );
                    return 1;
                })));
    }
}
