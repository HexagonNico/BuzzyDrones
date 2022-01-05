package hexagon.buzzydrones.common.block;

import javax.annotation.ParametersAreNonnullByDefault;

import hexagon.buzzydrones.common.blockentity.IdleStationBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IdleStationBlock extends BaseEntityBlock {
    
    public IdleStationBlock() {
        super(Properties.of(Material.METAL).strength(3.0f, 4.8f).sound(SoundType.METAL));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IdleStationBlockEntity(pos, state);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
