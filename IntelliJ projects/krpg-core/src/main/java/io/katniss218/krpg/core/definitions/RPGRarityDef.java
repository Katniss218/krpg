package io.katniss218.krpg.core.definitions;

import io.katniss218.krpg.core.KRPGCore;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RPGRarityDef
{
    private String displayName;

    private String primaryColor;
    private String secondaryColor;

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public String getPrimaryColor()
    {
        return primaryColor;
    }

    public void setPrimaryColor( String primaryColor )
    {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor()
    {
        return secondaryColor;
    }

    public void setSecondaryColor( String secondaryColor )
    {
        this.secondaryColor = secondaryColor;
    }

    @Nullable
    public static RPGRarityDef fromConfig( @Nonnull ConfigurationSection config )
    {
        try
        {
            RPGRarityDef rarity = new RPGRarityDef();
            rarity.setDisplayName( config.getString( "display.name" ) );
            rarity.setPrimaryColor( config.getString( "display.primary_color" ) );
            rarity.setSecondaryColor( config.getString( "display.secondary_color" ) );
            return rarity;
        }
        catch( Exception ex )
        {
            KRPGCore.getPluginLogger().warning( "Loading rarity definition failed: " + config.getCurrentPath() );
            return null;
        }
    }
}