package fr.bretzel.soulshard.event;

import fr.bretzel.soulshard.item.SoulShardItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ToolTipEvent {

    public static final String TIER = StatCollector.translateToLocal("tooltip.soul_shard.tier");
    public static final String KILL = StatCollector.translateToLocal("tooltip.soul_shard.killCount");

    @SubscribeEvent
    public void onToolTipEvent(ItemTooltipEvent event) {
        if (event.itemStack.getItem() instanceof SoulShardItem && event.itemStack.hasTagCompound() && event.itemStack.getItemDamage() != SoulShardItem.EnumType.UNBOUND.getDamage()) {
            if (GuiScreen.isShiftKeyDown()) {
                event.toolTip.add(TIER + ": " + event.itemStack.getTagCompound().getInteger("Tier"));
                event.toolTip.add(KILL + ": " + event.itemStack.getTagCompound().getInteger("KillCount"));
            } else {
                event.toolTip.add(EnumChatFormatting.GOLD + "<HOLD SHIFT>");
            }
        }
    }

}
