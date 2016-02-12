package fr.bretzel.soulshard.config;

import fr.bretzel.soulshard.FileUtils;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public abstract class IConfig {

    private Configuration configuration;
    private File file;
    private boolean isLoaded = false;

    public IConfig(File file) {
        if (!file.exists()) {
            FileUtils.createNewFile(file);
        }

        this.file = file;
        this.configuration = new Configuration(file);
        this.configuration.load();
        this.isLoaded = true;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public File getFile() {
        return file;
    }

    public void load() {
        isLoaded = false;
        configuration.load();
        isLoaded = true;
    }

    public void reload() {
        isLoaded = false;
        configuration.save();
        configuration = null;
        configuration = new Configuration(file);
        configuration.load();
        isLoaded = true;
    }

    public boolean isLoaded() {
        return isLoaded && configuration != null;
    }
}
