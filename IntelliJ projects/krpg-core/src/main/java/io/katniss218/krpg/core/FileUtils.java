package io.katniss218.krpg.core;

import java.io.File;

public class FileUtils
{
    public static String getExtension( File file )
    {
        if( file.isDirectory() )
            return null;

        String[] split = file.getPath().split( "\\." );
        return split[split.length - 1];
    }
}
