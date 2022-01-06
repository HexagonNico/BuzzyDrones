package hexagonnico.buzzydrones.content.item;

import hexagonnico.buzzydrones.content.entity.DroneEntity;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class DroneItem extends Item {

	private final int type;

	public DroneItem(int type) {
		super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));
		this.type = type;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		if(!level.isClientSide && player != null) {
			DroneEntity droneEntity = new DroneEntity(level, this.type);
			BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
			context.getItemInHand().shrink(1);
			droneEntity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			if(context.getItemInHand().hasCustomHoverName()) {
				droneEntity.setCustomName(context.getItemInHand().getHoverName());
			}
			level.addFreshEntity(droneEntity);
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
		if(this.type == DroneEntity.BASIC) {
			tooltip.add(new TranslatableComponent("item.buzzydrones.drone_basic_description").withStyle(ChatFormatting.GRAY));
		} else if(this.type == DroneEntity.PICK_UP) {
			tooltip.add(new TranslatableComponent("item.buzzydrones.drone_pick_up_description").withStyle(ChatFormatting.GRAY));
		}
	}
}
