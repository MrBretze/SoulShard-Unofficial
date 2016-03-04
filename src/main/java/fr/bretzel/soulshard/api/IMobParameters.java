package fr.bretzel.soulshard.api;

import net.minecraft.nbt.NBTBase;

import java.util.Map;

public interface IMobParameters {

    Map<String, NBTBase> getCustomsTags();

}
