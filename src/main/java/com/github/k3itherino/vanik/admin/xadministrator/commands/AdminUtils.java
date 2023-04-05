package com.github.k3itherino.vanik.admin.xadministrator.commands;

import com.github.k3itherino.vanik.admin.xadministrator.XAdministrator;
import com.github.k3itherino.vanik.admin.xadministrator.listeners.Page;
import com.github.k3itherino.vanik.admin.xadministrator.listeners.PageInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminUtils implements CommandExecutor, Listener {
    XAdministrator mcPlugin;
    int currPage = 0;
    ArrayList<Inventory> pages = new ArrayList<Inventory>();
    String adminInvName = "Admin Menu";
    String tpaInvName = "Tp a";
    public AdminUtils(XAdministrator plugin) {
        mcPlugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    ArrayList<String> currentlyHiding = new ArrayList<>();
    @EventHandler
    public void onMenuClick(InventoryClickEvent inventoryClick) {
        Player whoClicked = (Player) inventoryClick.getWhoClicked();

        if (inventoryClick.getView().getTitle() == adminInvName) {
            int slot = inventoryClick.getSlot();

            whoClicked.closeInventory();
            if (slot == 11) {
                whoClicked.setAllowFlight(!whoClicked.getAllowFlight());
                whoClicked.sendMessage(whoClicked.getAllowFlight() ? "Permiso de vuelo aplicado" : "Permiso de vuelo revocado");
            } else if (slot == 13) {
                ArrayList<ItemStack> playerHeads = new ArrayList<ItemStack>();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (int i = 0;i < 100;i++) {
                        playerHeads.add(getHead(player));
                    }
                }
                Page customPage = new Page("dwa", whoClicked);
                customPage.addItem(new ItemStack(Material.TORCH), 34);
                new PageInv(whoClicked, playerHeads, "Tp a", mcPlugin).addPage(customPage);

            } else if (slot == 15) {
                boolean isInvisible = checkIfInArr(currentlyHiding, whoClicked.getDisplayName());

                if (isInvisible) {
                    whoClicked.sendMessage("Eres visible");
                    hideOrShowFromEveryone(whoClicked, false);
                    currentlyHiding.remove(whoClicked.getDisplayName());
                } else {
                    whoClicked.sendMessage("Eres invisible");
                    hideOrShowFromEveryone(whoClicked, true);
                    currentlyHiding.add(whoClicked.getDisplayName());
                }

            } else if (slot == 26) {
                whoClicked.closeInventory();
            }
            inventoryClick.setCancelled(true);
        }
    }

    private void hideOrShowFromEveryone(Player player, boolean hide) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (hide) {
                otherPlayer.hidePlayer(player);
                return;
            }
            otherPlayer.showPlayer(otherPlayer);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(("Cant use this command from console"));
            return true;
        }
        Player player = (Player) sender;

        Inventory inv = Bukkit.createInventory(player, 9*3, adminInvName);

        inv.setItem(11, getItem(new ItemStack(Material.FEATHER), "Volar", "Clickea para volar"));
        inv.setItem(13, getItem(new ItemStack(Material.CHEST), "Abrir", "Clickea para abrir el menú"));
        inv.setItem(15, getItem(new ItemStack(Material.SKELETON_SKULL), "Vanish", "Clickea para volverte invisible"));
        inv.setItem(26, getItem(new ItemStack(Material.BARRIER), "Cerrar", "Clickea para cerrar el menú"));

        player.openInventory(inv);
        return true;
    }

    private boolean checkIfInArr(ArrayList<String> Array, String value) {
        for (String currentVal : Array) {
            if (value == currentVal) {
                return true;
            }
        }
        return false;
    }

    private ItemStack getItem(ItemStack item, String name, String ... lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        List<String> lores = new ArrayList<>();
        for (String value: lore) {
            lores.add(value);
        }

        meta.setLore(lores);

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getHead(Player player) {
        ItemStack item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(player.getName());
        skull.setOwner(player.getName());
        item.setItemMeta(skull);
        return item;
    }
}