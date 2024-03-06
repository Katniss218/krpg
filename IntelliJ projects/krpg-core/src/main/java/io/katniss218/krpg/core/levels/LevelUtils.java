package io.katniss218.krpg.core.levels;

import io.katniss218.krpg.core.players.RPGPlayerDatabase;
import io.katniss218.krpg.core.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

public class LevelUtils
{
    final static double xpLevelRatio = 1.25;

    /**
     * Calculates the xp required to level up to the specified level, from the level below.
     * @param level The target level.
     * @return The xp points required to level up from (level-1) to level.
     */
    public static double getXpToLevel( int level )
    {
        return Math.floor( 20 * Math.pow( xpLevelRatio, level-1) );
    }

    /**
     * Calculates the xp required to level up to the specified level, from the specified starting level.
     * @param level The target level.
     * @param startingLevel The starting level.
     * @return The xp points required to level up or down from (startingLevel) to level.
     */
    public static double getXpToLevelFrom( int level, int startingLevel )
    {
        if( level < 0 || startingLevel < 0 )
        {
            throw new IllegalArgumentException("Level and startingLevel must be > 0");
        }

        double acc = 0.0;
        if( startingLevel < level )
        {
            for( ; startingLevel < level; startingLevel++ )
            {
                acc += Math.floor( 20 * Math.pow( xpLevelRatio, startingLevel-1) );
            }
            return acc;
        }
        if( startingLevel > level )
        {
            for( ; startingLevel > level; level-- )
            {
                acc += Math.floor( 20 * Math.pow( xpLevelRatio, level-1) );
            }
            return acc;
        }
        return 0;
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    public static void AddXp( @Nonnull Player player, double xp )
    {
        var data = RPGPlayerDatabase.getOrDefault( player );
        data.setXp( data.getXp() + xp );

        if( xp > 0 )
        {
            var xpForLevel = getXpToLevel( data.getLevel() );
            player.sendMessage( ColorUtils.GetComponent( "&aYou've gained " + decimalFormat.format( xp ) + " xp. (" + decimalFormat.format( data.getXp() ) + " / " + decimalFormat.format( xpForLevel ) + ")" ) );
            player.playSound( player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.1f );
            while( data.getXp() >= xpForLevel )
            {
                data.setLevel( data.getLevel() + 1 );
                data.setXp( data.getXp() - xpForLevel );
                xpForLevel = getXpToLevel( data.getLevel() );

                player.playSound( player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.1f );

                Location playerLocation = player.getLocation();

                // Define the offset vector (2, 2, 2)
                double offsetX = 2.0;
                double offsetY = 2.0;
                double offsetZ = 2.0;

                // Define the extra parameter (0.5)
                float extra = 0.5f;

                // Spawn 100 flame particles with the specified offset and extra parameters
                for( int i = 0; i < 100; i++ )
                {
                    double x = playerLocation.getX() + (Math.random() - 0.5) * offsetX * 2;
                    double y = playerLocation.getY() + (Math.random() - 0.5) * offsetY * 2;
                    double z = playerLocation.getZ() + (Math.random() - 0.5) * offsetZ * 2;

                    player.getWorld().spawnParticle( Particle.FLAME, x, y, z, 1, 0, 0, 0, extra );
                }

                player.sendMessage( ColorUtils.GetComponent( "&a&lLevel up!" ) );
                player.sendMessage( ColorUtils.GetComponent( "&aYou're now level " + data.getLevel() +"." ) );
            }
            SyncPlayer( player, data.getXp(), data.getLevel() );
        }
        else
        {
            var xpForLevel = getXpToLevel( data.getLevel() - 1 );
            player.sendMessage( ColorUtils.GetComponent( "&aYou've lost " + decimalFormat.format( xp ) + " xp. (" + decimalFormat.format( data.getXp() ) + " / " + decimalFormat.format( xpForLevel ) + ")" ) );
            while( data.getXp() <= 0 )
            {
                data.setLevel( data.getLevel() - 1 );
                data.setXp( data.getXp() + xpForLevel );
                xpForLevel = getXpToLevel( data.getLevel() - 1 );

                player.sendMessage( ColorUtils.GetComponent( "&c&lLevel down." ) );
                player.sendMessage( ColorUtils.GetComponent( "&cYou're now level " + data.getLevel() +"." ) );
            }
            SyncPlayer( player, data.getXp(), data.getLevel() );
        }
    }

    private static void SyncPlayer( @Nonnull Player player, double xp, int level )
    {
        player.setLevel( level );

        double nextLevelExp = player.getExpToLevel();

        double perc = 0.5;

        player.setExp( (float)((nextLevelExp * perc) / nextLevelExp) );
    }
}