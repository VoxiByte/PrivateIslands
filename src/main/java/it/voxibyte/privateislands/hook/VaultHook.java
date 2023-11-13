package it.voxibyte.privateislands.hook;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private Economy economy;

    public VaultHook() {
        initializeEconomy();
    }

    public boolean withdraw(Player player, double amount) {
        if (this.economy != null) {
            EconomyResponse economyResponse = this.economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), amount); //Nota: withDrawPlayer(Player, Double) è deprecato
            return economyResponse.transactionSuccess();
        }

        return false;
    }

    private void initializeEconomy() {
        final RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

        if(economyProvider != null) {
            this.economy = economyProvider.getProvider();
        }
    }

}
