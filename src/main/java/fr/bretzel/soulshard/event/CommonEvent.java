package fr.bretzel.soulshard.event;

import fr.bretzel.soulshard.MobMapping;
import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.Utils;
import fr.bretzel.soulshard.item.SoulShardItem;
import fr.bretzel.soulshard.registry.ItemRegistry;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonEvent
{

	@SubscribeEvent public void onEntityKill(LivingDeathEvent event)
	{

		World world = event.getEntityLiving().getEntityWorld();

		if (world.isRemote || (!(event.getEntityLiving() instanceof EntityLiving)) || (!(event.getSource()
				.getEntity() instanceof EntityPlayer)))
			return;

		EntityLiving dead = (EntityLiving) event.getEntityLiving();

		if (dead.getEntityData().getBoolean("IsSoulShard"))
			return;

		EntityPlayer player = (EntityPlayer) event.getSource().getEntity();

		String entName = EntityList.getEntityString(dead);

		if (entName == null || entName.isEmpty())
			return;

		if (MobMapping.isMobBlackListed(entName))
			return;

		ItemStack shardItem = SoulShardItem.getShardFromInventory(player, entName);

		if (shardItem != null)
		{
			if (!Utils.isBound(shardItem))
			{
				Utils.boundEntity(dead, shardItem);
			}

			int soulStealer = EnchantmentHelper
					.getEnchantmentLevel(ItemRegistry.soulStealer, player.getHeldItem(EnumHand.MAIN_HAND));
			SoulShardItem.increaseShardKillCount(shardItem, soulStealer + 1);
		}
	}

	public int getSoulBonnus(int level) {
		//Default option
		return level + 1;
	}
}
