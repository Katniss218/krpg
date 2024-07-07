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

public class RPGLootTableRegistry
{
    static HashMap<String, RPGLootTableDef> lootTables = new HashMap<>();

    static final String DEFS_DIRECTORY_NAME = "defs";
    static final String ENTITIES_DIRECTORY_NAME = "rpgloot_tables";
    static final String CONFIG_NODE = "rpgloot_tables";

    @Nullable
    public static RPGLootTableDef get( @Nonnull String id )
    {
        return lootTables.get( id );
    }

    public static List<String> getRegisteredIDs()
    {
        ArrayList<String> list = new ArrayList<>(lootTables.size());
        list.addAll( lootTables.keySet() );
        return list;
    }

    static void LoadLootTablesFromYamlFile( File file )
    {
        KRPGCore.getPluginLogger().info( "loading loot tables from " + file.getPath() );
        FileConfiguration config = YamlConfiguration.loadConfiguration( file );

        ConfigurationSection rpgLootTables = config.getConfigurationSection( CONFIG_NODE );
        if( rpgLootTables == null )
        {
            return;
        }

        Set<String> lootTableNodes = rpgLootTables.getKeys( false );
        for( final var lootTableNode : lootTableNodes )
        {
            ConfigurationSection rpgLootTable = config.getConfigurationSection( CONFIG_NODE + "." + lootTableNode );
            if( rpgLootTable == null )
            {
                continue;
            }

            try
            {
                RPGLootTableDef def = RPGLootTableDef.fromConfig( rpgLootTable );
                if( def == null )
                {
                    continue;
                }

                lootTables.put( def.id, def );
            }
            catch( Exception ex )
            {
                //
            }
        }
    }

    static void LoadLootTablesFromDirectoryRecursive( File directory )
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
                LoadLootTablesFromDirectoryRecursive( file );
            }
            String ext = FileUtils.getExtension( file );
            if( ext != null && ext.equals( "yml" ) )
            {
                LoadLootTablesFromYamlFile( file );
            }
        }
    }

    public static void reload()
    {
        String dirPath = KRPGCore.getPlugin().getDataFolder() + File.separator + DEFS_DIRECTORY_NAME + File.separator + ENTITIES_DIRECTORY_NAME;

        lootTables.clear();
        LoadLootTablesFromDirectoryRecursive( new File( dirPath ) );
    }
}
