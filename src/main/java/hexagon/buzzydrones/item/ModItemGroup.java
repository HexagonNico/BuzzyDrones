package hexagon.buzzydrones.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup extends ItemGroup {

    public static final ItemGroup INSTANCE = new ModItemGroup();

    public ModItemGroup() {
        super(ItemGroup.getGroupCountSafe(), "buzzydrones");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.DRONE_ITEM);
    }
}
