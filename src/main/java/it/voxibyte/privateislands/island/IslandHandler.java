package it.voxibyte.privateislands.island;

import it.voxibyte.privateislands.database.MysqlDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IslandHandler {
    private final IslandRepository islandRepository;
    private final List<Island> loadedIslands;

    public IslandHandler(MysqlDatabase mysqlDatabase) {
        this.islandRepository = new IslandRepository(mysqlDatabase);
        this.loadedIslands = new ArrayList<>();
    }

    public void loadIsland(final Island island, final boolean save) {
        this.loadedIslands.add(island);

        if(save)
            this.islandRepository.saveIsland(island);
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
