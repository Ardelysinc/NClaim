package nesoi.network.NClaim.menus.claim.coop;

import nesoi.network.NClaim.NCoreMain;
import nesoi.network.NClaim.menus.ConfirmMenu;
import nesoi.network.NClaim.models.ClaimDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.nandayo.DAPI.GUIManager.Button;
import org.nandayo.DAPI.GUIManager.Menu;
import org.nandayo.DAPI.ItemCreator;

import java.util.*;
import java.util.function.Consumer;

import static nesoi.network.NClaim.utils.HeadManager.getPlayerHead;
import static org.nandayo.DAPI.HexUtil.parse;

public class ManageCoopPlayerMenu extends Menu {

    private final OfflinePlayer targetPlayer;
    private final Chunk chunk;

    public ManageCoopPlayerMenu(Player owner, OfflinePlayer targetPlayer, Chunk chunk) {
        this.createInventory(9 * 6, "NClaim - Edit Co-op");
        this.targetPlayer = targetPlayer;
        this.chunk = chunk;

        setup();
        displayTo(owner);
    }

    public void setup() {
        ClaimDataManager claimDataManager = NCoreMain.inst().claimDataManager;

        List<String> permissions = Arrays.asList(
                "can-break-spawners",
                "can-place-spawners",
                "can-cast-water-or-lava",
                "can-interact-with-claim-bedrock", // ALWAYS FALSE
                "can-break-blocks",
                "can-place-blocks",
                "can-interact-with-chests",
                "can-interact-with-buttons-doors-pressure-plates"
        );

        List<String> permissionNames = Arrays.asList(
                "Spawner Breaking",
                "Spawner Placing",
                "Casting Water and Lavas",
                "Interact With Claim Bedrock", // ALWAYS FALSE
                "Breaking Blocks",
                "Placing Blocks",
                "Interacting with Chests",
                "Interacting with Buttons, Doors, Pressure Plates"
        );

        List<List<String>> permissionLores = Arrays.asList(
                Arrays.asList("&f", "{GRAY}Can the player {WHITE}break {GRAY}the {WHITE}spawners{GRAY}?", "&f"),
                Arrays.asList("&f", "{GRAY}Can the player {WHITE}place {GRAY}the {WHITE}spawners{GRAY}?", "&f"),
                Arrays.asList("&f", "{GRAY}Can the player {WHITE}cast {GRAY}water or lava?", "&f"),
                Arrays.asList("&f", "{GRAY}Can the player {WHITE}interact {GRAY}with claim bedrock?", "&f"), // ALWAYS FALSE
                Arrays.asList("&f", "{GRAY}Can the player {WHITE}break {GRAY}blocks?", "&f"),
                Arrays.asList("&f", "{GRAY}Can the player {WHITE}place {GRAY}blocks?", "&f"),
                Arrays.asList("&f", "{GRAY}Can the player {WHITE}interact with {GRAY}chests?", "&f"),
                Arrays.asList("&f", "{GRAY}Can the player {WHITE}interact with", "{GRAY}buttons, doors, pressure plates?" , "&f")
        );

        List<Integer> paperSlots = Arrays.asList(11, 20, 29, 38, 14, 23, 32, 41);
        List<Integer> dyeSlots = Arrays.asList(12, 21, 30, 39, 15, 24, 33, 42);

        this.addButton(new Button(45) {
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(Material.ARROW)
                        .name("{ORANGE}Go Back")
                        .get();
            }

            @Override
            public void onClick(Player p, ClickType clickType) {
                new ManageCoopsMenu(NCoreMain.inst(), p, chunk);
            }
        });

        this.addButton(new Button(0) {
            @Override
            public ItemStack getItem() {
                ItemStack itemStack;
                itemStack = getPlayerHead(targetPlayer);
                itemStack = ItemCreator.of(itemStack)
                        .name("{YELLOW}" + targetPlayer.getName())
                        .lore("", "{WHITE}You are now {GRAY}setting {WHITE}this", "{WHITE}player's {GRAY}permissions{WHITE}.")
                        .get();

                return itemStack;
            }

            @Override
            public void onClick(Player p, ClickType clickType) {

            }
        });

        this.addButton(new Button(53) {
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(Material.BARRIER)
                        .name("{BROWN}Kick player from claim")
                        .lore("",
                                "{WHITE}After doing this, the player will be",
                                "{GRAY}kicked {WHITE}from the claim and you",
                                "{WHITE}will {GRAY}not be able to add {WHITE}the player",
                                "{WHITE}back to the claim for {GRAY}15 minutes{WHITE}.")
                        .get();
            }

            @Override
            public void onClick(Player p, ClickType clickType) {
                Consumer<String> onFinish = (result) -> {
                    if ("confirmed".equals(result)) {
                        Player onlinePlayer = Bukkit.getPlayer(targetPlayer.getUniqueId());
                        claimDataManager.kickCoopPlayer(p, onlinePlayer, chunk);
                        p.closeInventory();
                    } else if ("declined".equals(result)) {
                        new ManageCoopPlayerMenu(p, targetPlayer, chunk);
                    }
                };

                new ConfirmMenu(p, "Kick from Claim", Arrays.asList("", "{WHITE}After {GRAY}confirming {WHITE}this action,", "{WHITE}the {GRAY}selected {WHITE}person will be", "{GRAY}kicked {WHITE}from {GRAY}Claim{WHITE}."), onFinish);
            }
        });

        for (int i = 0; i < permissions.size(); i++) {
            String perm = permissions.get(i);
            String permName = permissionNames.get(i);
            List<String> permLore = new ArrayList<>(permissionLores.get(i));

            boolean permissionStatus = claimDataManager.getCoopPlayerPermission(chunk, targetPlayer.getUniqueId().toString(), perm);
            String permissionColor = permissionStatus ? "{GREEN}Active" : "{RED}Inactive";
            Material dyeMaterial = permissionStatus ? Material.LIME_DYE : Material.GRAY_DYE;

            permLore.add("{GRAY}Status: " + parse(permissionColor));

            this.addButton(new Button(paperSlots.get(i)) {
                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(Material.PAPER)
                            .name("{BROWN}" + permName)
                            .lore(permLore)
                            .get();
                }

                @Override
                public void onClick(Player p, ClickType clickType) {

                }
            });

            this.addButton(new Button(dyeSlots.get(i)) {
                @Override
                public ItemStack getItem() {
                    Material finalDyeMaterial = perm.equals("can-interact-with-claim-bedrock") ? Material.RED_DYE : dyeMaterial;

                    return ItemCreator.of(finalDyeMaterial)
                            .name(" ")
                            .get();
                }

                @Override
                public void onClick(Player p, ClickType clickType) {
                    if (!perm.equals("can-interact-with-claim-bedrock")) {
                        claimDataManager.toggleCoopPlayerPermission(chunk, targetPlayer.getUniqueId().toString(), perm);
                        new ManageCoopPlayerMenu(p, targetPlayer, chunk);
                    }
                }
            });
        }
    }
}
