package fr.bretzel.soulshard;

import java.io.File;
import java.io.IOException;

/**
 * Created by MrBretzel on 12/02/2016.
 */
public class FileUtils {

    public static void createNewFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            SoulShard.soulLog.warn(e.fillInStackTrace());
        }
    }

}
