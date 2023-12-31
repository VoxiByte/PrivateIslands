package it.voxibyte.privateislands.menu.provider;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import it.voxibyte.privateislands.island.Island;
import it.voxibyte.privateislands.island.IslandFactory;
import it.voxibyte.privateislands.island.IslandHandler;
import it.voxibyte.privateislands.menu.IslandMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;
import java.util.UUID;

public class IslandMenuProvider implements InventoryProvider {
    private final IslandHandler islandHandler;

    public IslandMenuProvider(IslandHandler islandHandler) {
        this.islandHandler = islandHandler;
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        ClickableItem generationItem = getGenerationItem(player);
        ClickableItem teleportItem = getTeleportItem(player);

        inventoryContents.set(new SlotPos(1, 3), generationItem);
        inventoryContents.set(new SlotPos(1, 5), teleportItem);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private ClickableItem getGenerationItem(Player player) {
        ClickableItem generationitem = null;
        if(islandHandler.findIslandByOwner(player.getUniqueId()).isEmpty()) {
            //Ottimizzabile attraverso un ItemBuilder
            ItemStack createItem = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta createItemMeta = createItem.getItemMeta();
            createItemMeta.setDisplayName("Crea l'isola");
            createItem.setItemMeta(createItemMeta);

            generationitem = ClickableItem.of(createItem, inventoryClickEvent -> {
                islandHandler.createIsland(player, true);

                IslandMenu.close(player);
                player.sendMessage(ChatColor.GREEN + "Isola creata con successo");
            });
        } else {
            ItemStack regenerateItem = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta regenerateItemItemMeta = regenerateItem.getItemMeta();
            regenerateItemItemMeta.setDisplayName("Rigenera l'isola");
            regenerateItem.setItemMeta(regenerateItemItemMeta);

            generationitem = ClickableItem.of(regenerateItem, inventoryClickEvent -> {
                islandHandler.regenerateIsland(player);

                IslandMenu.close(player);
                player.sendMessage(ChatColor.GREEN + "Isola rigenerata con successo");
            });
        }

        return generationitem;
    }

    private ClickableItem getTeleportItem(Player player) {
        Optional<Island> islandOptional = islandHandler.findIslandByOwner(player.getUniqueId());
        if(islandOptional.isEmpty())
            return ClickableItem.empty(new ItemStack(Material.BARRIER));

        ItemStack homeItem = new ItemStack(Material.DARK_OAK_DOOR);
        ItemMeta homeItemMeta = homeItem.getItemMeta();
        homeItemMeta.setDisplayName("Vai alla tua isola");
        homeItem.setItemMeta(homeItemMeta);

        return ClickableItem.of(homeItem, inventoryClickEvent -> {
            World islandWorld = Bukkit.getWorld(islandOptional.get().getWorldUid());
            player.teleport(islandWorld.getSpawnLocation());
            IslandMenu.close(player);
        });
    }
}
