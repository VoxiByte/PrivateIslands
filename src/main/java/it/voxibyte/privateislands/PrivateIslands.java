package it.voxibyte.privateislands;

import it.voxibyte.privateislands.island.IslandHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrivateIslands extends JavaPlugin {
    private IslandHandler islandHandler;

    @Override
    public void onEnable() {
        this.islandHandler = new IslandHandler();
    }

    @Override
    public void onDisable() {

    }
}
