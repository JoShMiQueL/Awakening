package me.joshmiquel.awakening.files;

import me.joshmiquel.awakening.Awakening;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ModulesFile {
    private final static Awakening instance = Awakening.getInstance();

    static FileConfiguration fileConfig;
    static File file;
    static String fileName;

    public static void setup(String ymlName) {
        file = new File(instance.getDataFolder(), ymlName);

        if (!file.exists()) {
            instance.saveResource(ymlName, false);
        }

        fileName = ymlName;
        fileConfig = YamlConfiguration.loadConfiguration(file);

        ModulesFile.saveConfig();
        ModulesFile.reloadConfig();
    }

    public static FileConfiguration getConfig() {
        return fileConfig;
    }

    public static void saveConfig() {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + String.format("Could not save %s!", fileName));
        }
    }

    public static void reloadConfig() {
        saveConfig();
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
