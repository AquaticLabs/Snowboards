package me.extremesnow.snowboard.commands;

import me.extremesnow.snowboard.Scoreboard;
import me.extremesnow.snowboard.Snowboard;
import me.extremesnow.snowboard.data.PlayerData;
import me.extremesnow.snowboard.utils.ConfigFile;
import me.extremesnow.snowboard.utils.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * @author Trevor/me.extremesnow
 * @since 8/26/2019 at 12:07 PM
 */
public class TSBCommand implements CommandExecutor {

    private Snowboard plugin;

    public TSBCommand(Snowboard plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("toggleScoreboard")) {
                Player player = (Player) sender;
                ConfigFile configFile = plugin.getFileUtil().getConfigFile();
                if (!player.hasPermission("snowboard.togglescoreboard")) {
                    player.sendMessage(configFile.getNoPermission());
                    return true;
                }
                if (!configFile.isScoreboardEnabled()) {
                    player.sendMessage(configFile.getScoreboardDisabled());
                    return true;
                }
                PlayerData playerData = plugin.getPlayerHolder().getOrInsert(player.getUniqueId());
                Scoreboard scoreboard = plugin.getScoreboard();
                if (configFile.isDisableCertainWorlds()) {
                    if (Utilities.containsString(player.getWorld().getName(), configFile.getEnabledWorlds())) {
                        if (!playerData.isScoreboardEnabled()) {
                            playerData.setScoreboardEnabled(true);
                            scoreboard.addToSBLoop(playerData, false);
                            player.sendMessage(configFile.getScoreboardToggleOn());
                            return true;
                        }
                        playerData.setScoreboardEnabled(false);
                        scoreboard.removeFromSBLoop(playerData);
                        player.sendMessage(configFile.getScoreboardToggleOff());
                        return true;
                    }

                    player.sendMessage(configFile.getScoreboardDisabled());
                    return true;
                }

                if (!playerData.isScoreboardEnabled()) {
                    playerData.setScoreboardEnabled(true);
                    scoreboard.addToSBLoop(playerData, false);
                    player.sendMessage(configFile.getScoreboardToggleOn());
                    return true;
                }
                playerData.setScoreboardEnabled(false);
                scoreboard.removeFromSBLoop(playerData);
                player.sendMessage(configFile.getScoreboardToggleOff());
                return true;
            }
        }
        return false;
    }
}
