package me.extremesnow.snowboard.utils;

import lombok.Getter;
import lombok.Setter;
import me.extremesnow.snowboard.Snowboard;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Snowball;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Trevor/extremesnow
 * @since 8/23/2020 at 11:54 AM
 */
@Getter @Setter
public class ConfigFile {

    private Snowboard plugin;
    File configFile;
    FileConfiguration config;

    private boolean disableCertainWorlds;
    private List<String> enabledWorlds;

    // Scoreboard
    private boolean scoreboardEnabled;
    private boolean scoreboardToggledByDefault;
    private String dateTimeZone;
    private String defaultScoreboard;
    private String dateFormat;
    // private String scoreboardTitle;
    // private List<String> scoreboardLayout;

    // Messages
    private String noPermission;
    private String scoreboardDisabled;
    private String scoreboardToggleOn;
    private String scoreboardToggleOff;




    public ConfigFile(Snowboard plugin, File file, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.configFile = file;
        this.config = fileConfiguration;



        disableCertainWorlds = config.getBoolean("MainOptions.disableCertainWorldStats");
        enabledWorlds = config.getStringList("MainOptions.enabledWorlds");

        // Scoreboard
        scoreboardEnabled = config.getBoolean("Scoreboard.enabled");
        scoreboardToggledByDefault = config.getBoolean("Scoreboard.toggledByDefault");
        dateTimeZone = config.getString("Scoreboard.dateTimeZone");
        dateFormat = config.getString("Scoreboard.dateFormat");
        defaultScoreboard = config.getString("Scoreboard.defaultScoreboard");

        // Messages
        noPermission = replace(config.getString("Messages.noPermission"));
        scoreboardDisabled = replace(config.getString("Messages.scoreboardDisabled"));
        scoreboardToggleOn = replace(config.getString("Messages.toggleSBMessageOn"));
        scoreboardToggleOff = replace(config.getString("Messages.toggleSBMessageOff"));

    }




    // Main options

    private List<String> replace(List<String> list) {
        List<String> replaced = new ArrayList<>();
        for (String s : list) {
            s = s.replace("%blank-1%", ChatColor.RESET.toString() + "")
                    .replace("%blank-2%", ChatColor.RESET.toString() + "" + ChatColor.RESET.toString())
                    .replace("%blank-3%", ChatColor.RESET.toString() + " ")
                    .replace("%blank-4%", ChatColor.RESET.toString() + "  ")
                    .replace("%blank-5%", ChatColor.RESET.toString() + "   ")
                    .replace("%blank-6%", ChatColor.RESET.toString() + "    ");
            replaced.add(Utilities.tryReplaceHexCodes(s));
        }
        return replaced;
    }

    private String replace(String s) {
        return Utilities.tryReplaceHexCodes(s);
    }
}
