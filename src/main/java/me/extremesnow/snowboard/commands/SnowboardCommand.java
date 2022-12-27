package me.extremesnow.snowboard.commands;

import me.extremesnow.snowboard.Scoreboard;
import me.extremesnow.snowboard.Snowboard;
import me.extremesnow.snowboard.data.PlayerData;
import me.extremesnow.snowboard.utils.ConfigFile;
import me.extremesnow.snowboard.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * @author Trevor/extremesnow
 * @since 8/23/2020 at 12:26 PM
 */
public class SnowboardCommand implements CommandExecutor {

    private Snowboard plugin;

    public SnowboardCommand(Snowboard plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Snowboard" + ChatColor.DARK_AQUA + "] " + ChatColor.RED + "Cannot be run from console.");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("snowboard")) {
            Player player = (Player) sender;
            ConfigFile configFile = plugin.getFileUtil().getConfigFile();

            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                if (player.hasPermission("snowboard.admin")) {
                    player.sendMessage(ChatColor.DARK_GRAY + "----------" + ChatColor.DARK_AQUA + " Snow Scoreboard Commands" + ChatColor.DARK_GRAY + " ----------");
                    player.sendMessage(ChatColor.DARK_GRAY + "-  ");
                    player.sendMessage(ChatColor.DARK_GRAY + "-  " + ChatColor.AQUA + "/togglescoreboard");
                    player.sendMessage(ChatColor.DARK_GRAY + "-  " + ChatColor.RED + "/snowboard reload");
                    player.sendMessage(ChatColor.DARK_GRAY + "-  ");
                    player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------");
                    return true;
                }
                player.sendMessage(ChatColor.DARK_GRAY + "----------" + ChatColor.DARK_AQUA + " Snow Scoreboard Commands" + ChatColor.DARK_GRAY + " ----------");
                player.sendMessage(ChatColor.DARK_GRAY + "-  ");
                player.sendMessage(ChatColor.DARK_GRAY + "-  " + ChatColor.AQUA + "/togglescoreboard");
                player.sendMessage(ChatColor.DARK_GRAY + "-  ");
                player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("snowboard.admin")) {
                    plugin.reloadFileUtilConfigs();
                    configFile = plugin.getFileUtil().getConfigFile();
                    Scoreboard scoreboard = plugin.getScoreboard();

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        PlayerData playerData = plugin.getPlayerHolder().getOrNull(p.getUniqueId());
                        if (!configFile.isScoreboardEnabled()) {
                            playerData.setScoreboardEnabled(false);
                            scoreboard.removeFromSBLoop(playerData);
                        } else {
                            if (configFile.isDisableCertainWorlds()) {
                                if (!Utilities.containsString(player.getWorld().getName(), configFile.getEnabledWorlds())) {
                                    playerData.setScoreboardEnabled(false);
                                    scoreboard.removeFromSBLoop(playerData);
                                    continue;
                                }
                            }
                            playerData.setScoreboardEnabled(true);
                            scoreboard.addToSBLoop(playerData, false);
                        }
                    }
                    player.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Snowboard" + ChatColor.DARK_AQUA + "] " + ChatColor.GRAY + "Files successfully reloaded!");
                    return true;
                }
                player.sendMessage(configFile.getNoPermission());
                return true;
            }
        }
        return false;
    }


}
