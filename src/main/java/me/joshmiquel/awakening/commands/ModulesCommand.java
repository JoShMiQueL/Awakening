package me.joshmiquel.awakening.commands;

import me.joshmiquel.awakening.Awakening;
import me.joshmiquel.awakening.files.ConfigFile;
import me.joshmiquel.awakening.files.ModulesFile;
import me.joshmiquel.awakening.modules.ModulesManager;
import me.joshmiquel.awakening.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ModulesCommand implements CommandExecutor, TabCompleter {
    private final Awakening instance = Awakening.getInstance();
    private final String prefix = ConfigFile.getConfig().getString("prefix");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            ChatUtils.sendMessage(sender, "&3Awakening Modules Help");
            ChatUtils.sendMessage(sender, "  &7- &e/modules list &7Shows the list of modules");
            ChatUtils.sendMessage(sender, "  &7- &e/modules enable &7Enable a module");
            ChatUtils.sendMessage(sender, "  &7- &e/modules disable &7Disable a module");
            ChatUtils.sendMessage(sender, "  &7- &e/modules handler &7Debug info");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("enableandlistdebug")) {
            for (String moduleKey : Objects.requireNonNull(ModulesFile.getConfig().getConfigurationSection("modules")).getKeys(false)) {
                boolean enabled = ModulesFile.getConfig().getBoolean("modules." + moduleKey + ".enabled");
                if (!enabled) {
                    Bukkit.dispatchCommand(sender, "modules enable " + ChatUtils.replaceUnderscoreAndCapitalize(moduleKey));
                    Bukkit.dispatchCommand(sender, "modules list");
                }
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("disableandlistdebug")) {
            for (String moduleKey : Objects.requireNonNull(ModulesFile.getConfig().getConfigurationSection("modules")).getKeys(false)) {
                boolean enabled = ModulesFile.getConfig().getBoolean("modules." + moduleKey + ".enabled");
                if (enabled) {
                    Bukkit.dispatchCommand(sender, "modules disable " + ChatUtils.replaceUnderscoreAndCapitalize(moduleKey));
                    Bukkit.dispatchCommand(sender, "modules list");
                }
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (sender instanceof Player) {
                for (int i = 0; i < 30; i++) {
                    ChatUtils.sendMessage((Player) sender, "");
                }
                ChatUtils.sendMessage((Player) sender, "&3Awakening Modules List");
                ChatUtils.sendMessage((Player) sender, "&7(Hover toggle state)");
                for (String moduleKey : Objects.requireNonNull(ModulesFile.getConfig().getConfigurationSection("modules")).getKeys(false)) {
                    boolean enabled = ModulesFile.getConfig().getBoolean("modules." + moduleKey + ".enabled");
                    TextComponent text = new TextComponent("  - ");
                    text.setColor(ChatColor.GRAY);
                    TextComponent moduleName = new TextComponent(ChatUtils.replaceUnderscoreAndCapitalize(moduleKey));
                    if (enabled) {
                        moduleName.setColor(ChatColor.GREEN);
                        text.addExtra(moduleName);
                        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cDisable a module")));
                        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/modules disableandlistdebug"));
                    } else {
                        moduleName.setColor(ChatColor.RED);
                        text.addExtra(moduleName);
                        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aEnable a module")));
                        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/modules enableandlistdebug"));
                    }
                    ChatUtils.sendMessage((Player) sender, text);
                }
            } else {
                ChatUtils.sendMessage(sender, "&3Awakening Modules List");
                ChatUtils.sendMessage(sender, "");
                for (String moduleKey : Objects.requireNonNull(ModulesFile.getConfig().getConfigurationSection("modules")).getKeys(false)) {
                    boolean enabled = ModulesFile.getConfig().getBoolean("modules." + moduleKey + ".enabled");
                    if (enabled) {
                        ChatUtils.sendMessage(sender, "  &7- &a" + ChatUtils.replaceUnderscoreAndCapitalize(moduleKey));
                    } else {
                        ChatUtils.sendMessage(sender, "  &7- &c" + ChatUtils.replaceUnderscoreAndCapitalize(moduleKey) + " &7(disabled)");
                    }
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("enable")) {
            for (String moduleKey : Objects.requireNonNull(ModulesFile.getConfig().getConfigurationSection("modules")).getKeys(false)) {
                if (args[1].equalsIgnoreCase(ChatUtils.replaceUnderscoreAndCapitalize(moduleKey))) {
                    ChatUtils.sendMessage((Player) sender, prefix + "&c[DEBUG] &e" + ChatUtils.replaceUnderscoreAndCapitalize(moduleKey) + " &aenabled");
                    ModulesFile.getConfig().set("modules.trader_notification.enabled", true);
                    ModulesFile.reloadConfig();
                    ModulesManager.reloadModules();
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("disable")) {
            for (String moduleKey : Objects.requireNonNull(ModulesFile.getConfig().getConfigurationSection("modules")).getKeys(false)) {
                if (args[1].equalsIgnoreCase(ChatUtils.replaceUnderscoreAndCapitalize(moduleKey))) {
                    ChatUtils.sendMessage((Player) sender, prefix + "&c[DEBUG] &e" + ChatUtils.replaceUnderscoreAndCapitalize(moduleKey) + " &cdisabled");
                    ModulesFile.getConfig().set("modules.trader_notification.enabled", false);
                    ModulesFile.reloadConfig();
                    ModulesManager.reloadModules();
                }
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("handler")) {
            ChatUtils.sendMessage((Player) sender, String.valueOf(HandlerList.getRegisteredListeners(instance)));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            ChatUtils.sendMessage(sender, prefix + "&3Modules successfully reloaded");
            ModulesFile.reloadConfig();
            ModulesManager.reloadModules();
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("list", "enable", "disable", "handler");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("enable")) {
                List<String> modules = new ArrayList<>();
                for (String moduleKey : Objects.requireNonNull(ModulesFile.getConfig().getConfigurationSection("modules")).getKeys(false)) {
                    if (!ModulesFile.getConfig().getBoolean("modules." + moduleKey + ".enabled")) {
                        modules.add(ChatUtils.replaceUnderscoreAndCapitalize(moduleKey));
                    }
                }
                return modules;
            } else if (args[0].equalsIgnoreCase("disable")) {
                List<String> modules = new ArrayList<>();
                for (String moduleKey : Objects.requireNonNull(ModulesFile.getConfig().getConfigurationSection("modules")).getKeys(false)) {
                    if (ModulesFile.getConfig().getBoolean("modules." + moduleKey + ".enabled")) {
                        modules.add(ChatUtils.replaceUnderscoreAndCapitalize(moduleKey));
                    }
                }
                return modules;
            }
        }
        return null;
    }
}
