package io.github.mzuber.sharedvillagerdiscounts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public record SharedVillagerDiscountsConfig(SharingMode sharingMode, boolean syncOnInteract) {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "shared-villager-discounts.json";

    public static SharedVillagerDiscountsConfig defaults() {
        return new SharedVillagerDiscountsConfig(SharingMode.SHARE_ALL_EXISTING, true);
    }

    public static SharedVillagerDiscountsConfig load(Path configDir) {
        Path path = configDir.resolve(FILE_NAME);

        if (Files.notExists(path)) {
            SharedVillagerDiscountsConfig defaults = defaults();
            defaults.save(configDir);
            return defaults;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            SharedVillagerDiscountsConfig loaded = GSON.fromJson(reader, SharedVillagerDiscountsConfig.class);
            if (loaded == null) {
                return defaults();
            }

            SharingMode mode = loaded.sharingMode() == null ? SharingMode.SHARE_ALL_EXISTING : loaded.sharingMode();
            return new SharedVillagerDiscountsConfig(mode, loaded.syncOnInteract());
        } catch (IOException exception) {
            SharedVillagerDiscountsMod.LOGGER.warn("Failed to load config, using defaults.", exception);
            return defaults();
        }
    }

    public void save(Path configDir) {
        Path path = configDir.resolve(FILE_NAME);

        try {
            Files.createDirectories(configDir);
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException exception) {
            SharedVillagerDiscountsMod.LOGGER.error("Failed to save config.", exception);
        }
    }

    public SharedVillagerDiscountsConfig withSharingMode(SharingMode updatedMode) {
        return new SharedVillagerDiscountsConfig(updatedMode, syncOnInteract);
    }
}
