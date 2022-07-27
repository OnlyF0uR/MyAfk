package uk.onlyf0ur.myafk;

import net.kyori.adventure.title.Title;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class AfkCMD implements CommandExecutor {
    private final MyAfk plugin;

    public AfkCMD(MyAfk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;
        String pName = p.getName();

        if (plugin.afkPlayers.contains(pName)) {
            this.plugin.afkPlayers.remove(pName);
            p.sendMessage(this.plugin.getMessage("NoLongerAfk.Message"));

            Title.Times titleTime = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1));
            Title title = Title.title(
                    this.plugin.getMessage("NoLongerAfk.Title"),
                    this.plugin.getMessage("NoLongerAfk.Subtitle"),
                    titleTime
            );

            p.showTitle(title);
        } else {
            this.plugin.afkPlayers.add(pName);
            p.sendMessage(this.plugin.getMessage("NowAfk.Message"));

            Title.Times titleTime = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1));
            Title title = Title.title(
                    this.plugin.getMessage("NowAfk.Title"),
                    this.plugin.getMessage("NowAfk.Subtitle"),
                    titleTime
            );

            p.showTitle(title);
        }

        return true;
    }
}
