package fr.bretzel.soulshard.event;

import fr.bretzel.soulshard.Utils;
import fr.bretzel.soulshard.item.SoulShardItem;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEvent
{

	public static final String TIER = I18n.translateToLocal("tooltip.soul_shard.tier");
	public static final String KILL = I18n.translateToLocal("tooltip.soul_shard.killCount");

	@SubscribeEvent public void onToolTipEvent(ItemTooltipEvent event)
	{
		if (Utils.isBound(event.itemStack) && Utils.hasTagCompound(event.itemStack)
				&& event.itemStack.getItemDamage() != SoulShardItem.EnumType.UNBOUND.getDamage())
		{
			event.toolTip.add(TIER + ": " + TextFormatting.WHITE + Utils.getTier(event.itemStack));
			event.toolTip.add(KILL + ": " + TextFormatting.WHITE + Utils.getKillCount(event.itemStack));
		}
	}
}
