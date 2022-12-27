package me.extremesnow.snowboard.data;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import me.extremesnow.snowboard.Snowboard;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * @author Trevor/extremesnow
 * @since 8/23/2020 at 11:49 AM
 */
@Getter @Setter
public class PlayerData {

    private UUID uuid;
    private String name;
    private FastBoard scoreboard;
    private boolean scoreboardEnabled;
    private BukkitTask scoreboardTask;

    private PlayerData(){}

    public PlayerData(Snowboard plugin, UUID uuid) {
        this.uuid = uuid;
        this.scoreboardEnabled = plugin.getFileUtil().getConfigFile().isScoreboardToggledByDefault();
    }

}
