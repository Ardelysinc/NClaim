package nesoi.network.NClaim;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.nandayo.DAPI.HexUtil.color;

public class Config {

    private File file;
    private FileConfiguration config;
    private final String unknown = "Unknown";

    public Config() {
        loadConfig();
    }

    private void loadConfig() {
        file = new File(NCoreMain.inst().getDataFolder(), "config/config.yml");
        if (!file.exists()) {
            NCoreMain.inst().saveResource("config/config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    private String getColor(String color) {
        return config.getString("colors." + color, unknown);
    }

    // Colors the string with config colors.
    public String getColoredString(String msg) {
        ConfigurationSection section = config.getConfigurationSection("colors");
        if (section != null) {
            for (String color : section.getKeys(false)) {
                msg = msg.replace("{" + color.toUpperCase(Locale.ROOT) + "}", getColor(color));
            }
        }
        return color(msg);
    }

    public List<String> getColoredStringList(List<String> list) {
        List<String> coloredList = new ArrayList<>();
        for (String msg : list) {
            coloredList.add(getColoredString(msg));
        }
        return coloredList;
    }

    // Uses %index handlers and prefix replacements.
    public String getLoadedString(String path, List<Object> obj) {
        String msg = Messages.getConfig().getString(path, unknown);
        for (int i = 0; i < obj.size(); i++) {
            msg = msg.replace("%" + i, String.valueOf(obj.get(i)));
        }
        return color(getColoredString(msg).replace("%p", config.getString("prefix", unknown)));
    }

    public String getLoadedString(String path) {
        String msg = Messages.getConfig().getString(path, unknown);
        return color(getColoredString(msg).replace("%p", config.getString("prefix", unknown)));
    }

    public List<String> getLoadedStringList(String path) {
        List<String> list = new ArrayList<>();
        for (String msg : Messages.getConfig().getStringList(path)) {
            list.add(color(getColoredString(msg).replace("%p", config.getString("prefix", unknown))));
        }
        return list;
    }

    public String getString(String path) {
        return config.getString(path, "Unknown");
    }
    public void setString(String path, String value) {
        config.set(path, value);
        saveConfigData();
    }

    public long getLong(String path) {
        return config.getLong(path, 0);
    }

    public List<String> getListedStrings(String path) {
        return config.getStringList(path);
    }
    public boolean getBoolean(String path) {
        return config.getBoolean(path, false);
    }

    public int getInt(String path) {
        return config.getInt(path, 0);
    }

    public void saveAllChanges() {
        saveConfigData();
    }

    private void saveConfigData() {
        try {
            config.save(file);
        } catch (IOException e) {
            NCoreMain.inst().getLogger().warning("Could not save config data.");
        }
    }

}
