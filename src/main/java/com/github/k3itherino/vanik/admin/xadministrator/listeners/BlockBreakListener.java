package com.github.k3itherino.vanik.admin.xadministrator.listeners;

import com.github.k3itherino.vanik.admin.xadministrator.XAdministrator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class BlockBreakListener implements Listener {
    FileConfiguration p;

    HashMap<String, Integer> blocksToTrack = new HashMap<String, Integer>();
    HashMap<String, HashMap> playerBrokeBlocks = new HashMap<String, HashMap>();
    private Object blocks;

    public BlockBreakListener(XAdministrator plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        p = plugin.getConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (playerBrokeBlocks.containsKey(e.getPlayer().getName())) {
            return;
        }

        ArrayList<String> materials = new ArrayList<String>();
        for (Object material : getKeys(p)) {
            materials.add(material.toString());
        }
        for (String material : materials) {
            blocksToTrack.put(material, 0);
        }
        playerBrokeBlocks.put(e.getPlayer().getName(), blocksToTrack);
        Bukkit.getLogger().info(playerBrokeBlocks.toString());
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Bukkit.getLogger().info(playerBrokeBlocks.toString());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        for (Map.Entry<String, HashMap> player : playerBrokeBlocks.entrySet()) {
            Iterator blockList = player.getValue().entrySet().iterator();
            while (blockList.hasNext()) {
                Map.Entry<String, Integer> blockEntry = (Map.Entry<String, Integer>) blockList.next();
                if (e.getBlock().getType() == Material.getMaterial(blockEntry.getKey())) {
                    HashMap prevState = (HashMap) playerBrokeBlocks.get(e.getPlayer().getName());
                    Integer prevNumber = (int) prevState.get(blockEntry.getKey());
                    prevState.put(blockEntry.getKey(), prevNumber + 1);
                    playerBrokeBlocks.put(e.getPlayer().getName(), prevState);
                    if (prevNumber > 0) {
                        Bukkit.getLogger().info(prevNumber.toString() + " " + getValues(p, "blockbreak").getString(blockEntry.getKey()));
                        if (prevNumber %  getValues(p, "blockbreak").getInt(blockEntry.getKey()) == 0) {
                            prevState.put(blockEntry.getKey(), 0);
                            playerBrokeBlocks.put(e.getPlayer().getName(), prevState);
                            for (Player playerToCheckPermission : Bukkit.getOnlinePlayers()) {
                                if (playerToCheckPermission.hasPermission(playerToCheckPermission.getName())) {
                                    String message = p.getString("message").replace("$player$", e.getPlayer().getName()).replace("$type$", blockEntry.getKey()).replace("$number$", getValues(p, "blockbreak").getString(blockEntry.getKey()));
                                    TextComponent messageToAdmin = new TextComponent(message);
                                    messageToAdmin.setBold(true);
                                    messageToAdmin.setColor(!p.getString("color").isEmpty() ? ChatColor.valueOf(p.getString("color")) : ChatColor.WHITE);
                                    messageToAdmin.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            new ComponentBuilder("Clickea para teleportarte al jugador").color(ChatColor.RED).italic(true).create()));

                                    messageToAdmin.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + playerToCheckPermission.getName() + " " + e.getPlayer().getName()));
                                    playerToCheckPermission.spigot().sendMessage(messageToAdmin);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Set getKeys(FileConfiguration p) {
        return p.getConfigurationSection("blockbreak").getKeys(false);
    }

    private ConfigurationSection getValues(FileConfiguration p, String path) {
        return  p.getConfigurationSection(path);
    }
}