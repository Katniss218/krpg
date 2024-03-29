package io.katniss218.krpg.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ColorUtils
{
    public static String GetSignColor( double val )
    {
        if( val < 0 )
            return "&c";
        if( val > 0 )
            return "&2";
        return "&6";
    }

    public static Component GetComponent( String text )
    {
         text = text
            .replace( "&0", "<black>" )
            .replace( "&1", "<dark_blue>" )
            .replace( "&2", "<dark_green>" )
            .replace( "&3", "<dark_aqua>" )
            .replace( "&4", "<dark_red>" )
            .replace( "&5", "<dark_purple>" )
            .replace( "&6", "<gold>" )
            .replace( "&7", "<gray>" )
            .replace( "&8", "<dark_gray>" )
            .replace( "&9", "<blue>" )
            .replace( "&a", "<green>" )
            .replace( "&b", "<aqua>" )
            .replace( "&c", "<red>" )
            .replace( "&d", "<light_purple>" )
            .replace( "&e", "<yellow>" )
            .replace( "&f", "<white>" )
            .replace( "&k", "<obfuscated>" )
            .replace( "&l", "<bold>" )
            .replace( "&m", "<strikethrough>" )
            .replace( "&n", "<underlined>" )
            .replace( "&o", "<italic>" )
            .replace( "&r", "<reset>" )
                 .replace( "&&", "&" );

        return MiniMessage.miniMessage().deserialize( "<reset>" + text );
    }
}