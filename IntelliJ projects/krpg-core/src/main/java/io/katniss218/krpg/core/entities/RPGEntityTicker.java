package io.katniss218.krpg.core.entities;

import com.destroystokyo.paper.entity.Pathfinder;
import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.loottables.LootTableDropper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;

public class RPGEntityTicker
{
    public static class Ticker extends BukkitRunnable
    {
        @Override
        public void run()
        {
            RPGEntityTicker.tickAll();
        }
    }

    public static void tickAll()
    {
        var worlds = Bukkit.getWorlds();
        for( final var world : worlds )
        {
            if( world.getPlayerCount() == 0 )
            {
                continue;
            }

            var entities = world.getEntities();
            for( final var entity : entities )
            {
                if( entity instanceof Mob mob )
                {
                    Pathfinder pathfinder = mob.getPathfinder();
                    if( pathfinder.getCurrentPath() != null )
                        continue;

                    var rpgEntity = RPGEntityData.getFrom( entity );
                    if( rpgEntity != null )
                    {
                        double x = rpgEntity.spawnLocation.getX() + LootTableDropper.randomRange( -13, 13 );
                        double y = rpgEntity.spawnLocation.getY();
                        double z = rpgEntity.spawnLocation.getZ() + LootTableDropper.randomRange( -13, 13 );

                        Location pathLocation = new Location( world, x, y, z );
                        pathfinder.stopPathfinding();
                        pathfinder.moveTo( pathLocation );
                    }
                }
            }
        }
    }
}