package nesoi.network.NClaim;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Messages {

    private static File file;
    private static FileConfiguration config;

    public Messages() {
        loadConfig();
    }

    public static void loadConfig() {
        file = new File(NCoreMain.inst().getDataFolder(), "config/messages.yml");
        if(!file.exists()) {
            NCoreMain.inst().saveResource("config/messages.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public void saveAllChanges() {
        saveMessagesData();
    }

    private void saveMessagesData() {
        try {
            config.save(file);
        } catch (IOException e) {
            NCoreMain.inst().getLogger().warning("Could not save config data.");
        }
    }

}
