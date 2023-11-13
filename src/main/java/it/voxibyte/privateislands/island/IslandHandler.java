package it.voxibyte.privateislands.island;

import it.voxibyte.privateislands.database.MysqlDatabase;
import it.voxibyte.privateislands.exception.PersistenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static it.voxibyte.privateislands.util.ThreadUtil.executeAsync;

public class IslandHandler {
    private final IslandRepository islandRepository;
    private final List<Island> loadedIslands;

    public IslandHandler(MysqlDatabase mysqlDatabase) {
        this.islandRepository = new IslandRepository(mysqlDatabase);
        this.loadedIslands = new ArrayList<>();
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
                .filter(island -> island.getOwnerUid() == ownerUid)
                .findAny();
    }

    public void loadIslands() {
        List<Island> loadedIslands = islandRepository.loadIslands();

        this.loadedIslands.addAll(loadedIslands);
    }
}
