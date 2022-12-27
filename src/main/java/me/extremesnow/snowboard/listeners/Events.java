package me.extremesnow.snowboard.listeners;

import me.extremesnow.snowboard.Scoreboard;
import me.extremesnow.snowboard.Snowboard;
import me.extremesnow.snowboard.data.PlayerData;
import me.extremesnow.snowboard.utils.ConfigFile;
import me.extremesnow.snowboard.utils.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Trevor/extremesnow
 * @since 8/23/2020 at 12:15 PM
 */
public class Events implements Listener {

    private Snowboard plugin;

    public Events(Snowboard plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = plugin.getPlayerHolder().getOrInsert(player.getUniqueId());
        playerData.setName(player.getName());
        ConfigFile configFile = plugin.getFileUtil().getConfigFile();
        plugin.debugLog(player.getName() + " Login.");

        if (!configFile.isScoreboardEnabled()) return;
        if (configFile.isDisableCertainWorlds()) {
            if (!Utilities.containsString(player.getWorld().getName(), configFile.getEnabledWorlds())) {
                playerData.setScoreboardEnabled(false);
            }
        }
        Scoreboard scoreboard = plugin.getScoreboard();
        scoreboard.addToSBLoop(playerData, true);

    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        PlayerData playerData = plugin.getPlayerHolder().getOrNull(playerQuitEvent.getPlayer().getUniqueId());
        if (playerData == null) return;
        plugin.debugLog(playerData.getName() + " Quit.");

        Scoreboard scoreboard = plugin.getScoreboard();
        scoreboard.removeFromSBLoop(playerData);
    }

    @EventHandler
    public void onWorldChangeEvent(PlayerChangedWorldEvent playerChangedWorldEvent) {
        Player player = playerChangedWorldEvent.getPlayer();
        ConfigFile configFile = plugin.getFileUtil().getConfigFile();
        plugin.debugLog(player.getName() + " Changed Worlds.  Old World: " + playerChangedWorldEvent.getFrom().getName() + " New World: " + player.getWorld().getName());

        if (!configFile.isDisableCertainWorlds()) return;

        PlayerData playerData = plugin.getPlayerHolder().getOrNull(player.getUniqueId());
        Scoreboard scoreboard = plugin.getScoreboard();

        if (!configFile.isScoreboardEnabled()) {
            plugin.debugLog("EV: 67");
            scoreboard.removeFromSBLoop(playerData);
            return;
        }
        if (playerData == null) {
            return;
        }

        if (!Utilities.containsString(player.getWorld().getName(), configFile.getEnabledWorlds())) {
            plugin.debugLog("EV: 76");
            playerData.setScoreboardEnabled(false);
            scoreboard.removeFromSBLoop(playerData);
            return;
        }
        if (playerData.getScoreboardTask() == null) {
            plugin.debugLog("EV: 82");
            playerData.setScoreboardEnabled(true);
            scoreboard.addToSBLoop(playerData, true);
        }
    }

}
