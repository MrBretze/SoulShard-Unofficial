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
		if (Utils.isBound(event.getItemStack()) && Utils.hasTagCompound(event.getItemStack())
				&& event.getItemStack().getItemDamage() != SoulShardItem.EnumType.UNBOUND.getDamage())
		{
			event.getToolTip().add(TIER + ": " + TextFormatting.WHITE + Utils.getTier(event.getItemStack()));
			event.getToolTip().add(KILL + ": " + TextFormatting.WHITE + Utils.getKillCount(event.getItemStack()));
		}
	}

	/*@SubscribeEvent public void onPlayerHasShardInHand(TickEvent.RenderTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();

		if (!mc.inGameHasFocus)
			return;

		if (mc.thePlayer.getHeldItemMainhand() != null || mc.thePlayer.getHeldItemOffhand() != null)
		{
			ItemStack stack = getStackInhHand(mc.thePlayer);

			if (stack == null)
				return;

			drawModel(stack, mc, 100, 100);
		}
	}

	private void drawString(String s, Minecraft mc, int x, int y, int size)
	{
		mc.ingameGUI.drawString(mc.fontRendererObj, s, x, y, size);
	}

	private void drawModel(ItemStack stack, Minecraft mc, int x, int y)
	{
		GL11.glEnable(3042);
		GL11.glPushMatrix();
		GL11.glBlendFunc(770, 771);
		GL11.glScalef(2, 2, 2);
		RenderHelper.enableStandardItemLighting();
		RenderItem renderItem = mc.getRenderItem();
		renderItem.renderItemAndEffectIntoGUI(stack, x, y);
		renderItem.renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y + 1, "");
		GL11.glDisable(2896);
		GL11.glPopMatrix();
	}

	public ItemStack getStackInhHand(EntityPlayer player)
	{
		if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof SoulShardItem)
			return player.getHeldItemMainhand();
		if (player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() instanceof SoulShardItem)
			return player.getHeldItemOffhand();

		return null;
	}*/
}
