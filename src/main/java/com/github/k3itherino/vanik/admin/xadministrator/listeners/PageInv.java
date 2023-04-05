package com.github.k3itherino.vanik.admin.xadministrator.listeners;

import com.github.k3itherino.vanik.admin.xadministrator.XAdministrator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PageInv implements Listener {
    String name;
    Player player;

    ArrayList<Inventory> pages = new ArrayList<Inventory>();

    int currPage = 0;
    public PageInv(Player p, ArrayList<ItemStack> Items, String invName, XAdministrator plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        name = invName;
        player = p;
        if (Items != null) {
            Inventory page = getNewPage();
            for (ItemStack item : Items) {
                if (page.firstEmpty() == 44) {
                    page.addItem(item);
                    pages.add(page);
                    page = getNewPage();
                } else {
                    page.addItem(item);
                }
            }
            pages.add(page);
        }
        p.openInventory(pages.get(currPage));
    }

    @EventHandler
    private void onPlayerClick(InventoryClickEvent inv) {
        if (inv.getView().getTitle() == name) {
            inv.setCancelled(true);
            int slot = inv.getSlot();

            if (slot == 53) {
                if (currPage < pages.size() - 1) {
                    currPage = currPage + 1;
                    inv.getView().getPlayer().openInventory(pages.get(currPage));
                }

            }if (slot == 45) {
                if (currPage != 0) {
                    currPage = currPage - 1;
                    inv.getView().getPlayer().openInventory(pages.get(currPage));
                }
            } if (slot != 45 && slot != 53) {
                ItemStack clickedItem = inv.getCurrentItem();
                if (clickedItem != null) {
                    String clickedPlayerName = inv.getCurrentItem().getItemMeta().getDisplayName();
                    Player clickedPlayer = Bukkit.getPlayer(clickedPlayerName);
                    player.teleport(clickedPlayer.getLocation());
                }
            }
        }
    }

    public PageInv addPage (Page page) {
        pages.add(page.getPage());
        return this;
    }

    private Inventory getNewPage(){
        Inventory page = Bukkit.createInventory(null, 54, name);

        ItemStack nextpage =  new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta meta = nextpage.getItemMeta();
        meta.setDisplayName("Siguiente");
        nextpage.setItemMeta(meta);

        ItemStack prevpage = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1, (byte) 2);
        meta = prevpage.getItemMeta();
        meta.setDisplayName("Atras");
        prevpage.setItemMeta(meta);


        page.setItem(53, nextpage);
        page.setItem(45, prevpage);
        return page;
    }
}


