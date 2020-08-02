package me.joshmiquel.awakening.modules.TraderNotificationModule;

import me.joshmiquel.awakening.files.ConfigFile;
import me.joshmiquel.awakening.files.ModulesFile;
import me.joshmiquel.awakening.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class TraderNotificationListener implements Listener {
    private final FileConfiguration modulesFile = ModulesFile.getConfig();

    private static Player nearestPlayer(Collection<? extends Player> players, Entity objective) {
        double lastDistance = 999999999999999999999999.999999999999999999999999D;
        Player nearestPlayer = null;
        for (Player player : players) {
            if (player.getLocation().distance(objective.getLocation()) < lastDistance) {
                lastDistance = player.getLocation().distance(objective.getLocation());
                nearestPlayer = player;
            }
        }
        return nearestPlayer;
    }

    @EventHandler
    public void onTraderSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.WANDERING_TRADER && (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DEFAULT)) {
            WanderingTrader trader = (WanderingTrader) event.getEntity();
            List<?> worldsList = modulesFile.getList("modules.trader_notification.allowed_worlds.list");
            if (!modulesFile.getBoolean("modules.trader_notification.allowed_worlds.blacklist")) {
                if (Objects.requireNonNull(worldsList).contains(trader.getWorld().getName())) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        Player nearestPlayer = nearestPlayer(Bukkit.getOnlinePlayers(), trader);
                        if (player == nearestPlayer) {
                            ChatUtils.sendMessage(player, ConfigFile.getConfig().getString("prefix") + "&3Ha aparecido un &eWandering Trader &3cerca tuyo");
                            return;
                        }
                        ChatUtils.sendMessage(player, ConfigFile.getConfig().getString("prefix") + "&3Ha aparecido un &eWandering Trader &3cerca de &e" + nearestPlayer.getName());
                    });
                }
            } else {
                if (!Objects.requireNonNull(worldsList).contains(trader.getWorld().getName())) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        Player nearestPlayer = nearestPlayer(Bukkit.getOnlinePlayers(), trader);
                        if (player == nearestPlayer) {
                            ChatUtils.sendMessage(player, ConfigFile.getConfig().getString("prefix") + "&3Ha aparecido un &eWandering Trader &3cerca tuyo");
                            return;
                        }
                        ChatUtils.sendMessage(player, ConfigFile.getConfig().getString("prefix") + "&3Ha aparecido un &eWandering Trader &3cerca de &e" + nearestPlayer.getName());
                    });
                }
            }
        }
    }
}
