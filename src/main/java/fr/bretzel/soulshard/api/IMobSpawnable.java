package fr.bretzel.soulshard.api;

import net.minecraft.entity.EntityLiving;

public abstract class IMobSpawnable {

    private EntityLiving entityLiving;

    public IMobSpawnable(EntityLiving entityLiving) {
        this.entityLiving = entityLiving;
    }

    abstract IMobParameters getParameters();

    abstract void addParameters(String tag, Object object);
}
