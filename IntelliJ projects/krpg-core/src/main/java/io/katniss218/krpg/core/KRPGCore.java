package io.katniss218.krpg.core;

import java.util.logging.Logger;

import io.katniss218.krpg.core.combat.EntityDamageByEntityListener;
import io.katniss218.krpg.core.combat.EntityDamageListener;
import io.katniss218.krpg.core.combat.EntityDeathListener;
import io.katniss218.krpg.core.combat.PlayerItemConsumeListener;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.definitions.RPGLootTableRegistry;
import io.katniss218.krpg.core.definitions.RPGRarityRegistry;
import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.entities.RPGEntityCommand;
import io.katniss218.krpg.core.items.RPGItemCommand;
import io.katniss218.krpg.core.items.durability.PlayerItemDamageListener;
import io.katniss218.krpg.core.levels.XpCommand;
import io.katniss218.krpg.core.loottables.RPGLootTableCommand;
import io.katniss218.krpg.core.players.RPGPlayerDatabase;
import io.katniss218.krpg.core.spawners.RPGSpawnerCommand;
import io.katniss218.krpg.core.spawners.RPGSpawnerDatabase;
import io.katniss218.krpg.core.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
        RPGRarityRegistry.Reload();
        RPGItemRegistry.Reload();
        RPGLootTableRegistry.Reload();
        RPGEntityRegistry.Reload();
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

    void registerEventListeners()
    {
        this.getServer().getPluginManager().registerEvents( this, this );
        this.getServer().getPluginManager().registerEvents( new EntityDamageListener(), this );
        this.getServer().getPluginManager().registerEvents( new EntityDamageByEntityListener(), this );
        this.getServer().getPluginManager().registerEvents( new EntityDeathListener(), this );
        this.getServer().getPluginManager().registerEvents( new PlayerItemDamageListener(), this );
        this.getServer().getPluginManager().registerEvents( new PlayerItemConsumeListener(), this );
    }

    @Override
    public void onEnable()
    {
        PLUGIN_INSTANCE = JavaPlugin.getPlugin( KRPGCore.class );
        PLUGIN_LOGGER = this.getLogger();
        this.saveDefaultConfig();
        ReloadRegistries();
        LoadDatabases();

        new AutoSaveDatabases().runTaskTimer( this, 0, 600 );
        new RPGSpawnerDatabase.Ticker().runTaskTimer( this, 0, 20 * 15 );

        registerEventListeners();

        this.getCommand( "rpgitem2" ).setExecutor( new RPGItemCommand() );
        this.getCommand( "rpgitem2" ).setTabCompleter( new RPGItemCommand() );
        this.getCommand( "rpgentity2" ).setExecutor( new RPGEntityCommand() );
        this.getCommand( "rpgentity2" ).setTabCompleter( new RPGEntityCommand() );
        this.getCommand( "rpgloottable2" ).setExecutor( new RPGLootTableCommand() );
        this.getCommand( "rpgloottable2" ).setTabCompleter( new RPGLootTableCommand() );
        this.getCommand( "rpgspawner2" ).setExecutor( new RPGSpawnerCommand() );
        this.getCommand( "rpgspawner2" ).setTabCompleter( new RPGSpawnerCommand() );
        this.getCommand( "xp" ).setExecutor( new XpCommand() );
        this.getCommand( "xp" ).setTabCompleter( new XpCommand() );
    }

    @Override
    public void onDisable()
    {
        SaveDatabases();
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