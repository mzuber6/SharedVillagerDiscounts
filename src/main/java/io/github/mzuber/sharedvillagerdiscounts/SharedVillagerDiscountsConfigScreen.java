package io.github.mzuber.sharedvillagerdiscounts;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SharedVillagerDiscountsConfigScreen extends Screen {
    private final Screen parent;
    private SharingMode sharingMode;
    private Button modeButton;

    public SharedVillagerDiscountsConfigScreen(Screen parent) {
        super(Component.literal("SharedVillagerDiscounts"));
        this.parent = parent;
        this.sharingMode = SharedVillagerDiscountsMod.config().sharingMode();
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int top = height / 3;

        modeButton = addRenderableWidget(Button.builder(modeLabel(), button -> {
            sharingMode = sharingMode.next();
            button.setMessage(modeLabel());
        }).bounds(centerX - 110, top, 220, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Save"), button -> {
            SharedVillagerDiscountsMod.saveConfig(SharedVillagerDiscountsMod.config().withSharingMode(sharingMode));
            onClose();
        }).bounds(centerX - 110, top + 48, 105, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> onClose())
            .bounds(centerX + 5, top + 48, 105, 20)
            .build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
        extractBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.centeredText(font, title, width / 2, 32, 0xFFFFFF);
        guiGraphics.centeredText(font, "Default: share all existing villager discounts", width / 2, 56, 0xA0A0A0);
        guiGraphics.centeredText(font, "Dedicated servers still use the server config file.", width / 2, 68, 0xA0A0A0);
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreenAndShow(parent);
        }
    }

    private Component modeLabel() {
        return Component.literal("Sharing Mode: " + sharingMode.displayName());
    }
}
