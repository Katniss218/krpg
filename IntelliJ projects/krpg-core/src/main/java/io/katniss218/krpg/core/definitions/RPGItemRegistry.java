package io.katniss218.krpg.core.definitions;

import io.katniss218.krpg.core.FileUtils;
import io.katniss218.krpg.core.KRPGCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RPGItemRegistry
{
    static HashMap<String, RPGItemDef> items = new HashMap<>();

    static final String DEFS_DIRECTORY_NAME = "defs";
    static final String ITEMS_DIRECTORY_NAME = "rpgitems";
    static final String CONFIG_NODE = "rpgitems";

    @Nullable
    public static RPGItemDef get( @Nonnull String id )
    {
        return items.get( id );
    }

    public static List<String> getRegisteredIDs()
    {
        ArrayList<String> list = new ArrayList<>(items.size());
        list.addAll( items.keySet() );
        return list;
    }

    /**
     * Adds a item definition to the registry. The added definition is persisted across restarts, and can be removed later.
     * @param def
     * @return
     */
    public static boolean add( @Nonnull RPGItemDef def )
    {
        if( items.containsKey( def.id ) )
        {
            return false;
        }

        items.put( def.id, def );
        return true;
    }

    static void LoadItemsFromYamlFile( File file )
    {
        KRPGCore.getPluginLogger().info( "loading items from " + file.getPath() );
        KRPGCore.getPluginLogger().info( "loading items from " + file.getPath() );
        FileConfiguration config = YamlConfiguration.loadConfiguration( file );

        ConfigurationSection rpgItems = config.getConfigurationSection( CONFIG_NODE );
        if( rpgItems == null )
        {
            return;
        }

        Set<String> itemNodes = rpgItems.getKeys( false );
        for( final var itemNode : itemNodes )
        {
            ConfigurationSection rpgItem = config.getConfigurationSection( CONFIG_NODE + "." + itemNode );
            if( rpgItem == null )
            {
                continue;
            }

            RPGItemDef def = RPGItemDef.fromConfig( rpgItem );
            if( def == null )
            {
                continue;
            }

            items.put( def.id, def );
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
            if( ext.equals( "yml" ) )
            {
                LoadItemsFromYamlFile( file );
            }
        }
    }

    public static void ReloadItems()
    {
        KRPGCore plugin = JavaPlugin.getPlugin( KRPGCore.class );
        String dirPath = plugin.getDataFolder() + File.separator + DEFS_DIRECTORY_NAME + File.separator + ITEMS_DIRECTORY_NAME;

        items.clear();
        LoadItemsFromDirectoryRecursive( new File( dirPath ) );
    }
}
