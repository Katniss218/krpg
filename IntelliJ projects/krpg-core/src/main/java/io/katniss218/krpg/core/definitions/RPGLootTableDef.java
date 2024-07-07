package io.katniss218.krpg.core.definitions;

import io.katniss218.krpg.core.items.RPGItemType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RPGLootTableDef
{
    public static class ItemEntry
    {
        @Nonnull
        public List<String> itemIds = new ArrayList<>();

        // prefix

        public int min = 1;
        public int max = 1;
        public double chance = 1.0;
    }

    public static class RangeEntry
    {
        public double min = 1;
        public double max = 1;
    }

    @Nonnull
    public final String id; // required

    public List<ItemEntry> items = null;
    public RangeEntry money = null;
    public RangeEntry xp = null;

    public RPGLootTableDef( @Nonnull String id )
    {
        this.id = id;
    }


    @Nullable
    public static RPGLootTableDef fromConfig( @Nonnull ConfigurationSection config )
    {
        String id = config.getName();

        RPGLootTableDef def = new RPGLootTableDef( id );

        List<String> items = config.getStringList( "items" );
        def.items = new ArrayList<ItemEntry>( items.size() );
        for( final var item : items )
        {
            String[] splitItems = item.split( "\\|" );
            if( splitItems.length != 4 )
                continue;
            try
            {
                ItemEntry entry = new ItemEntry();
                String[] splitCounts = splitItems[0].split( "-" );
                if( splitCounts.length == 1 )
                {
                    entry.min = Integer.parseUnsignedInt( splitCounts[0] );
                    entry.max = entry.min;
                }
                else if( splitCounts.length == 2 )
                {
                    entry.min = Integer.parseUnsignedInt( splitCounts[0] );
                    entry.max = Integer.parseUnsignedInt( splitCounts[1] );
                }
                else
                {
                    continue;
                }

                //entry.prefixList = splitItems[1];

                entry.itemIds.addAll( Arrays.asList( splitItems[2].split( "," ) ) );

                entry.chance = splitItems[3].endsWith( "%" )
                        ? Double.parseDouble( splitItems[3].substring( 0, splitItems[3].length() - 1 ) ) / 100
                        : Double.parseDouble( splitItems[3] );

                def.items.add( entry );
            }
            catch( Exception ex )
            {
                //
            }
        }

        String xpRange = config.getString( "xp" );
        if( xpRange != null )
        {
            def.xp = new RangeEntry();
            String[] splitXp = xpRange.split( "-" );
            if( splitXp.length == 1 )
            {
                def.xp.min = Integer.parseInt( splitXp[0] );
                def.xp.max = def.xp.min;
            }
            else if( splitXp.length == 2 )
            {
                def.xp.min = Integer.parseInt( splitXp[0] );
                def.xp.max = Integer.parseInt( splitXp[1] );
            }
        }

        String moneyRange = config.getString( "money" );
        if( moneyRange != null )
        {
            def.money = new RangeEntry();
            String[] splitMoney = moneyRange.split( "-" );
            if( splitMoney.length == 1 )
            {
                def.money.min = Integer.parseInt( splitMoney[0] );
                def.money.max = def.money.min;
            }
            else if( splitMoney.length == 2 )
            {
                def.money.min = Integer.parseInt( splitMoney[0] );
                def.money.max = Integer.parseInt( splitMoney[1] );
            }
        }

        return def;
    }
}