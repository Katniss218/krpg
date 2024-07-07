package io.katniss218.krpg.core;

import java.util.logging.Logger;

import io.katniss218.krpg.core.chat.AsyncChatListener;
import io.katniss218.krpg.core.combat.EntityDamageByEntityListener;
import io.katniss218.krpg.core.combat.EntityDamageListener;
import io.katniss218.krpg.core.combat.EntityDeathListener;
import io.katniss218.krpg.core.combat.PlayerItemConsumeListener;
import io.katniss218.krpg.core.definitions.*;
import io.katniss218.krpg.core.entities.RPGEntityCommand;
import io.katniss218.krpg.core.entities.RPGEntityTicker;
import io.katniss218.krpg.core.items.PlayerJoinListener;
import io.katniss218.krpg.core.items.RPGItemCommand;
import io.katniss218.krpg.core.items.durability.PlayerItemDamageListener;
import io.katniss218.krpg.core.levels.XpCommand;
import io.katniss218.krpg.core.loottables.RPGLootTableCommand;
import io.katniss218.krpg.core.players.RPGPlayerDatabase;
import io.katniss218.krpg.core.shops.InventoryCloseListener;
import io.katniss218.krpg.core.shops.PlayerInteractEntityListener;
import io.katniss218.krpg.core.shops.RPGShopCommend;
import io.katniss218.krpg.core.shops.InventoryClickListener;
import io.katniss218.krpg.core.spawners.RPGSpawnerCommand;
import io.katniss218.krpg.core.spawners.RPGSpawnerDatabase;
import io.katniss218.krpg.core.spawners.RPGSpawnerTicker;
import io.katniss218.krpg.core.utils.ColorUtils;
import io.katniss218.krpg.core.utils.NoTrampleListener;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.milkbowl.vault.economy.Economy;

public final class KRPGCore extends JavaPlugin implements Listener
{
    private static KRPGCore PLUGIN_INSTANCE;
    private static Logger PLUGIN_LOGGER;

    public static KRPGCore getPlugin()
    {
        return PLUGIN_INSTANCE;
    }

    public static Logger getPluginLogger()
    {
        return PLUGIN_LOGGER;
    }

    private static String noPermissionMsg = "&cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is an error.";

    public static Component getNoPermissionMsg()
    {
        return ColorUtils.GetComponent( noPermissionMsg );
    }

    public static void ReloadRegistries()
    {
        RPGRarityRegistry.reload();
        RPGItemRegistry.reload();
        RPGLootTableRegistry.reload();
        RPGEntityRegistry.reload();
        RPGShopRegistry.reload();
    }

    public static void LoadDatabases()
    {
        getPluginLogger().info( "Loading databases..." );
        try
        {
            RPGPlayerDatabase.LoadFromDatabase();
        }
        catch( Exception ex )
        {
            getPluginLogger().warning( "Couldn't load the player database: " + ex.getMessage() );
        }

        try
        {
            RPGSpawnerDatabase.LoadFromDatabase();
        }
        catch( Exception ex )
        {
            getPluginLogger().warning( "Couldn't load the spawner database: " + ex.getMessage() );
        }
    }

    public static void SaveDatabases()
    {
        getPluginLogger().info( "Saving databases..." );
        try
        {
            RPGPlayerDatabase.SaveToDatabase();
        }
        catch( Exception ex )
        {
            getPluginLogger().warning( "Couldn't save the player database. " + ex.getMessage() );
        }

        try
        {
            RPGSpawnerDatabase.SaveToDatabase();
        }
        catch( Exception ex )
        {
            getPluginLogger().warning( "Couldn't save the spawner database. " + ex.getMessage() );
        }
    }

    private static Economy vaultEconomy = null;
    private static Chat vaultChat = null;

    private boolean setupVault()
    {
        if( getServer().getPluginManager().getPlugin( "Vault" ) == null )
        {
            return false;
        }

        RegisteredServiceProvider<Economy> rspEco = getServer().getServicesManager().getRegistration( Economy.class );
        if( rspEco == null )
        {
            return false;
        }
        vaultEconomy = rspEco.getProvider();

        RegisteredServiceProvider<Chat> rspChat = getServer().getServicesManager().getRegistration( Chat.class );
        if( rspChat == null )
        {
            return false;
        }
        vaultChat = rspChat.getProvider();

        return true;
    }

    void setupDependencies()
    {
        if( !setupVault() )
        {
            getLogger().severe( "Disabled due to no Vault dependency found!" );
            getServer().getPluginManager().disablePlugin( this );
        }
    }

    void registerEventListeners()
    {
        this.getServer().getPluginManager().registerEvents( this, this );
        this.getServer().getPluginManager().registerEvents( new PlayerJoinListener(), this );
        this.getServer().getPluginManager().registerEvents( new InventoryClickListener(), this );
        this.getServer().getPluginManager().registerEvents( new InventoryCloseListener(), this );
        this.getServer().getPluginManager().registerEvents( new PlayerInteractEntityListener(), this );
        this.getServer().getPluginManager().registerEvents( new EntityDamageListener(), this );
        this.getServer().getPluginManager().registerEvents( new EntityDamageByEntityListener(), this );
        this.getServer().getPluginManager().registerEvents( new EntityDeathListener(), this );
        this.getServer().getPluginManager().registerEvents( new PlayerItemDamageListener(), this );
        this.getServer().getPluginManager().registerEvents( new PlayerItemConsumeListener(), this );
        this.getServer().getPluginManager().registerEvents( new NoTrampleListener(), this );
        this.getServer().getPluginManager().registerEvents( new AsyncChatListener(), this );
    }

    void registerCommands()
    {
        this.getCommand( "rpgitem2" ).setExecutor( new RPGItemCommand() );
        this.getCommand( "rpgitem2" ).setTabCompleter( new RPGItemCommand() );

        this.getCommand( "rpgentity2" ).setExecutor( new RPGEntityCommand() );
        this.getCommand( "rpgentity2" ).setTabCompleter( new RPGEntityCommand() );

        this.getCommand( "rpgloottable2" ).setExecutor( new RPGLootTableCommand() );
        this.getCommand( "rpgloottable2" ).setTabCompleter( new RPGLootTableCommand() );

        this.getCommand( "rpgspawner2" ).setExecutor( new RPGSpawnerCommand() );
        this.getCommand( "rpgspawner2" ).setTabCompleter( new RPGSpawnerCommand() );

        this.getCommand( "rpgshop2" ).setExecutor( new RPGShopCommend() );
        this.getCommand( "rpgshop2" ).setTabCompleter( new RPGShopCommend() );

        this.getCommand( "xp" ).setExecutor( new XpCommand() );
        this.getCommand( "xp" ).setTabCompleter( new XpCommand() );
    }

    public static Economy getVaultEconomy()
    {
        return vaultEconomy;
    }
    public static Chat getVaultChat()
    {
        return vaultChat;
    }

    @Override
    public void onEnable()
    {
        PLUGIN_INSTANCE = JavaPlugin.getPlugin( KRPGCore.class );
        PLUGIN_LOGGER = this.getLogger();
        this.setupDependencies();
        this.saveDefaultConfig();
        ReloadRegistries();
        LoadDatabases();

        registerEventListeners();
        registerCommands();

        new AutoSaveDatabases().runTaskTimer( this, 0, 600 );

        // This is important to be loaded a few ticks later,
        // because multiverse core needs to load the world before we can load the spawner locations in that world.
        new AutoLoadDatabases().runTaskLater( this, 2 );

        new RPGSpawnerTicker.Ticker().runTaskTimer( this, 5, 20 * 15 );
        new RPGEntityTicker.Ticker().runTaskTimer( this, 5, 20 * 17 );
    }

    @Override
    public void onDisable()
    {
        SaveDatabases();
    }

    private static class AutoLoadDatabases extends BukkitRunnable
    {
        @Override
        public void run()
        {
            KRPGCore.LoadDatabases();
        }
    }

    private static class AutoSaveDatabases extends BukkitRunnable
    {
        @Override
        public void run()
        {
            KRPGCore.SaveDatabases();
        }
    }
}