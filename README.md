# SharedVillagerDiscounts

A Fabric mod for Minecraft `26.2` that shares villager discounts between players.

## Modes

The default mode is `share_all_existing`.

- `share_all_existing` shares the best current positive villager discount already present on that villager
- `cured_only` only shares discounts captured from zombie villager cures after the mod is installed

## Config

The config file is stored at:

- `config/shared-villager-discounts.json`

Default config:

```json
{
  "sharingMode": "SHARE_ALL_EXISTING",
  "syncOnInteract": true
}
```

What the settings mean:

- `sharingMode: "SHARE_ALL_EXISTING"` lets any player inherit the best positive discount that villager already has
- `sharingMode: "CURED_ONLY"` only shares discounts captured from zombie villager cures that happen after this mod is installed
- `syncOnInteract: true` applies the sharing rule when a player interacts with a villager

Server note:

- on a dedicated server, the server's own `config/shared-villager-discounts.json` controls the behavior for that world
- players having or not having Mod Menu installed does not override the server's setting

If Mod Menu is installed on the client, it can open a simple config screen for this mod. That is most useful for singleplayer or a local host. Dedicated servers still use their own server-side config file.

## Building

Use:

- `./gradle-local build`

The built jar will be written to `build/libs/`.
