package me.extremesnow.snowboard;

import lombok.Getter;
import lombok.Setter;
import me.extremesnow.snowboard.commands.SnowboardCommand;
import me.extremesnow.snowboard.commands.TSBCommand;
import me.extremesnow.snowboard.data.PlayerData;
import me.extremesnow.snowboard.data.PlayerHolder;
import me.extremesnow.snowboard.listeners.Events;
import me.extremesnow.snowboard.utils.FileUtil;
import me.extremesnow.snowboard.utils.Utilities;
import me.extremesnow.snowboard.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class Snowboard extends JavaPlugin {

    public static boolean SHOULD_BE_LEGACY = false;
    @Getter @Setter
    private FileUtil fileUtil;
    @Getter @Setter
    private PlayerHolder playerHolder;
    @Getter
    private boolean debug = false;
    @Getter
    private boolean worldGuard7 = false;

    private Scoreboard scoreboard;


    @Override
    public void onEnable() {
        reloadFileUtilConfigs();
        this.scoreboard = new Scoreboard(this);
        setPlayerHolder(new PlayerHolder(this));
        getServer().getPluginManager().registerEvents(new Events(this), this);
        getCommand("snowboard").setExecutor(new SnowboardCommand(this));
        getCommand("togglescoreboard").setExecutor(new TSBCommand(this));
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            onReload();
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getConsoleSender().sendMessage("§9[§bSnowboard§9] §bPlaceholderAPI Detected! §aEnabling support!");
        } else Bukkit.getConsoleSender().sendMessage("§9[§bSnowboard§9] §cPlaceholderAPI not detected!");

        if (getServer().getPluginManager().getPlugin("ViaBackwards") != null) {
            Bukkit.getConsoleSender().sendMessage("§9[§bSnowboard§9] §bViaBackwards Detected. Disabling 1.13+ scoreboard lengths");
            SHOULD_BE_LEGACY = true;
        }

        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            Version version = new Version(Utilities.getVersion());
            if (version.compareTo(new Version("1.13")) > 0) {
                worldGuard7 = true;
                Bukkit.getConsoleSender().sendMessage("§9[§bSnowboard§9] §bWorldGuard Detected! §aEnabling Region Capability.");
            } else {
                Bukkit.getConsoleSender().sendMessage("§9[§bSnowboard§9] §bOld WorldGuard Version Detected.. §cDisabling Region Support.");
            }
        }

        debugLog("§3NOTICE THIS IS A DEBUG VERSION OF THE PLUGIN.");
        debugLog("§3VERSION: §c" + getDescription().getVersion());
        debugLog("§3SERVER VERSION: §c" + getServer().getVersion());

    }


    private void onReload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = getPlayerHolder().getOrInsert(player.getUniqueId());
            playerData.setName(player.getName());

            if (!fileUtil.getConfigFile().isScoreboardEnabled()) {
                playerData.setScoreboardEnabled(false);
                scoreboard.removeFromSBLoop(playerData);
            } else {
                if (fileUtil.getConfigFile().isDisableCertainWorlds()) {
                    if (!Utilities.containsString(player.getWorld().getName(), fileUtil.getConfigFile().getEnabledWorlds())) {
                        playerData.setScoreboardEnabled(false);
                        scoreboard.removeFromSBLoop(playerData);
                        continue;
                    }
                }
                playerData.setScoreboardEnabled(true);
                scoreboard.addToSBLoop(playerData, false);
            }
        }
    }

    public void reloadFileUtilConfigs() {
        if (fileUtil == null) {
            fileUtil = new FileUtil(this);
        }
        fileUtil.setupConfig("config");
        fileUtil.setupConfig("scoreboards");
        debugLog("FileUtil reloaded.");
    }

    public void debugLog(String string) {
        if (debug) {
            getServer().getConsoleSender().sendMessage("§c[Snowboard Debug] §7" + string.replace("&", "§"));
        }
    }

    public void debugLog(List<String> stringList) {
        if (debug) {
            for (String s : stringList) {
                getServer().getConsoleSender().sendMessage("§c[Snowboard Debug] §7" + s.replace("&", "§"));
            }
        }
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerHolder.getOrNull(uuid);
    }
}
