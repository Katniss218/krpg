package io.katniss218.krpg.core.combat;

import io.katniss218.krpg.core.definitions.RPGEntityDef;
import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.definitions.RPGLootTableDef;
import io.katniss218.krpg.core.definitions.RPGLootTableRegistry;
import io.katniss218.krpg.core.entities.RPGEntityData;
import io.katniss218.krpg.core.loottables.LootTableDropper;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener
{
    private void DropLootTable()
    {

    }

    private void Death( EntityDeathEvent event, Entity attacker, RPGEntityData attackerData, LivingEntity victim, RPGEntityData victimData, Projectile projectile )
    {
        if( victimData == null )
            return;

        Location location = event.getEntity().getLocation();

        RPGEntityDef def = RPGEntityRegistry.get( victimData.getID() );
        if( def == null )
        {
            return;
        }

        // Clear vanilla drops.
        event.setDroppedExp( 0 );
        event.getDrops().clear();

        if( def.lootTableId != null )
        {
            RPGLootTableDef defLoot = RPGLootTableRegistry.get( def.lootTableId );
            if( defLoot != null )
            {
                Player player = null;
                if( attacker instanceof Player p )
                    player = p;

                LootTableDropper.DropLootTable( defLoot, location, player );
            }
        }
    }

    @EventHandler
    public void onEntityDeath( EntityDeathEvent event )
    {
        LivingEntity victim = event.getEntity();

        Projectile projectile = null;
        Entity attacker = null;
        EntityDamageEvent e = victim.getLastDamageCause();
        if (e instanceof EntityDamageByEntityEvent)
        {
            attacker = ((EntityDamageByEntityEvent)e).getDamager();
        }

        if( attacker instanceof Projectile proj )
        {
            projectile = proj;
            var shooter = proj.getShooter(); // CraftSkeleton, CraftPlayer, etc.
            if( shooter instanceof Entity shooterEntity )
            {
                attacker = shooterEntity;
            }
        }

        if( victim instanceof Player )
        {
            if( attacker instanceof Player )
            {
                // player attacks player.
                Death( event, attacker, null, victim, null, projectile );
            }
            else
            {
                RPGEntityData attackerData = RPGEntityData.getFrom( attacker );
                if( attackerData != null )
                {
                    // rpgentity attacks player.
                    Death( event, attacker, attackerData, victim, null, projectile );
                }
            }
        }
        else
        {
            RPGEntityData victimData = RPGEntityData.getFrom( victim );
            if( victimData != null )
            {
                if( attacker instanceof Player )
                {
                    // player attacks rpgentity.
                    Death( event, attacker, null, victim, victimData, projectile );
                }
                else
                {
                    RPGEntityData attackerData = RPGEntityData.getFrom( attacker );
                    if( attackerData != null )
                    {
                        // rpgentity attacks rpgentity.
                        Death( event, attacker, attackerData, victim, victimData, projectile );
                    }
                }
            }
        }
        //Bukkit.broadcast( Component.text("damaged entity: " + event.getEntity().getName() ) );
        //var cause = event.getCause();
    }
}