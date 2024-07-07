package io.katniss218.krpg.core.definitions;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.items.RPGItemType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;

public class RPGShopDef
{
    @Nonnull
    public final String id; // required

    @Nonnull
    public String displayName = "name_not_assigned"; // required

    @Nonnull
    public HashSet<String> items = new HashSet<>();

    public RPGShopDef( @Nonnull String id, @Nonnull String displayName )
    {
        this.id = id;
        this.displayName = displayName;
    }

    @Nullable
    public static RPGShopDef fromConfig( @Nonnull ConfigurationSection config )
    {
        String id = config.getName();

        String displayName;
        try
        {
            displayName = config.getString( "display.name" );
        }
        catch( Exception ex )
        {
            KRPGCore.getPluginLogger().warning( "Loading shop definition failed. id: " + id );
            KRPGCore.getPluginLogger().info( ex.getMessage() );
            return null;
        }
        if( displayName == null )
        {
            return null;
        }

        RPGShopDef def = new RPGShopDef( id, displayName );

        if( config.contains( "items" ) )
        {
            try
            {
                var eqSection = config.getStringList( "items" );
                def.items = new HashSet<>( eqSection );
            }
            catch( Exception ex )
            {
            }
        }

        return def;
    }
}