package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.container.CassetteDeckContainer;

public class CassetteDeckTileEntity extends IMPAbstractPAPLEquipmentTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private Screen currentScreen = Screen.OFF;

    public CassetteDeckTileEntity() {
        super(IMPTileEntityTypes.CASSETTE_DECK);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.cassette_deck");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new CassetteDeckContainer(id, player, this, getPos());
    }

    @Override
    public void readByIKSG(BlockState state, CompoundNBT tag) {
        super.readByIKSG(state, tag);
        this.currentScreen = Screen.getScreenByName(tag.getString("CurrentScreen"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putString("CurrentScreen", this.currentScreen.getName());
        return tag;
    }

    public void setScreen(Screen screen) {
        this.currentScreen = screen;
    }

    public Screen getScreen() {
        return currentScreen;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            if (isOn()) {
                if (currentScreen == Screen.OFF)
                    currentScreen = Screen.SELECTION;
            } else {
                if (currentScreen != Screen.OFF)
                    currentScreen = Screen.OFF;
            }
        }
    }

    @Override
    public CompoundNBT instructionFromClient(ServerPlayerEntity player, String s, CompoundNBT tag) {
        if (s.equals("Mode")) {
            setScreen(Screen.getScreenByName(tag.getString("name")));
        }
        return super.instructionFromClient(player, s, tag);
    }


    public enum Screen {
        OFF("off"),
        SELECTION("selection"),
        PLAY("play"),
        WRITE_1("write_1"),
        ERASE("erase"),
        COPY("copy");
        private final String name;

        private Screen(String name) {
            this.name = name;
        }

        public static Screen getScreenByName(String name) {
            for (Screen sc : values()) {
                if (sc.getName().equals(name))
                    return sc;
            }
            return OFF;
        }

        public String getName() {
            return name;
        }

        public ResourceLocation getTexLocation() {
            return new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/cassette_deck_screen/" + getName() + ".png");
        }
    }
}
