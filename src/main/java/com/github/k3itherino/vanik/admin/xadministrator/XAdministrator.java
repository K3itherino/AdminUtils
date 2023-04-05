package com.github.k3itherino.vanik.admin.xadministrator;

import com.github.k3itherino.vanik.admin.xadministrator.listeners.BlockBreakListener;
import com.github.k3itherino.vanik.admin.xadministrator.commands.AdminUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;

public final class XAdministrator extends JavaPlugin {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // Plugin startup logic
        new BlockBreakListener(this);
        this.getDataFolder().mkdir();
        getCommand("administrador").setExecutor(new AdminUtils(this));
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
