package io.katniss218.krpg.core;

/**
 * Describes a set of modifiers for a specific parameter.
 */
public class ModifierSet
{
    private double primary;
    private double additionalFlat;
    private double additionalPercent;

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
}
