package me.joshmiquel.awakening.modules;

import me.joshmiquel.awakening.Awakening;
import me.joshmiquel.awakening.files.ModulesFile;
import me.joshmiquel.awakening.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Objects;

public class ModulesManager {
    private final static Awakening instance = Awakening.getInstance();

    public static void loadModule(String module) {
        Class<?> classInstance;
        try {
            String modulePackage = ChatUtils.replaceUnderscoreAndCapitalize(module) + "Module";
            String moduleListener = ChatUtils.replaceUnderscoreAndCapitalize(module) + "Listener";
            classInstance = Class.forName("me.joshmiquel.awakening.modules." + modulePackage + "." + moduleListener);
            Bukkit.getServer().getPluginManager().registerEvents((Listener) classInstance.newInstance(), instance);
            System.out.println("[DEBUG] - " + ChatUtils.replaceUnderscoreAndCapitalize(module) + " loaded");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void loadModules() {
        for (String moduleKey : Objects.requireNonNull(ModulesFile.getConfig().getConfigurationSection("modules")).getKeys(false)) {
            if (ModulesFile.getConfig().getBoolean("modules." + moduleKey + ".enabled")) {
                loadModule(moduleKey);
            }
        }
    }

    public static void reloadModules() {
        HandlerList.unregisterAll();
        loadModules();
    }
}
