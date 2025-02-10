package nesoi.network.NClaim.menus.claim;

import nesoi.network.NClaim.NCoreMain;
import nesoi.network.NClaim.models.ClaimDataManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.nandayo.DAPI.GUIManager.Button;
import org.nandayo.DAPI.GUIManager.LazyButton;
import org.nandayo.DAPI.GUIManager.Menu;
import org.nandayo.DAPI.ItemCreator;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ClaimListMenu extends Menu {

    public ClaimListMenu(Player p) {
        this.createInventory(9 * 6, "NClaim - All Claims");
        this.addLazyButton(new LazyButton(Set.of(0,1,2,3,4,5,6,7,8,9,13,17,18,22,26,27,31,35,36,40,44,45,46,47,48,49,50,51,52,53)) {
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(Material.TINTED_GLASS).name(" ").get();
            }
        });
        setup(p);
        displayTo(p);
    }

    private void setup(Player p) {
        ClaimDataManager claimDataManager = NCoreMain.inst().claimDataManager;

        List<String> playerClaims = claimDataManager.getPlayerClaims(p.getUniqueId());
        List<String> coopClaims = claimDataManager.getCoopClaims(p.getUniqueId());

        int[] playerSlots = {10, 11, 12, 19, 20, 21, 28, 29, 30, 37, 38, 39};
        int[] coopSlots = {14, 15, 16, 23, 24, 25, 32, 33, 34, 41, 42, 43};

        int playerSlotIndex = 0;
        for (String claimKey : playerClaims) {
            String[] parts = claimKey.split("_");
            int chunkX = Integer.parseInt(parts[0]);
            int chunkZ = Integer.parseInt(parts[1]);

            if (playerSlotIndex >= playerSlots.length) break;
            int playerSlot = playerSlots[playerSlotIndex++];

            this.addButton(new Button(playerSlot) {
                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(Material.GRASS_BLOCK)
                            .name("{BROWN}Claim: " + claimKey)
                            .lore(
                                    "",
                                    "{WHITE}World: {GRAY}" + claimDataManager.getClaimWorld(chunkX, chunkZ),
                                    "{WHITE}Coordinates: {GRAY}" + claimDataManager.getClaimCoords(claimKey),
                                    "{WHITE}Owner: {GRAY}" + p.getName(),
                                    "",
                                    "{YELLOW}Click to view or manage this Claim."
                            )
                            .get();
                }

                @Override
                public void onClick(Player p, ClickType clickType) {
                    Location bedrockLocation = claimDataManager.getClaimBedrockLocation(claimKey);
                    new ClaimMenu(p, bedrockLocation.getChunk());
                }
            });
        }

        int coopSlotIndex = 0;
        for (String claimKey : coopClaims) {
            String[] parts = claimKey.split("_");
            int chunkX = Integer.parseInt(parts[0]);
            int chunkZ = Integer.parseInt(parts[1]);

            World world = Bukkit.getWorld(claimDataManager.getClaimWorld(chunkX, chunkZ));

            assert world != null;
            Chunk chunk = world.getChunkAt(chunkX, chunkZ);

            if (coopSlotIndex >= coopSlots.length) break;
            int coopSlot = coopSlots[coopSlotIndex++];

            UUID ownerUUID = UUID.fromString(claimDataManager.getClaimOwner(chunk));
            OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);
            String ownerName = owner.getName() != null ? owner.getName() : "Unknown";

            this.addButton(new Button(coopSlot) {
                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(Material.OAK_SIGN)
                            .name("{BROWN}Coop Claim: " + claimKey)
                            .lore(
                                    "",
                                    "{WHITE}World: {GRAY}" + claimDataManager.getClaimWorld(chunkX, chunkZ),
                                    "{WHITE}Coordinates: {GRAY}" + claimDataManager.getClaimCoords(claimKey),
                                    "{WHITE}Claim Owner: {GRAY}" + ownerName,
                                    "",
                                    "{YELLOW}You can only overview this Claim."
                            )
                            .get();
                }

                @Override
                public void onClick(Player p, ClickType clickType) {
                }
            });
        }
    }
}
