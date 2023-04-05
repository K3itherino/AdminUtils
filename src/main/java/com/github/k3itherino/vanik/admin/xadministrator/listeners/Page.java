package com.github.k3itherino.vanik.admin.xadministrator.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Page {
    Inventory page;

    public Page(String name, Player player){

        page = Bukkit.createInventory(player, 53, name);

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
    }

    public Page addItem(ItemStack item, int position) {
        if (position != 45 && position != 53) {
            page.setItem(position, item);
        }
        return this;
    }

    public Inventory getPage() {
        return page;
    }
}
