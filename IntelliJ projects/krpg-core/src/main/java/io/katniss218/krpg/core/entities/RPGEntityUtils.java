package io.katniss218.krpg.core.entities;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.katniss218.krpg.core.definitions.RPGEntityDef;
import io.katniss218.krpg.core.items.RPGItemData;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Objects;

public final class RPGEntityUtils
{
    /**
     * Creates a new entity at a given location in the world, and with the given data.
     *
     * @param def      the base definition of the entity.
     * @param location the location where the entity should appear.
     * @param count    the number of entities to create.
     * @param data     additional data about the entity.
     * @return the spawned entity (root if multipart).
     */
    @Nonnull
    @Contract( pure = true )
    public static Entity createEntity( @Nonnull RPGEntityDef def, Location location, int count, @Nullable RPGEntityData data )
    {
        if( data == null )
        {
            data = new RPGEntityData();
            data.setID( def.id );
            data.setHealth( def.maxHealth );
            data.setSpawnLocation( location );
            data.setSpawnTime( Instant.now() );
        }

        Entity entity = location.getWorld().spawnEntity( location, def.baseEntity, CreatureSpawnEvent.SpawnReason.CUSTOM );

        var nmsEntity = ((CraftEntity)entity).getHandle();

        var compound = new CompoundTag();
        nmsEntity.save( compound );

        // put default values here (e.g. zombies being adults by default, regardless of what is spawned by spawnEntity)
        compound.putBoolean( "IsVillager", false );
        compound.putBoolean( "IsBaby", false );
        compound.put( "HandItems", new ListTag() );
        compound.put( "ArmorItems", new ListTag() );
        var handDropChances = new ListTag();
        handDropChances.add( FloatTag.valueOf( 0.0F ) );
        handDropChances.add( FloatTag.valueOf( 0.0F ) );
        compound.put( "HandDropChances", handDropChances );
        var armorDropChances = new ListTag();
        armorDropChances.add( FloatTag.valueOf( 0.0F ) );
        armorDropChances.add( FloatTag.valueOf( 0.0F ) );
        armorDropChances.add( FloatTag.valueOf( 0.0F ) );
        armorDropChances.add( FloatTag.valueOf( 0.0F ) );
        compound.put( "ArmorDropChances", armorDropChances );

        // user data
        if( def.nbt != null )
        {
            try
            {
                var userNbt = net.minecraft.nbt.TagParser.parseTag( def.nbt );
                compound = userNbt.merge( compound ); // `a.merge(b)` replaces things in `a` with things in `b`, if duplicate.
            }
            catch( CommandSyntaxException e )
            {
            }
        }

        // after merging user data, replace the data that should always have a specific value.
        data.applyTo( compound );

        nmsEntity.load( compound );

        // set up data that doesn't require nbt.
        entity.customName( Component.text( "Lv." + def.level + " " + def.displayName ) ); // TODO - minimessage format colors
        entity.setPersistent( true );
        if( entity instanceof LivingEntity le )
        {
            var eq = le.getEquipment();
            if( eq != null )
            {
                // set up helmet to not burn in the sun.
                if( eq.getHelmet() == null )
                {
                    eq.setHelmet( new ItemStack( Material.STONE_BUTTON, 1 ) );
                }
            }
        }

        return entity;
    }
}