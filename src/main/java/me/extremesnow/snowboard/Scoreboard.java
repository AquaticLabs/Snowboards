package me.extremesnow.snowboard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fr.mrmicky.fastboard.FastBoard;
import me.extremesnow.snowboard.data.PlayerData;
import me.extremesnow.snowboard.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Trevor/extremesnow
 * @since 8/23/2020 at 11:44 AM
 */
public class Scoreboard {

    private Snowboard plugin;

    public Scoreboard(Snowboard plugin) {
        this.plugin = plugin;
    }


    public void updateScoreboard(Player player) {
        FastBoard scoreboard;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (playerData == null) return;
        if (playerData.getScoreboard()== null) {
            scoreboard = new FastBoard(player);
        } else scoreboard = playerData.getScoreboard();

        List<String> configLayout = getScoreboardForPlayer(player);

        List<String> layout = new ArrayList<>();
        for (String s : configLayout) {
            String replaced = Utilities.replacePlaceholders(plugin, playerData, s);
            layout.add(replaced);
        }

        scoreboard.updateTitle(Utilities.replacePlaceholders(plugin, playerData, getScoreboardTitleForPlayer(player)));
        scoreboard.updateLines(layout);
        playerData.setScoreboard(scoreboard);
    }

    public void addToSBLoop(PlayerData playerData, boolean delay) {
        if (playerData == null) return;
        plugin.debugLog("Given Scoreboard to: " + playerData.getName());

        if (!playerData.isScoreboardEnabled()) return;

        if (playerData.getScoreboardTask() != null) {
            playerData.getScoreboardTask().cancel();
        }
        Player player = Bukkit.getPlayer(playerData.getUuid());

        if (player == null) return;
        int intDelay = 0;
        if (delay) intDelay = 20;

        BukkitTask taskID = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> updateScoreboard(player), intDelay, 20);
        playerData.setScoreboardTask(taskID);
    }

    public void removeFromSBLoop(PlayerData playerData) {
        if (playerData == null) return;
        plugin.debugLog("Removed " + playerData.getName() + "'s Scoreboard.");
        Player player = Bukkit.getPlayer(playerData.getUuid());
        if (player == null) return;

        if (playerData.getScoreboardTask() != null) {
            playerData.getScoreboardTask().cancel();
            playerData.setScoreboardTask(null);
        }
        if (player.isOnline()) {
            if (playerData.getScoreboard() != null) {
                playerData.getScoreboard().delete();
                playerData.setScoreboard(null);
                return;
            }
            FastBoard scoreboard = new FastBoard(player);
            scoreboard.delete();
            playerData.setScoreboard(null);
        }
    }

    private List<String> getScoreboardForPlayer(Player player) {
        String worldName = player.getWorld().getName();
        String defaultBoard = plugin.getFileUtil().getConfigFile().getDefaultScoreboard();

        ConfigurationSection scoreboardSection = plugin.getFileUtil().getScoreboardFile().getScoreboardSection();
        ConfigurationSection regionSection = plugin.getFileUtil().getScoreboardFile().getRegionSection();

        if (plugin.isWorldGuard7()) {
            Location loc = BukkitAdapter.adapt(player.getLocation());
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(loc);
            for (ProtectedRegion region : set) {
                if (regionSection.getKeys(false).contains(region.getId())) {
                    return regionSection.getStringList(region.getId() + ".layout");
                }
            }
        }

        if (scoreboardSection.getKeys(false).contains(worldName.toLowerCase())) {
            return scoreboardSection.getStringList(worldName.toLowerCase() + ".layout");
        }

        if (scoreboardSection.get(defaultBoard + ".layout") == null && scoreboardSection.get("default.layout") == null) {
            return getErrorScoreboard();
        }

        if (scoreboardSection.get(defaultBoard + ".title") != null) {
            return scoreboardSection.getStringList(defaultBoard + ".layout");
        } else {
            return scoreboardSection.getStringList("default.layout");
        }
    }


    private String getScoreboardTitleForPlayer(Player player) {
        String defaultBoard = plugin.getFileUtil().getConfigFile().getDefaultScoreboard();
        String worldName = player.getWorld().getName();
        ConfigurationSection configurationSection = plugin.getFileUtil().getScoreboardFile().getScoreboardSection();
        ConfigurationSection regionSection = plugin.getFileUtil().getScoreboardFile().getRegionSection();

        if (plugin.isWorldGuard7()) {
            Location loc = BukkitAdapter.adapt(player.getLocation());
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(loc);
            for (ProtectedRegion region : set) {
                if (regionSection.getKeys(false).contains(region.getId())) {
                    return regionSection.getString(region.getId() + ".title");
                }
            }
        }

        if (configurationSection.getKeys(false).contains(worldName.toLowerCase())) {
            return configurationSection.getString(worldName.toLowerCase() + ".title");
        }
        if (configurationSection.get(defaultBoard + ".title") == null && configurationSection.get("default.title") == null) {
            return "&cERROR";
        }
        if (configurationSection.get(defaultBoard + ".title") != null) {
            return configurationSection.getString(defaultBoard + ".title");
        } else {
            return configurationSection.getString("default.title");
        }
    }
    private List<String> getErrorScoreboard() {
        List<String> list = new ArrayList<>();
        list.add("&cInvalid scoreboard layout!");
        list.add("&cYou can fix this by adding a default");
        list.add("&cboard in scoreboards.yml");
        return list;
    }
}
