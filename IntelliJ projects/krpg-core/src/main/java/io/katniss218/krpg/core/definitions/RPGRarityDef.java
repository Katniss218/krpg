package io.katniss218.krpg.core.definitions;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RPGRarityDef
{
    private String displayName;

    private TextColor primaryColor;
    private TextColor secondaryColor;

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public TextColor getPrimaryColor()
    {
        return primaryColor;
    }

    public void setPrimaryColor( TextColor primaryColor )
    {
        this.primaryColor = primaryColor;
    }

    public TextColor getSecondaryColor()
    {
        return secondaryColor;
    }

    public void setSecondaryColor( TextColor secondaryColor )
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
            rarity.setPrimaryColor( TextColor.fromCSSHexString( config.getString( "display.primary_color" ) ) );
            rarity.setSecondaryColor( TextColor.fromCSSHexString( config.getString( "display.secondary_color" ) ) );
            return rarity;
        }
        catch( Exception ex )
        {
            Bukkit.getLogger().warning( "Loading rarity definition failed: " + config.getCurrentPath() );
            return null;
        }
    }
}