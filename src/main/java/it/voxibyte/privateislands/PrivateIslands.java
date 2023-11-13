package it.voxibyte.privateislands;

import it.voxibyte.privateislands.database.MysqlDatabase;
import it.voxibyte.privateislands.island.IslandHandler;
import it.voxibyte.privateislands.island.IslandRepository;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrivateIslands extends JavaPlugin {
    private static JavaPlugin plugin;
    private MysqlDatabase mysqlDatabase;
    private IslandHandler islandHandler;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        this.mysqlDatabase = initDatabase();
        this.islandHandler = new IslandHandler(mysqlDatabase);

        this.islandHandler.loadIslands();
    }

    @Override
    public void onDisable() {

    }

    private MysqlDatabase initDatabase() {
        String url = this.getConfig().getString("datasource.url");
        String username = this.getConfig().getString("datasource.username");
        String password = this.getConfig().getString("datasource.password");

        return new MysqlDatabase(url, username, password);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
