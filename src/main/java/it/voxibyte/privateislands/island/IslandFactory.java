package it.voxibyte.privateislands.island;

import org.bukkit.entity.Player;

import java.util.UUID;

public class IslandFactory {

    public static Island createIsland(Player islandOwner, UUID worldUid) {
        return new Island(islandOwner.getUniqueId(), worldUid);
    }

}
