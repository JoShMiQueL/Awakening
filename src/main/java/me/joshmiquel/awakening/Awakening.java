package me.joshmiquel.awakening;

import me.joshmiquel.awakening.commands.ModulesCommand;
import me.joshmiquel.awakening.files.ConfigFile;
import me.joshmiquel.awakening.files.ModulesFile;
import me.joshmiquel.awakening.modules.ModulesManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Awakening extends JavaPlugin {

    private static Awakening instance;

    public static Awakening getInstance() {
        return instance;
    }

    private void setInstance(Awakening instance) {
        Awakening.instance = instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        setInstance(this);
        ModulesFile.setup("modules.yml");
        ConfigFile.setup("config.yml");
        ModulesManager.loadModules();
        Objects.requireNonNull(Bukkit.getPluginCommand("modules")).setExecutor(new ModulesCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("modules")).setTabCompleter(new ModulesCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
