package it.voxibyte.privateislands;

import it.voxibyte.privateislands.command.IslandCommand;
import it.voxibyte.privateislands.database.MysqlDatabase;
import it.voxibyte.privateislands.island.IslandHandler;
import it.voxibyte.privateislands.island.IslandRepository;
import it.voxibyte.privateislands.menu.IslandMenu;
import it.voxibyte.privateislands.menu.provider.IslandMenuProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrivateIslands extends JavaPlugin {
    private static JavaPlugin plugin;
    private MysqlDatabase mysqlDatabase;
    private IslandHandler islandHandler;

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        registerCommands();

        this.mysqlDatabase = initDatabase();

        String islandTemplateWorld = getConfig().getString("island-template-world");
        this.islandHandler = new IslandHandler(mysqlDatabase, islandTemplateWorld);

        this.islandHandler.loadIslands();
        IslandMenu.init(islandHandler);
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

    private void registerCommands() {
        getCommand("island").setExecutor(new IslandCommand());
    }
}
