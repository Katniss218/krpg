package io.katniss218.krpg.core.chat;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.levels.LevelUtils;
import io.katniss218.krpg.core.players.RPGPlayerData;
import io.katniss218.krpg.core.players.RPGPlayerDatabase;
import io.katniss218.krpg.core.utils.ColorUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener
{
    @EventHandler
    public void onChat( AsyncChatEvent event )
    {
        event.setCancelled( true );

        var vaultChat = KRPGCore.getVaultChat();

        Player player = event.getPlayer();

        var prefix = vaultChat.getPlayerPrefix( player );

        RPGPlayerData rpgPlayer = RPGPlayerDatabase.getOrDefault( player );
        int level = rpgPlayer.getLevel();

        String messageString = ((TextComponent)event.originalMessage()).content();

        Component pref = ColorUtils.GetComponent( " &8&k!&r" + prefix + "&8&k! &8&k!&r&2&l" + level + "&8&k! &r&7" );
        pref = pref.append( player.displayName() );
        pref = pref.append( ColorUtils.GetComponent( " &r&8• " ) );
        pref = pref.append( ColorUtils.GetComponent( "&r&7" + messageString ) );

        Bukkit.broadcast( pref );
    }
}