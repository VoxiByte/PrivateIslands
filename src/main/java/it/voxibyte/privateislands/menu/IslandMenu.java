package it.voxibyte.privateislands.menu;

import fr.minuskube.inv.SmartInventory;
import it.voxibyte.privateislands.island.IslandHandler;
import it.voxibyte.privateislands.menu.provider.IslandMenuProvider;
import org.bukkit.entity.Player;

public class IslandMenu {
    private static SmartInventory islandInventory;

    public static void init(IslandHandler islandHandler) {
        islandInventory = SmartInventory.builder()
                .id("islandMenu")
                .provider(new IslandMenuProvider(islandHandler))
                .size(3, 9)
                .title("Opzioni isola")
                .build();
    }

    public static void open(Player player) {
        islandInventory.open(player);
    }

    public static void close(Player player) {
        islandInventory.close(player);
    }


}
