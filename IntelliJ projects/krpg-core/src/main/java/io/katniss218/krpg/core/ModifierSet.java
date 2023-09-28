package io.katniss218.krpg.core;

import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

/**
 * Describes a set of modifiers for a specific parameter.
 */
public class ModifierSet
{
    private double primary = 0;
    private double additionalFlat = 0;
    private double additionalPercent = 0;

    public boolean isValid()
    {
        return primary != 0 || additionalFlat != 0 || additionalPercent != 0;
    }

    /**
     * Gets the primary (main) modifier. It is displayed as the main modifier when looking at the item.
     */
    public double getPrimary()
    {
        return primary;
    }

    /**
     * Sets the primary (main) modifier. It is displayed as the main modifier when looking at the item.
     */
    public void setPrimary( double primary )
    {
        this.primary = primary;
    }

    /**
     * Gets the additional modifier (flat addition). It is displayed as `+num xyz` when looking at the item.
     */
    public double getAdditionalFlat()
    {
        return additionalFlat;
    }

    /**
     * Sets the additional modifier (flat addition). It is displayed as `+num xyz` when looking at the item.
     */
    public void setAdditionalFlat( double additionalFlat )
    {
        this.additionalFlat = additionalFlat;
    }

    /**
     * Gets the additional modifier (percentage addition). It is displayed as `+num% xyz` when looking at the item.
     */
    public double getAdditionalPercent()
    {
        return additionalPercent;
    }

    /**
     * Sets the additional modifier (percentage addition). It is displayed as `+num% xyz` when looking at the item.
     */
    public void setAdditionalPercent( double additionalPercent )
    {
        this.additionalPercent = additionalPercent;
    }

    @Nullable
    public static ModifierSet getModifierSet( ConfigurationSection configSection, String name )
    {
        ModifierSet ms = new ModifierSet();
        if( configSection.contains( name + ".primary" ) )
        {
            ms.setPrimary( configSection.getDouble( name + ".primary" ) );
        }
        if( configSection.contains( name + ".additional_flat" ) )
        {
            ms.setAdditionalFlat( configSection.getDouble( name + ".additional_flat" ) );
        }
        if( configSection.contains( name + ".additional_percent" ) )
        {
            ms.setAdditionalPercent( configSection.getDouble( name + ".additional_percent" ) );
        }
        if( ms.isValid() )
        {
            return ms;
        }
        return null;
    }
}
