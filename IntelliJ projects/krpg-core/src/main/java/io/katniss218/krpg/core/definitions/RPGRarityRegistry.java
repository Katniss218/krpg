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
import java.util.List;
import java.util.Set;

public class RPGRarityRegistry
{
    static List<RPGRarityDef> rarities = new ArrayList<>();

    static final String DEFS_DIRECTORY_NAME = "defs";
    static final String ITEMS_DIRECTORY_NAME = "rpgrarities";
    static final String CONFIG_NODE = "rpgrarities";

    @Nullable
    public static RPGRarityDef get( int index )
    {
        return rarities.get( index );
    }

    public static int size()
    {
        return rarities.size();
    }

    public static void add( @Nonnull RPGRarityDef def )
    {
        rarities.add( def );
    }

    public static void add( int index, @Nonnull RPGRarityDef def )
    {
        rarities.add( index, def );
    }

    static void LoadRaritiesFromYamlFile( File file )
    {
        Bukkit.getLogger().info( "loading rarities from " + file.getPath() );
        System.out.println( "loading rarities from " + file.getPath() );
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

            try
            {
                RPGRarityDef def = RPGRarityDef.fromConfig( rpgItem );
                if( def == null )
                {
                    continue;
                }

                rarities.add( def );
            }
            catch( Exception ex )
            {
                //
            }
        }
    }

    static void LoadRaritiesFromDirectoryRecursive( File directory )
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
                LoadRaritiesFromDirectoryRecursive( file );
            }
            String ext = FileUtils.getExtension( file );
            if( ext.equals( "yml" ) )
            {
                LoadRaritiesFromYamlFile( file );
            }
        }
    }

    // todo - do stuff.
    public static void reload()
    {
        KRPGCore plugin = JavaPlugin.getPlugin( KRPGCore.class );
        String dirPath = plugin.getDataFolder() + File.separator + DEFS_DIRECTORY_NAME + File.separator + ITEMS_DIRECTORY_NAME;

        rarities.clear();
        LoadRaritiesFromDirectoryRecursive( new File( dirPath ) );
    }
}
