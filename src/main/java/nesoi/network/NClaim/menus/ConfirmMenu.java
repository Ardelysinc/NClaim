package nesoi.network.NClaim.menus;

import org.nandayo.DAPI.GUIManager.Button;
import org.nandayo.DAPI.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.nandayo.DAPI.GUIManager.LazyButton;
import org.nandayo.DAPI.GUIManager.Menu;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ConfirmMenu extends Menu {

    private final Consumer<String> onFinish;
    private final String itemName;
    private final List<String> lore;

    public ConfirmMenu(Player p, String itemName, List<String> lore, Consumer<String> onFinish) {
        this.itemName = itemName;
        this.onFinish = onFinish;
        this.lore = lore;
        this.addLazyButton(new LazyButton(Set.of(0, 8, 9, 17, 18, 26, 27, 35, 36, 44)) {
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(Material.TINTED_GLASS).name(" ").get();
            }
        });
        this.createInventory(9 * 5, "Do you approve this action");
        setup();
        displayTo(p);
    }

    private void setup() {
        this.addButton(new Button(22) {
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(Material.BOOK)
                        .name(("{BROWN}" + itemName))
                        .lore(lore)
                        .get();
            }

            @Override
            public void onClick(Player p, ClickType clickType) {

            }
        });

        this.addButton(new Button(20) {
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(Material.GREEN_DYE)
                        .name("{GREEN}Confirm")
                        .get();
            }

            @Override
            public void onClick(Player p, ClickType clickType) {
                onFinish.accept("confirmed");
            }
        });

        this.addButton(new Button(24) {
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(Material.RED_DYE)
                        .name("{RED}Decline")
                        .get();
            }

            @Override
            public void onClick(Player p, ClickType clickType) {
                onFinish.accept("declined");
            }
        });

    }
}
