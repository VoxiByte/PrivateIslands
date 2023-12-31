package it.voxibyte.privateislands.island;

import it.voxibyte.privateislands.database.MysqlDatabase;
import it.voxibyte.privateislands.exception.PersistenceException;
import it.voxibyte.privateislands.hook.VaultHook;
import it.voxibyte.privateislands.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static it.voxibyte.privateislands.util.ThreadUtil.executeAsync;

public class IslandHandler {
    private final IslandRepository islandRepository;
    private final VaultHook vaultHook;
    private final List<Island> loadedIslands;
    private final String islandTemplateWorld;

    public IslandHandler(MysqlDatabase mysqlDatabase, String islandTemplateWorld) {
        this.islandRepository = new IslandRepository(mysqlDatabase);
        this.vaultHook = new VaultHook();
        this.loadedIslands = new ArrayList<>();
        this.islandTemplateWorld = islandTemplateWorld;
    }

    public void regenerateIsland(Player player) {
        Optional<Island> playerIsland = findIslandByOwner(player.getUniqueId());
        if(!playerIsland.isPresent()) {
            return;
        }

        Island island = playerIsland.get();
        WorldUtil.deleteWorld(island.getWorldUid());

        executeAsync(() -> {
            boolean deleted = this.islandRepository.deleteIsland(island);

            if(!deleted) throw new PersistenceException("unable to delete island " + island.getWorldUid());

            this.loadedIslands.remove(island);
            createIsland(player, true);
        });
    }

    public void createIsland(final Player player, boolean save) {
        boolean transactionDone = vaultHook.withdraw(player, 150);
        if(!transactionDone)
            player.sendMessage("Assicurati di avere almeno 150 monete per creare un'isola!");

        World generatedWorld = WorldUtil.copyWorld(islandTemplateWorld, player.getUniqueId().toString());
        Island island = IslandFactory.createIsland(player, generatedWorld.getUID());

        loadIsland(island, save);
        player.teleport(generatedWorld.getSpawnLocation());
    }

    public void loadIsland(final Island island, final boolean save) {
        this.loadedIslands.add(island);

        if(save) {
            executeAsync(() -> {
                boolean saved = this.islandRepository.saveIsland(island);

                if(!saved) throw new PersistenceException("unable to save island " + island.getWorldUid());
            });
        }
    }

    public Optional<Island> findIslandByOwner(final UUID ownerUid) {
        return this.loadedIslands.stream()
                .filter(island -> island.getOwnerUid().equals(ownerUid))
                .findAny();
    }

    public void loadIslands() {
        List<Island> loadedIslands = islandRepository.loadIslands();

        this.loadedIslands.addAll(loadedIslands);
    }
}
