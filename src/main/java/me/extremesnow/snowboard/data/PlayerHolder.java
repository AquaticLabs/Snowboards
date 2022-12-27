package me.extremesnow.snowboard.data;

import me.extremesnow.snowboard.Snowboard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

/**
 * @author Trevor/me.extremesnow
 * @since 3/29/2020 at 11:23 AM
 */

public class PlayerHolder {

    private Snowboard plugin;
    private Map<UUID, PlayerData> dataMap = new HashMap<>();

    public PlayerHolder(Snowboard plugin) {
        this.plugin = plugin;
    }

    public void onAdd(PlayerData playerData) {
        dataMap.put(playerData.getUuid(), playerData);
    }
    public void load() {
        for (OfflinePlayer offlinePlayer : Bukkit.getServer().getOfflinePlayers()) {
            getOrInsert(offlinePlayer.getUniqueId());
        }
    }
    public void onRemove(PlayerData playerData) {
        dataMap.remove(playerData.getUuid());
    }
    public PlayerData getOrInsert(UUID uuid) {
        PlayerData playerData = dataMap.get(uuid);
        if (playerData == null) {
            playerData = new PlayerData(plugin, uuid);
            onAdd(playerData);
        }
        return playerData;
    }
    public PlayerData getOrNull(UUID uuid) {
        return dataMap.get(uuid);
    }

    public Set<PlayerData> getAllPlayerData() {
        return new HashSet<>(dataMap.values());
    }

    public Set<UUID> getAllUUIDs() {
        return new HashSet<>(dataMap.keySet());
    }


}

