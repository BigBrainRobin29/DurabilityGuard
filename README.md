# DurabilityGuard

[![Modrinth](https://img.shields.io/badge/dynamic/json?url=https://api.modrinth.com/v2/project/durability-guard&query=$.downloads&suffix=%20Downloads&logo=modrinth&label=Modrinth)](https://modrinth.com/mod/durability-guard/)

## What it does

DurabilityGuard is a highly configurable Minecraft mod that stops you from using a tool when it reaches a certain durability threshold and thus prevents it from breaking. 

## Keybind

You can toggle the mod on and off using the `Toggle` keybind in the `Durability Guard` category in the keybinds menu. By default, this keybind is not bound to any key, so you will need to set it up manually.

## Configuration

You can open the in-game configuration menu by running the `/durabilityguard` command in the chat.

<img width="1920" height="1080" alt="configuration screen" src="https://github.com/user-attachments/assets/292e7ca3-3dc7-45d1-b84f-70b992e46790" />

| Option                         | Default      | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
|--------------------------------|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Minimum Percentage             | `10`         | The minimum durability percentage a tool must have to be used if `Limit type` is set to `PERCENTAGE` or `BOTH`.                                                                                                                                                                                                                                                                                                                                                 |
| Minimum Durability             | `30`         | The minimum durability a tool must have to be used if `Limit type` is set to `DURABILITY` or `BOTH`.                                                                                                                                                                                                                                                                                                                                                            |
| Active                         | `true`       | Whether the mod is currently active.                                                                                                                                                                                                                                                                                                                                                                                                                            |
| Limit Type                     | `PERCENTAGE` | The type of limit to apply. Can be set to `PERCENTAGE`, `DURABILITY`, or `BOTH`.                                                                                                                                                                                                                                                                                                                                                                                |
| Use ignored items as whitelist | `false`      | If true, items in `Ignored items` will be treated as a whitelist, meaning items that are not in the list can always be used regardless of their durability.                                                                                                                                                                                                                                                                                                     |
| Ignored items                  | `[]`         | A list of items that will be ignored by the mod (if `Use ignored items as whitelist` is true, see description above). The items are specified by their item ID, which can be found in the game by hovering over the item in the inventory and looking at the tooltip. For example, `minecraft:diamond_pickaxe` for a diamond pickaxe. The `*` placeholder can be used to match any item of a certain type, e.g. `minecraft:stone_*` will match all stone tools. |

## Supported Versions

| Minecraft Version | Status    |
|-------------------|-----------|
| 1.21.9            | Supported |
| 1.21.6 - 1.21.8   | Supported |
| 1.21.5            | Supported |
| 1.21.4            | Supported |
| 1.21.2 - 1.21.3   | Supported |
| 1.21 - 1.21.1     | Supported |
| 1.20.5 - 1.20.6   | Supported |
| 1.20.4            | Supported |
| 1.20 - 1.20.1     | Supported |

<sub>This code is licensed under the [CC BY-NC-SA 4.0](https://creativecommons.org/licenses/by-nc-sa/4.0/deed) license.<sub>
