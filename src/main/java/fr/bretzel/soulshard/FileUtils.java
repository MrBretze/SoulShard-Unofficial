package fr.bretzel.soulshard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void createNewFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            SoulShard.soulLog.warn(e.fillInStackTrace());
        }
    }

    public static MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer player, boolean bool) {
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;
        double d0 = player.posX;
        double d1 = player.posY + (double)player.getEyeHeight();
        double d2 = player.posZ;
        Vec3 vec3 = new Vec3(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0D;

        if(player instanceof EntityPlayerMP) {
            d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }

        Vec3 vec31 = vec3.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return world.rayTraceBlocks(vec3, vec31, bool, !bool, false);
    }
}
