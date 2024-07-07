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

public class RPGShopRegistry
{
    static HashMap<String, RPGShopDef> shops = new HashMap<>();

    static final String DEFS_DIRECTORY_NAME = "defs";
    static final String ENTITIES_DIRECTORY_NAME = "rpgshops";
    static final String CONFIG_NODE = "rpgshops";

    @Nullable
    public static RPGShopDef get( @Nonnull String id )
    {
        return shops.get( id );
    }

    public static List<String> getRegisteredIDs()
    {
        ArrayList<String> list = new ArrayList<>(shops.size());
        list.addAll( shops.keySet() );
        return list;
    }

    /**
     * Adds a shop definition to the registry. The added definition is persisted across restarts, and can be removed later.
     * @param def
     * @return
     */
    public static boolean add( @Nonnull RPGShopDef def )
    {
        if( shops.containsKey( def.id ) )
        {
            return false;
        }

        shops.put( def.id, def );
        return true;
    }

    static void LoadShopsFromYamlFile( File file )
    {
        KRPGCore.getPluginLogger().info( "loading shops from " + file.getPath() );
        FileConfiguration config = YamlConfiguration.loadConfiguration( file );

        ConfigurationSection rpgShops = config.getConfigurationSection( CONFIG_NODE );
        if( rpgShops == null )
        {
            return;
        }

        Set<String> shopNodes = rpgShops.getKeys( false );
        for( final var shopNode : shopNodes )
        {
            ConfigurationSection rpgShop = config.getConfigurationSection( CONFIG_NODE + "." + shopNode );
            if( rpgShop == null )
            {
                continue;
            }

            try
            {
                RPGShopDef def = RPGShopDef.fromConfig( rpgShop );
                if( def == null )
                {
                    continue;
                }

                shops.put( def.id, def );
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
                LoadShopsFromYamlFile( file );
            }
        }
    }

    public static void reload()
    {
        String dirPath = KRPGCore.getPlugin().getDataFolder() + File.separator + DEFS_DIRECTORY_NAME + File.separator + ENTITIES_DIRECTORY_NAME;

        shops.clear();
        LoadItemsFromDirectoryRecursive( new File( dirPath ) );
    }
}
