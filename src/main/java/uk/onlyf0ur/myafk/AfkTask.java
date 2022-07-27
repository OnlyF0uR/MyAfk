package uk.onlyf0ur.myafk;

import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;

public class AfkTask implements Runnable {
    private final Player player;
    private final MyAfk plugin;

    public AfkTask(Player player, MyAfk plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        String pName = this.player.getName();
        if (!this.plugin.afkPlayers.contains(pName)) {
            Long cTime = Instant.now().getEpochSecond();
            if (cTime - this.plugin.players.get(pName) >
                    this.plugin.fileManager.getConfig("Config.yml").get().getLong("AfkDelay")) {
                this.plugin.afkPlayers.add(pName);
                this.player.sendMessage(this.plugin.getMessage("NowAfk.Message"));

                Title.Times titleTime = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1));
                Title title = Title.title(
                        this.plugin.getMessage("NowAfk.Title"),
                        this.plugin.getMessage("NowAfk.Subtitle"),
                        titleTime
                );

                this.player.showTitle(title);
            }
        }
    }
}
