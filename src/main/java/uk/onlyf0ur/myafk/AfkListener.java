package uk.onlyf0ur.myafk;

import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

public record AfkListener(MyAfk plugin) implements Listener {
    private static HashMap<String, Integer> tasks = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.hasChangedBlock()) {
            Player p = e.getPlayer();
            String pName = p.getName();

            this.plugin.players.put(pName, Instant.now().getEpochSecond());

            if (this.plugin.afkPlayers.contains(pName)) {
                this.plugin.afkPlayers.remove(pName);

                p.sendMessage(this.plugin.getMessage("NoLongerAfk.Message"));

                Title.Times titleTime = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1));
                Title title = Title.title(
                        this.plugin.getMessage("NoLongerAfk.Title"),
                        this.plugin.getMessage("NoLongerAfk.Subtitle"),
                        titleTime
                );

                p.showTitle(title);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String pName = p.getName();

        BukkitTask task = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, new AfkTask(p, this.plugin), 20L * 60, 20L * 60);

        // We do not do any checks here because the remove function does that for us
        this.plugin.players.put(pName, Instant.now().getEpochSecond());
        tasks.put(pName, task.getTaskId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        String pName = e.getPlayer().getName();

        // We do not do any checks here because the remove function does that for us
        this.plugin.players.remove(pName);
        this.plugin.afkPlayers.remove(pName);

        this.plugin.getServer().getScheduler().cancelTask(tasks.get(pName));
        tasks.remove(pName);
    }

}
