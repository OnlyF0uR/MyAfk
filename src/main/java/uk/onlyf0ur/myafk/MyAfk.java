package uk.onlyf0ur.myafk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAfk extends JavaPlugin {
    public FileManager fileManager = new FileManager(this);

    public HashMap<String, Long> players = new HashMap<>();
    public ArrayList<String> afkPlayers = new ArrayList<>();

    public void onEnable() {
        this.fileManager.getConfig("Config.yml").setDefaults(true);

        this.getCommand("afk").setExecutor(new AfkCMD(this));
        Bukkit.getPluginManager().registerEvents(new AfkListener(this), this);
    }

    public Component getMessage(String path) {
        return buildColourComponent(this.fileManager.getConfig("Config.yml").get().getString("Text." + path));
    }

    public Component buildColourComponent(String plaintext) {
        // --#fffff--Hello --#11111--World!
        if (!plaintext.contains("--")) {
            return Component.text(plaintext);
        }

        Component result = Component.text("");

        String[] ptArray = plaintext.split("--");
        String activeColour = null;
        for (String pt : ptArray) {
            if (pt.length() == 7 && pt.contains("#")) {
                activeColour = pt;
            } else {
                if (activeColour == null) {
                    result = result.append(Component.text(pt).decoration(TextDecoration.ITALIC, false));
                } else {
                    result = result.append(Component.text(pt).color(TextColor.fromHexString(activeColour)));
                }
            }
        }

        return result;
    }

}
