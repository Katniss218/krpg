package io.katniss218.krpg.core.spawners;

import io.katniss218.krpg.core.definitions.RPGEntityDef;
import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.entities.RPGEntityData;
import io.katniss218.krpg.core.entities.RPGEntityFactory;
import io.katniss218.krpg.core.loottables.LootTableDropper;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class RPGSpawnerTicker
{
    public static class Ticker extends BukkitRunnable
    {
        @Override
        public void run()
        {
            RPGSpawnerTicker.tickAll();
        }
    }

    public static void tickAll()
    {
        final var onlinePlayers = Bukkit.getOnlinePlayers();

        for( final var spawner : RPGSpawnerDatabase.getSpawners() )
        {
            if( !spawner.getLocation().isChunkLoaded() )
            {
                continue;
            }

            boolean inRange = false;
            for( final var player : onlinePlayers )
            {
                if( player.getLocation().distance( spawner.getLocation() ) < 80 )
                {
                    inRange = true;
                    break;
                }
            }

            if( inRange )
            {
                final var entities = spawner.getLocation().getNearbyEntities( 24, 24, 24 );

                boolean rpgEntityInRange = false;
                for( final var entity : entities )
                {
                    var rpgEntity = RPGEntityData.getFrom( entity );
                    if( rpgEntity != null )
                    {
                        rpgEntityInRange = true;
                        break;
                    }
                }

                if( !rpgEntityInRange )
                {
                    RPGEntityDef def = RPGEntityRegistry.get( spawner.getEntityId() );
                    if( def != null )
                    {
                        int count = LootTableDropper.randomRange( spawner.getMinCount(), spawner.getMaxCount() + 1 );
                        RPGEntityFactory.createEntities( def, spawner.getLocation(), count, null );
                    }
                }
            }
        }
    }
}
