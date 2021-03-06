package hexagonnico.buzzydrones.content.item;

import hexagonnico.buzzydrones.content.entity.DroneEntity;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class DroneItem extends Item {

	private final int type;

	public DroneItem(int type) {
		super(new Properties().stacksTo(1).tab(ItemGroup.TAB_MISC));
		this.type = type;
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		PlayerEntity playerEntity = context.getPlayer();
		if(!world.isClientSide && playerEntity != null) {
			DroneEntity droneEntity = new DroneEntity(world, this.type);
			BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
			context.getItemInHand().shrink(1);
			droneEntity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			if(context.getItemInHand().hasCustomHoverName()) {
				droneEntity.setCustomName(context.getItemInHand().getHoverName());
			}
			world.addFreshEntity(droneEntity);
			return ActionResultType.CONSUME;
		}
		return ActionResultType.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if(this.type == DroneEntity.BASIC) {
			tooltip.add(new TranslationTextComponent("item.buzzydrones.drone_basic_description").withStyle(TextFormatting.GRAY));
		} else if(this.type == DroneEntity.PICK_UP) {
			tooltip.add(new TranslationTextComponent("item.buzzydrones.drone_pick_up_description").withStyle(TextFormatting.GRAY));
		}
	}
}
