package fr.bretzel.soulshard.config;

import fr.bretzel.soulshard.Utils;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public abstract class IConfig {

    private Configuration configuration;
    private File file;
    private boolean isLoaded = false;

    public IConfig(File file) {
        if (!file.exists()) {
            Utils.createNewFile(file);
        }

        this.file = file;
        this.configuration = new Configuration(file);
        this.configuration.load();
        this.isLoaded = true;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public File getFile() {
        return file;
    }
}
