package io.katniss218.krpg.core.combat;

import io.katniss218.krpg.core.PhysicalDamageType;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.entities.RPGEntityData;
import io.katniss218.krpg.core.Stats;
import io.katniss218.krpg.core.items.RPGItemData;
import io.katniss218.krpg.core.utils.ColorUtils;
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

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

public class EntityDamageByEntityListener implements Listener
{
    private static String Bar( int length, float fillPercent, @Nonnull String color1, @Nonnull String color2 )
    {
        int count = (int)Math.floor( fillPercent * length );
        if( count == 0 )
            count = 1;
        return color1 + "|".repeat( count ) + color2 + "|".repeat( length - count );
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.#");

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
            if( victim instanceof LivingEntity livingVictim )
            {
                double health = livingVictim.getHealth() - damageTaken;
                double maxHealth = victimStats.maxHealth; // lv.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                float perc = (float)(health / maxHealth);
                if( perc < 0 )
                    perc = 0;
                else if( perc > 1 )
                    perc = 1;
                String bar = Bar( 20, perc, "&c", "&8" );

                player.sendActionBar( ColorUtils.GetComponent( " &c-" + decimalFormat.format( damageTaken ) + "HP &8&l--- &r" + bar + " &8&l--- &8(&c" + decimalFormat.format( health ) + " &8/ &c" + decimalFormat.format( maxHealth ) + "&8)" ) );
            }
        }

        // entities use their base values from entity id.
        // players use their combined equipment values + skill values.
        //Bukkit.broadcast( Component.text( "attacker: " + attacker.getName() ) );
        //Bukkit.broadcast( Component.text( "victim: " + victim.getName() ) );
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