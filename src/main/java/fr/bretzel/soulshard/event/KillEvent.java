package fr.bretzel.soulshard.event;

import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.item.SoulShardItem;
import fr.bretzel.soulshard.registry.ItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KillEvent {

    @SubscribeEvent
    public void onEntityKill(LivingDeathEvent event) {

        World world = event.entity.getEntityWorld();

        if (world.isRemote || (!(event.entity instanceof EntityLiving)) || (!(event.source.getEntity() instanceof EntityPlayer)))
            return;

        EntityLiving dead = (EntityLiving) event.entity;

        if (dead.getEntityData().hasKey("SoulMob") && dead.getEntityData().getBoolean("SoulMob"))
            return;

        EntityPlayer player = (EntityPlayer) event.source.getEntity();

        String entName = SoulShard.mobMapping.getEntityType(dead);

        if (entName == null || entName.isEmpty())
            return;

        if (SoulShard.mobMapping.isMobBlackListed(entName))
            return;

        ItemStack shardItem = SoulShardItem.getShardFromInventory(player, entName);

        if (shardItem != null) {

            if (!SoulShardItem.isBound(shardItem)) {
                SoulShardItem.boundEntity(dead, shardItem);
            }

            int soulStealer = EnchantmentHelper.getEnchantmentLevel(ItemRegistry.soulStealer.effectId, player.getHeldItem());
            SoulShardItem.increaseShardKillCount(shardItem, soulStealer + 1);
        }
    }

}
