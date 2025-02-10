package nesoi.network.NClaim.menus.claim;


import nesoi.network.NClaim.NCoreMain;
import nesoi.network.NClaim.menus.claim.coop.ManageCoopsMenu;
import nesoi.network.NClaim.menus.claim.land.ExpandClaimMenu;
import nesoi.network.NClaim.models.ClaimDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.nandayo.DAPI.GUIManager.Button;
import org.nandayo.DAPI.GUIManager.Menu;
import org.nandayo.DAPI.ItemCreator;

import java.util.Objects;
import java.util.UUID;

import static nesoi.network.NClaim.utils.HeadManager.getPlayerHead;

public class ClaimMenu extends Menu {

    public ClaimMenu(Player p, Chunk chunk) {
        ClaimDataManager claimDataManager = NCoreMain.inst().claimDataManager;

        this.setSize(9*3);
        this.setTitle("NClaim - Manage Claim");

        this.addButton(new Button(11) {
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(Material.GRASS_BLOCK)
                        .name("{BROWN}Manage Chunks")
                        .lore(
                                "",
                                "{GRAY}Expand {WHITE}your {GRAY}Claim {WHITE}here.",
                                "{WHITE}No need to buy a new claim.",
                                "",
                                "{YELLOW}Click to expand your Claim."
                        )
                        .get();
            }

            @Override
            public void onClick(Player p, ClickType clickType) {
                new ExpandClaimMenu(p, chunk);
            }
        });

        this.addButton(new Button(13) {
            @Override
            public ItemStack getItem() {
                ItemStack itemStack;
                itemStack = getPlayerHead(p.getPlayer());
                itemStack = ItemCreator.of(itemStack)
                        .name("{BROWN}Manage Members")
                        .lore(
                                "",
                                "{WHITE}Manage your claim's {GRAY}members{WHITE} here.",
                                "{WHITE}Easily {GRAY}add{WHITE} or {GRAY}remove{WHITE} members from your claim.",
                                "",
                                "{YELLOW}Click to manage your claim's members."
                        )
                        .get();
                return itemStack;
            }

            @Override
            public void onClick(Player p, ClickType clickType) {
                new ManageCoopsMenu(NCoreMain.inst(), p, chunk);
            }
        });



        this.addButton(new Button(15) {

            final String ownerID = claimDataManager.getClaimOwner(chunk);
            final String ownerName = (ownerID != null) ? Objects.requireNonNull(Bukkit.getServer().getPlayer(UUID.fromString(ownerID))).getName() : "{RED}Unclaimed";

            final String createdDate = claimDataManager.getCreatedDate(chunk);
            final int coopCount = claimDataManager.getCoopCount(chunk);

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(Material.BEDROCK)

                        .name("{BROWN}Claim Infos")
                        .lore(
                                "",
                                "{WHITE}Claim Owner: {GRAY}" + ownerName,
                                "{WHITE}Claim Created Date: {GRAY}" + createdDate,
                                "{WHITE}Coop Member Counts: {GRAY}" + coopCount,
                                " "
                        )
                        .get();
            }

            @Override
            public void onClick(Player p, ClickType clickType) {

            }
        });
        displayTo(p);
    }
}
