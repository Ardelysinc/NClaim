package nesoi.network.NClaim.admin.commands;

import nesoi.network.NClaim.NCoreMain;
import nesoi.network.NClaim.models.ClaimDataManager;
import nesoi.network.NClaim.models.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import java.util.List;

import static nesoi.network.NClaim.NCoreMain.economy;


public class Add {

    public void execute(Player player, String[] args) {
        if (args.length < 5) {
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.wrong-usage"));
            return;
        }

        if (!player.hasPermission("nclaim.add") || !player.hasPermission("nclaim.admin")) {
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.dont-have-a-permission"));
            return;
        }

        String value = args[2];
        int amount;
        try {
            amount = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.enter-a-valid-number"));
            return;
        }

        Player target = Bukkit.getPlayerExact(args[4]);
        if (target == null) {
            target = player;
        }

        if (value.equalsIgnoreCase("balance")) {
            if (amount <= 0) {
                player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.enter-a-valid-number"));
                return;
            }

            String moneyData = NCoreMain.inst().config.getString("money-data");

            if (moneyData.equals("Vault")) {
                economy.depositPlayer(target, amount);
                double newBalance = economy.getBalance(target);

                target.sendMessage(NCoreMain.inst().config.getLoadedString("messages.balance-added-to-target", List.of(amount, newBalance)));
                player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.balance-added-successfully", List.of(amount, target.getName())));
            } else if (moneyData.equals("PlayerData")) {
                PlayerDataManager playerDataManager = NCoreMain.pdCache.get(target);
                double currentValue = playerDataManager.getBalance();
                playerDataManager.setBalance(currentValue + amount);
                double newValue = playerDataManager.getBalance();

                target.sendMessage(NCoreMain.inst().config.getLoadedString("messages.balance-added-to-target", List.of(amount, newValue)));
                player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.balance-added-successfully", List.of(amount,target.getName())));
            } else {
                player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.setup-your-config-file"));
            }

        } else if (value.equalsIgnoreCase("claim-expiration-date")) {
            if (args.length < 6) {
                player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.wrong-usage"));
                return;
            }

            int days = 0, hours = 0, minutes = 0;

            try {
                days = Integer.parseInt(args[3]);
                hours = Integer.parseInt(args[4]);
                minutes = Integer.parseInt(args[5]);
            } catch (NumberFormatException e) {
                player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.invalid-number-format"));
                return;
            }

            Chunk chunk = player.getLocation().getChunk();
            ClaimDataManager claimDataManager = NCoreMain.inst().claimDataManager;
            claimDataManager.extendExpirationDate(player, chunk, days, hours, minutes);

        } else {
            player.sendMessage(NCoreMain.inst().config.getLoadedString("messages.enter-a-valid-data"));
        }

    }

}
