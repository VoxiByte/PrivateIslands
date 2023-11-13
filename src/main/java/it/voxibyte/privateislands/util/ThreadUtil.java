package it.voxibyte.privateislands.util;

import it.voxibyte.privateislands.PrivateIslands;
import org.bukkit.Bukkit;

public class ThreadUtil {

    public static void executeAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(PrivateIslands.getPlugin(), runnable, 1);
    }

}
