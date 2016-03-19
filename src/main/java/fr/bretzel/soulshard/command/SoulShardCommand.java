package fr.bretzel.soulshard.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class SoulShardCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "soulshard";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "soulshard kill";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>();
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (strings.length > 0) {
            if (strings[0].equals("kill")) {
                int killCount = 0;

                for (Entity entity : iCommandSender.getEntityWorld().loadedEntityList) {
                    if (entity.getEntityData().getBoolean("IsSoulShard")) {
                        entity.setDead();
                        killCount++;
                    }
                }

                iCommandSender.addChatMessage(new TextComponentString("Soul mob killed: " + killCount));

            } else {
                iCommandSender.addChatMessage(new TextComponentString("Invalid argument"));
                return;
            }
        } else {
            iCommandSender.addChatMessage(new TextComponentString("/soulshard kill"));
            return;
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
}
