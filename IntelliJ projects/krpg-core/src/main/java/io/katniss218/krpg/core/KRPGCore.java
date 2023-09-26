package io.katniss218.krpg.core;

import java.util.Collection;

import io.katniss218.krpg.core.nbt.TestCommand;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.EntityArgument.players;

public final class KRPGCore extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents( this, this );

        this.getCommand( "rpgtest" ).setExecutor( new TestCommand() );
    }
}