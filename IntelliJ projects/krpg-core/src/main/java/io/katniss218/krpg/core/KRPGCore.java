package io.katniss218.krpg.core;

import java.util.logging.Logger;

import io.katniss218.krpg.core.combat.EntityDamageByEntityListener;
import io.katniss218.krpg.core.combat.EntityDamageListener;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.definitions.RPGRarityRegistry;
import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.entities.RPGEntityCommand;
import io.katniss218.krpg.core.items.RPGItemCommand;
import io.katniss218.krpg.core.items.durability.PlayerItemDamageListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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

    public static void ReloadRegistries()
    {
        RPGRarityRegistry.ReloadRarities();
        RPGItemRegistry.ReloadItems();
        //RPGLootTableRegistry.ReloadLootTable();
        RPGEntityRegistry.ReloadEntities();
    }

    void registerEventListeners(){

        this.getServer().getPluginManager().registerEvents( this, this );
        this.getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerItemDamageListener(), this);

    }

    @Override
    public void onEnable()
    {
        PLUGIN_INSTANCE = JavaPlugin.getPlugin( KRPGCore.class );
        PLUGIN_LOGGER = this.getLogger();
        this.saveDefaultConfig();
        ReloadRegistries();

        registerEventListeners();

        this.getCommand( "rpgitem2" ).setExecutor( new RPGItemCommand() );
        this.getCommand( "rpgitem2" ).setTabCompleter( new RPGItemCommand() );
        this.getCommand( "rpgentity2" ).setExecutor( new RPGEntityCommand() );
        this.getCommand( "rpgentity2" ).setTabCompleter( new RPGEntityCommand() );
    }
}