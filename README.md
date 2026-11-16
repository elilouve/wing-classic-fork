# Wings Classic

Craftable wings and flight for Minecraft **1.20.1** (Forge) — a port of the craftable-wings version (1.12.2) of the **Wings** mod by Pau101. No LLibrary needed.

*Port de la version « ailes craftables » (1.12.2) du mod Wings de Pau101. Aucune dépendance à LLibrary.*

## Features

- 9 pairs of wings: Angel, Slime, Blue Butterfly, Monarch Butterfly, Fire, Bat, Fairy, Evil and Dragon, each with its own animations (fire wings glow and leave particles).
- Fairy Dust Ore and Amethyst Ore (with deepslate variants), generated in the world.
- Bat Blood: right-click a bat with an empty bottle.
- Toggle flight with a key (**R** by default, rebindable). Sprint while flying (**Ctrl** + forward) to reach elytra-with-firework speed (about 30 blocks/s).
- Wings lose durability while flying; falls are cushioned.
- Optional [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios) support (back slot, compatible with Elytra Slot).
- Server config and client config files to tune speed, durability, ore generation, which wings are enabled, etc.

## Requirements

- Minecraft 1.20.1
- Forge
- Optional: Curios API

## Building from source

Requires JDK 17.

```
.\gradlew build        (Windows)
./gradlew build         (Linux / macOS)
```

The mod jar is created in `build/libs/`. To test in game: `.\gradlew runClient`.

## Credits and license

Original mod: [Wings](https://github.com/pau101/Wings) by Pau101 (MIT). Concept design of the original mod: LDShadowLady.
Port by **elilouve**. Released under the MIT license — see `LICENSE.txt` and `CREDITS.txt`.
