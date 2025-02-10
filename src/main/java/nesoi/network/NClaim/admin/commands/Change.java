package nesoi.network.NClaim.admin.commands;

import nesoi.network.NClaim.NCoreMain;
import org.bukkit.entity.Player;

import java.util.List;

public class Change {

    public void execute(Player player, String[] args) {

        if (args.length < 4) {
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.wrong-usage"));
            return;
        }

        // /nclaim admin change money-data PlayerData

        if (!player.hasPermission("nclaim.admin") || !player.hasPermission("nclaim.change")) {
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.dont-have-a-permission"));
            return;
        }

        String moneySource = args[3];
        String moneyData = NCoreMain.inst().config.getString("money-data");

        if (moneySource.equalsIgnoreCase(moneyData)) {
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.value-already-set", List.of(moneySource)));
            return;
        }

        if (moneySource.equalsIgnoreCase("PlayerData")) {
            NCoreMain.inst().config.setString("money-data", "PlayerData");
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.money-data-changed", List.of("PlayerData")));
        }

        else if (moneySource.equalsIgnoreCase("Vault")) {
            NCoreMain.inst().config.setString("money-data", "Vault");
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.money-data-changed", List.of("Vault")));
        }

        else {
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.invalid-data", List.of(moneySource)));
        }

    }
}
