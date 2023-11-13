package it.voxibyte.privateislands.island;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IslandHandler {
    private final List<Island> loadedIslands;

    public IslandHandler() {
        this.loadedIslands = new ArrayList<>();
    }

    public void loadIsland(Island island, boolean save) {
        this.loadedIslands.add(island);
    }

    public Optional<Island> findIslandByOwner(UUID ownerUid) {
        return this.loadedIslands.stream()
                .filter(island -> island.getOwnerUid() == ownerUid)
                .findAny();
    }
}
