# 🪦 GraveWarden
**Modern item protection. Secure, intuitive, and feature-rich.**

GraveWarden is a lightweight yet powerful death management solution for Paper/Spigot servers. While it maintains a "plug-and-play" soul, the latest updates introduce advanced management tools for players and admins, ensuring that no item is ever lost to the void.

---

## ✨ Key Features

* **📦 Precise Inventory Restoration:** Remembers your exact layout. Restores items to their original slots (Hotbar, Armor, Off-hand) instantly.
* **🖥️ Interactive Grave GUI:** Use `/grave list` to open a visual menu of all your active graves. View coordinates, death time, and world info at a glance.
* **🛡️ Overflow & Protection:** If your inventory is full, excess items drop safely at your feet. Includes built-in protection against lava and void deaths.
* **👻 Native Holograms:** Beautiful player-head graves with floating text. **No external dependencies** (like DecentHolograms) required.
* **🌌 Soul Effects:** Immersive particle bursts and sound effects upon grave retrieval for a premium feel.
* **🛠️ Admin Toolbox:** Full control with commands to list, teleport to, or purge graves for any player or the entire server.

---

## 🛠️ Technical Requirements

* **Server Version:** Paper/Spigot 1.20.6 - 1.21.x (Optimized for 1.21.1)
* **Java:** 21 or higher
* **Dependencies:** **None.** Everything is built-in.

---

## 📖 How it Works

1. **Death:** A grave is generated at your death location using your player's skin.
2. **Management:** Open the **Grave List GUI** to track your deaths or find your way back.
3. **Recovery:** **Right-Click** your grave. Items fly back to their original slots, and a "Soul Burst" effect triggers.
4. **Admin Control:** Admins can manage the afterlife using the comprehensive `/grave` command suite.

---

## ⌨️ Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/grave list` | Opens your active graves GUI | `grave.list` |
| `/grave list <player>` | Opens another player's graves GUI | `grave.list.others` |
| `/grave remove <player>` | Purges all graves for a specific player | `grave.remove` |
| `/grave remove-all` | Global wipe of all server graves | `grave.remove.all` |
| `/grave help` | Displays the help menu | *None* |

> **[TIP]**
> Use `grave.*` for full access to all features and to receive update notifications.

---

## 🤝 Support
Found a bug or have a suggestion? Feel free to open an issue on the GitHub repository.

*Developed with ❤️ by zNxki_*