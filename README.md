# tablist-tweaks

![](https://img.shields.io/badge/MINECRAFT-1.16+-966C4A?style=for-the-badge&labelColor=53AC56)
![](https://img.shields.io/badge/JAVA-1.8+-5283A2?style=for-the-badge&labelColor=E86F00)

[Modrinth](https://modrinth.com/plugin/tablist-tweaks) ·
[CurseForge](https://www.curseforge.com/minecraft/bukkit-plugins/tablist-tweaks) ·
[SpigotMC](https://www.spigotmc.org/resources/tablist-tweaks.109018)

A Spigot (Bukkit) Minecraft that improves the player list (shown on Tab button press)


| Default  | Dimension Dots |
|----------|----------------|
| ![](https://cdn.modrinth.com/data/MDSeFHTz/images/d9c0e5e978036931665b3d4dcb894899764e4465.png) | ![](https://cdn.modrinth.com/data/MDSeFHTz/images/29d827ef25d4b87c282dab696cd6c2f19374f313.png) |


## Features (can be disabled through configuration)

- The color of the player's name is associated with the dimension in which they are located:
  - ![](https://via.placeholder.com/15/55ff55/55ff55.png) Green for Overworld
  - ![](https://via.placeholder.com/15/ff5555/ff5555.png) Red for Nether
  - ![](https://via.placeholder.com/15/ff55ff/ff55ff.png) Purple for The End
- The "idle" badge is placed after the player's name if they haven't moved for some time


## Compatibility
**Tablist tweaks is compatible with [TAB](https://modrinth.com/plugin/tab-was-taken)!**  
As for other similar plugins - most likely not.


## Usage

### Commands

`/tablisttweaks` is the main plugin command, which has the alias `/tt`.

| Command              | Description                                                   |
|----------------------|---------------------------------------------------------------|
| `/tt help [command]` | Show help for given command, for available commands otherwise |
| `/tt reload`         | Reload config                                                 |


## Configuration

- The time player has to stand still to get the "idle" badge (in seconds)
- Enable/disable features
  - Dimension colors
    - Dot format (see images)
  - Idle badge
- Plugin messages
  - error
  - help
  - warning
  - info


## Permissions

| Permission node        | Default | Description                                               |
|------------------------|---------|-----------------------------------------------------------|
| `tablisttweaks.help`   | true    | Allows to use `/tt help` (lists only available commands)  |
| `tablisttweaks.reload` | op      | Allows to use `/tt reload`                                |
| `tablisttweaks.admin`  | op      | Refers to `tablisttweaks.reload` by default               |


## Supported versions
Plugin works properly on **1.16** and above. May work on older versions, probably without problems down to **1.13**.  
Requires **Java 1.8** or higher.


## Reference

- [MineShield server](https://shield.land/mineshield)'s tab as inspiration


## Special thanks to:

- [Legitimoose](https://www.youtube.com/c/Legitimoose) for amazing Paper (Bukkit) plugin (in Kotlin) project setup [tutorial](https://youtu.be/5DBJcz0ceaw)
- [BeBr0](https://www.youtube.com/c/BeBr0) for Spigot (Bukkit) plugin development [tutorial [RU]](https://youtube.com/playlist?list=PLlLq-eYkh0bB_uyZN4NdzkxLBs9glZmIT) with very clear API explanation


## Copyright

The project itself is distributed under [GNU GPLv3](./LICENSE).
