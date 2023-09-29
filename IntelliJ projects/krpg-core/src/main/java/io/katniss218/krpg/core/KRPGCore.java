package io.katniss218.krpg.core;

import java.util.Collection;
import java.util.logging.Logger;

import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.definitions.RPGRarityRegistry;
import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.entities.RPGEntityCommand;
import io.katniss218.krpg.core.items.RPGItemCommand;
import io.katniss218.krpg.core.nbt.TestCommand;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.EntityArgument.players;

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

    @Override
    public void onEnable()
    {
        PLUGIN_INSTANCE = JavaPlugin.getPlugin( KRPGCore.class );
        PLUGIN_LOGGER = this.getLogger();
        this.saveDefaultConfig();
        ReloadRegistries();

        this.getServer().getPluginManager().registerEvents( this, this );

        this.getCommand( "rpgtest" ).setExecutor( new TestCommand() );
        this.getCommand( "rpgitem2" ).setExecutor( new RPGItemCommand() );
        this.getCommand( "rpgitem2" ).setTabCompleter( new RPGItemCommand() );
        this.getCommand( "rpgentity2" ).setExecutor( new RPGEntityCommand() );
        this.getCommand( "rpgentity2" ).setTabCompleter( new RPGEntityCommand() );
    }
}