package me.extremesnow.snowboard.utils;

import lombok.Getter;
import me.extremesnow.snowboard.Snowboard;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @author Trevor/extremesnow
 * @since 10/10/2020 at 2:36 PM
 */
@Getter
public class ScoreboardFile {

    private Snowboard plugin;
    File scoreboardFile;
    FileConfiguration scoreboard;

    ConfigurationSection scoreboardSection;
    ConfigurationSection regionSection;

    public ScoreboardFile(Snowboard plugin, File file, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.scoreboardFile = file;
        this.scoreboard = fileConfiguration;

        scoreboardSection = scoreboard.getConfigurationSection("Scoreboards");
        regionSection = scoreboard.getConfigurationSection("RegionBoards");
    }
}
