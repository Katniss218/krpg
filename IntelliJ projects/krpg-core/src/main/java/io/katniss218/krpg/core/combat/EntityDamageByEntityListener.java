package io.katniss218.krpg.core.combat;

import io.katniss218.krpg.core.PhysicalDamageType;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.entities.RPGEntityData;
import io.katniss218.krpg.core.Stats;
import io.katniss218.krpg.core.items.RPGItemData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageByEntityListener implements Listener
{
    private void Hit( EntityDamageByEntityEvent event, Entity attacker, RPGEntityData attackerData, Entity victim, RPGEntityData victimData, Projectile projectile )
    {
        Stats attackerStats = Stats.getFrom( attacker );
        Stats victimStats = Stats.getFrom( victim );

        if( attackerStats == null || victimStats == null )
        {
            return;
        }

        double damageTaken = DamageCalculator.getDamageTaken( attackerStats, victimStats );

        event.setDamage( damageTaken );

        if( attacker instanceof Player player )
        {
            if( victim instanceof LivingEntity lv )
            {
                player.sendActionBar( Component.text( " -" + damageTaken + "HP (" + lv.getHealth() + " / " + victimStats.maxHealth +")" ) );
            }
        }

        // entities use their base values from entity id.
        // players use their combined equipment values + skill values.
        Bukkit.broadcast( Component.text( "attacker: " + attacker.getName() ) );
        Bukkit.broadcast( Component.text( "victim: " + victim.getName() ) );
    }

    private void Explode()
    {

    }

    @EventHandler
    public void onEntityDamage( EntityDamageByEntityEvent event )
    {
        Entity victim = event.getEntity();
        Entity attacker = event.getDamager(); // can be arrow, etc.
        Projectile projectile = null;

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
                Hit( event, attacker, null, victim, null, projectile );
            }
            else
            {
                RPGEntityData attackerData = RPGEntityData.getFrom( attacker );
                if( attackerData != null )
                {
                    // rpgentity attacks player.
                    Hit( event, attacker, attackerData, victim, null, projectile );
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
                    Hit( event, attacker, null, victim, victimData, projectile );
                }
                else
                {
                    RPGEntityData attackerData = RPGEntityData.getFrom( attacker );
                    if( attackerData != null )
                    {
                        // rpgentity attacks rpgentity.
                        Hit( event, attacker, attackerData, victim, victimData, projectile );
                    }
                }
            }
        }
    }
}