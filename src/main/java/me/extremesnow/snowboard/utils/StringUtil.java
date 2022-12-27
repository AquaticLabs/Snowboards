package me.extremesnow.snowboard.utils;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.extremesnow.snowboard.Snowboard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: extremesnow
 * On: 5/19/2022
 * At: 01:14
 */

public class StringUtil {

    private Snowboard plugin;
    @Getter
    private Map<String, String> replaceMap = new HashMap<>();

    public StringUtil(Snowboard plugin) {
        this.plugin = plugin;
        //addReplacement("%prefix%", plugin.getFileUtil().getConfigFile().getPrefix());
    }

    public StringUtil() {
        //addReplacement("%prefix%", plugin.getFileUtil().getMessageFile().getPrefix());
    }


    public void send(CommandSender sender, String message) {
        if (sender == null) return;
        validate();
        if (message.equalsIgnoreCase("")) return;
        message = Utilities.tryReplaceHexCodes(message);

        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        if (sender instanceof LivingEntity && plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            LivingEntity ent = (LivingEntity) sender;
            message = PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(ent.getUniqueId()), message);
        }


        List<String> messages = new ArrayList<>(Arrays.asList(message.split("\\n")));
        for (String m : messages) {
            sender.sendMessage(Utilities.tryReplaceHexCodes(m));
        }
    }

    public void send(CommandSender sender, List<String> messageList) {
        if (sender == null) return;
        validate();

        for (String message : messageList) {
            message = Utilities.tryReplaceHexCodes(message);

            for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
            if (sender instanceof LivingEntity && plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                LivingEntity ent = (LivingEntity) sender;
                message = PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(ent.getUniqueId()), message);
            }
            List<String> messages = new ArrayList<>(Arrays.asList(message.split("\\n")));
            for (String m : messages) {
                sender.sendMessage(Utilities.tryReplaceHexCodes(m));
            }
        }
    }

    public void send(Player player, String message) {
        if (!player.isOnline()) return;
        if (message.equalsIgnoreCase("")) return;
        validate();
        message = Utilities.tryReplaceHexCodes(message);

        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        List<String> messages = new ArrayList<>(Arrays.asList(message.split("\\n")));
        for (String m : messages) {
            player.sendMessage(Utilities.tryReplaceHexCodes(m));
        }
    }

    public void send(Player player, List<String> messageList) {
        if (!player.isOnline()) return;
        validate();
        for (String message : messageList) {
            message = Utilities.tryReplaceHexCodes(message);

            for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
            if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }
            List<String> messages = new ArrayList<>(Arrays.asList(message.split("\\n")));
            for (String m : messages) {
                player.sendMessage(Utilities.tryReplaceHexCodes(m));
            }
        }
    }

    public void validate() {

        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            if (entry.getValue() == null) replaceMap.remove(entry.getKey());
        }
    }

    public String getReplaced(Player player, String message) {

        if (message.equalsIgnoreCase("")) return "";
        validate();
        message = Utilities.tryReplaceHexCodes(message);

        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        if (player != null && plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }


        return message;

    }

    public void addReplacement(String oldString, String newString) {
        if (newString == null || oldString == null) {
            //DebugLogger.logDebugMessage("Failed to add Replacement, One of the replacements was null");
            return;
        }
        replaceMap.put(oldString, newString);
    }

    public void addDefaultCommandReplacements(CommandSender sender, Command cmd, String[] args) {
        addReplacement("%cmd%", cmd.getName());
        addReplacement("%sender%", sender.getName());
        for (int i = 0; i < args.length; i++) {
            addReplacement("%args[" + i + "]%", args[i]);
        }
    }

    public void addDefaultCommandReplacements(CommandSender sender, Command cmd) {
        addReplacement("%cmd%", cmd.getName());
        addReplacement("%sender%", sender.getName());
    }

    public String replace(String replace) {
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            replace = replace.replace(entry.getKey(), entry.getValue());
        }
        return replace;
    }

    public StringUtil merge(StringUtil messageUtil) {
        for (Map.Entry<String, String> entry : messageUtil.getReplaceMap().entrySet()) {
            replaceMap.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

}
