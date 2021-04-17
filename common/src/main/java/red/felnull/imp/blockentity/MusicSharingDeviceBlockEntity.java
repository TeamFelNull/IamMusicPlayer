package red.felnull.imp.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import red.felnull.otyacraftengine.blockentity.container.IkisugiItemContainerBlockEntity;

public class MusicSharingDeviceBlockEntity extends IkisugiItemContainerBlockEntity {

    public MusicSharingDeviceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IMPBlockEntitys.MUSIC_SHARING_DEVICE, blockPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}
