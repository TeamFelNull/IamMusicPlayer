package red.felnull.imp.container;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ObjectHolder;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.OtyacraftEngine;
import red.felnull.otyacraftengine.util.IKSGRegistryUtil;

import java.util.ArrayList;
import java.util.List;

public class IMPContainerTypes {
    public static List<ContainerType<?>> MOD_CONTAINERTYPE = new ArrayList<ContainerType<?>>();

    @ObjectHolder(IamMusicPlayer.MODID + ":" + "music_sharing_device")
    public static ContainerType<MusicSharingDeviceContainer> MUSIC_SHARING_DEVICE = register((windowId, inv, data) -> {
        return new MusicSharingDeviceContainer(windowId, inv, new Inventory(1), data.readBlockPos());
    }, new ResourceLocation(OtyacraftEngine.MODID, "music_sharing_device"));

    private static <T extends ContainerType<?>> T register(IContainerFactory<?> factory, ResourceLocation location) {
        T cont = IKSGRegistryUtil.createContainerType(factory, location);
        MOD_CONTAINERTYPE.add(cont);
        return cont;
    }


}
