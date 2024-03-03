package io.katniss218.krpg.core.definitions;

import io.katniss218.krpg.core.FileUtils;
import io.katniss218.krpg.core.KRPGCore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RPGEntityRegistry
{
    static HashMap<String, RPGEntityDef> entities = new HashMap<>();

    static final String DEFS_DIRECTORY_NAME = "defs";
    static final String ENTITIES_DIRECTORY_NAME = "rpgentities";
    static final String CONFIG_NODE = "rpgentities";

    @Nullable
    public static RPGEntityDef get( @Nonnull String id )
    {
        return entities.get( id );
    }

    public static List<String> getRegisteredIDs()
    {
        ArrayList<String> list = new ArrayList<>(entities.size());
        list.addAll( entities.keySet() );
        return list;
    }

    /**
     * Adds an entity definition to the registry. The added definition is persisted across restarts, and can be removed later.
     * @param def
     * @return
     */
    public static boolean add( @Nonnull RPGEntityDef def )
    {
        if( entities.containsKey( def.id ) )
        {
            return false;
        }

        entities.put( def.id, def );
        return true;
    }

    static void LoadEntitiesFromYamlFile( File file )
    {
        KRPGCore.getPluginLogger().info( "loading entities from " + file.getPath() );
        FileConfiguration config = YamlConfiguration.loadConfiguration( file );

        ConfigurationSection rpgEntities = config.getConfigurationSection( CONFIG_NODE );
        if( rpgEntities == null )
        {
            return;
        }

        Set<String> entityNodes = rpgEntities.getKeys( false );
        for( final var entityNode : entityNodes )
        {
            ConfigurationSection rpgEntity = config.getConfigurationSection( CONFIG_NODE + "." + entityNode );
            if( rpgEntity == null )
            {
                continue;
            }

            try
            {
                RPGEntityDef def = RPGEntityDef.fromConfig( rpgEntity );
                if( def == null )
                {
                    continue;
                }

                entities.put( def.id, def );
            }
            catch( Exception ex )
            {
                //
            }
        }
    }

    static void LoadItemsFromDirectoryRecursive( File directory )
    {
        File[] files = directory.listFiles();
        if( files == null )
        {
            return;
        }

        for( final var file : files )
        {
            if( file.isDirectory() )
            {
                LoadItemsFromDirectoryRecursive( file );
            }
            String ext = FileUtils.getExtension( file );
            if( ext != null && ext.equals( "yml" ) )
            {
                LoadEntitiesFromYamlFile( file );
            }
        }
    }

    public static void Reload()
    {
        String dirPath = KRPGCore.getPlugin().getDataFolder() + File.separator + DEFS_DIRECTORY_NAME + File.separator + ENTITIES_DIRECTORY_NAME;

        entities.clear();
        LoadItemsFromDirectoryRecursive( new File( dirPath ) );
    }
}
