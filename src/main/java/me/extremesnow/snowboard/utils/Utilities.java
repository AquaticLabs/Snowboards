package me.extremesnow.snowboard.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.extremesnow.snowboard.Snowboard;
import me.extremesnow.snowboard.data.PlayerData;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Trevor/extremesnow
 * @since 8/23/2020 at 12:03 PM
 */
public class Utilities {

    private static final Pattern HEX_REGEX = Pattern.compile("#[0-9A-Fa-f]{6}|#[0-9A-Fa-f]{3}");

    public static String tryReplaceHexCodes(String input) {
        Version version = new Version(getVersion());
        if (version.compareTo(new Version("1.16.0")) > 0) {
            final Matcher matcher = HEX_REGEX.matcher(input);
            final StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of(matcher.group()).toString());
            }
            return matcher.appendTail(buffer).toString().replace("&", "§");
        }
        return input.replace("&", "§");
    }

    public static String getVersion() {
        String bukkitver = Bukkit.getServer().getVersion();
        String mcver = "1.0.0";
        int idx = bukkitver.indexOf("(MC: ");
        if (idx > 0) {
            mcver = bukkitver.substring(idx + 5);
            idx = mcver.indexOf(')');
            if (idx > 0) mcver = mcver.substring(0, idx);
        }
        return mcver;
    }


    public static String replacePlaceholders(Snowboard plugin, PlayerData playerData, String string) {

        ConfigFile configFile = plugin.getFileUtil().getConfigFile();

        final String name = playerData.getName();

        Date now = new Date();
        SimpleDateFormat date;
        try {
            date = new SimpleDateFormat(configFile.getDateFormat());
        } catch (NullPointerException e) {
            date = new SimpleDateFormat("MM/dd/yyyy");
        }

        date.setTimeZone(TimeZone.getTimeZone(configFile.getDateTimeZone()));
        string = string
                .replace("%user%", name)
                .replace("%date%", date.format(now))
                .replace("%blank-1%", "&r")
                .replace("%blank-2%", "&r&r")
                .replace("%blank-3%", "&r &r")
                .replace("%blank-4%", "&r  &r")
                .replace("%blank-5%", "&r   &r")
                .replace("%blank-6%", "&r    &r")
                .replace("%sun%", "☀")
                .replace("%cloud%", "☁")
                .replace("%snowman%", "☃")
                .replace("%star%", "★")
                .replace("%white_star%", "☆")
        ;

        string = decode(string);
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            string = PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(playerData.getUuid()), string);
        }
        return tryReplaceHexCodes(string);
    }

    public static boolean containsString(String string, List<String> list) {
        String worldList = list.toString().toLowerCase();
        return worldList.contains("[" + string.toLowerCase() + ",") ||
                worldList.contains(" " + string.toLowerCase() + ",") ||
                worldList.contains(" " + string.toLowerCase() + "]") ||
                worldList.contains("[" + string.toLowerCase() + "]");
    }

    public static final String decode(final String in) {
        String working = in;
        int index;
        index = working.indexOf("\\u");
        while(index > -1)
        {
            int length = working.length();
            if(index > (length-6))break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring,16);
            String stringStart = working.substring(0, index);
            String stringEnd   = working.substring(numFinish);
            working = stringStart + ((char)number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }
}
