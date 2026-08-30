package io.github.mzuber.sharedvillagerdiscounts;

public enum SharingMode {
    SHARE_ALL_EXISTING,
    CURED_ONLY;

    public String configValue() {
        return name().toLowerCase();
    }

    public String displayName() {
        return switch (this) {
            case SHARE_ALL_EXISTING -> "Share All Existing";
            case CURED_ONLY -> "Cured Only";
        };
    }

    public SharingMode next() {
        return switch (this) {
            case SHARE_ALL_EXISTING -> CURED_ONLY;
            case CURED_ONLY -> SHARE_ALL_EXISTING;
        };
    }
}
