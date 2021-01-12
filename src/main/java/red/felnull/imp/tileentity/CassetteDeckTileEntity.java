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
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.imp.util.ItemHelper;

public class CassetteDeckTileEntity extends IMPAbstractPAPLEquipmentTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private PlayMusic writePlayMusic = PlayMusic.EMPTY;
    private Screen currentScreen = Screen.OFF;
    private int writeProgres;
    private int prevWriteProgres;
    private int writeProgresAll;

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
        this.writePlayMusic = new PlayMusic(tag.getString("WritePlayMusicUUID"), tag.getCompound("WritePlayMusic"));
        this.writeProgres = tag.getInt("WriteProgres");
        this.prevWriteProgres = tag.getInt("PrevWriteProgres");
        this.writeProgresAll = tag.getInt("WriteProgresAll");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putString("CurrentScreen", this.currentScreen.getName());
        tag.put("WritePlayMusic", writePlayMusic.write(new CompoundNBT()));
        tag.putString("WritePlayMusicUUID", writePlayMusic.getUUID());
        tag.putInt("WriteProgres", this.writeProgres);
        tag.putInt("PrevWriteProgres", this.prevWriteProgres);
        tag.putInt("WriteProgresAll", this.writeProgresAll);
        return tag;
    }

    public void setScreen(Screen screen) {
        this.currentScreen = screen;
    }

    public Screen getScreen() {
        return currentScreen;
    }

    public PlayMusic getWritePlayMusic() {
        return writePlayMusic;
    }

    public int getWriteProgres() {
        return writeProgres;
    }

    public int getWriteProgresAll() {
        return writeProgresAll;
    }

    public int getPrevWriteProgres() {
        return prevWriteProgres;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            if (isOn()) {
                if (currentScreen == Screen.OFF)
                    currentScreen = Screen.SELECTION;

                if ((currentScreen == Screen.WRITE_1 || currentScreen == Screen.WRITE_2) && getPAntenna().isEmpty())
                    currentScreen = Screen.WRITE_NO_ANTENNA;

                if (currentScreen == Screen.WRITE_NO_ANTENNA && !getPAntenna().isEmpty())
                    currentScreen = Screen.WRITE_1;

                if (currentScreen == Screen.WRITE_2) {
                    if (getCassetteTape().isEmpty())
                        currentScreen = Screen.WRITE_1;
                    writeProgresAll = 20;

                    if (writeProgres >= writeProgresAll) {
                        writeCassetteTape();
                        currentScreen = Screen.WRITE_1;
                    }

                    if (writeProgres < writeProgresAll)
                        writeProgres++;

                    prevWriteProgres = writeProgres;

                    if (prevWriteProgres < writeProgresAll)
                        prevWriteProgres++;

                } else {
                    writeProgres = 0;
                    prevWriteProgres = 0;
                }
            } else {
                if (currentScreen != Screen.OFF)
                    currentScreen = Screen.OFF;

                writeProgres = 0;
                prevWriteProgres = 0;
            }
        }
    }

    private void writeCassetteTape() {
        setCassetteTape(ItemHelper.writtenCassetteTape(getCassetteTape(), getWritePlayMusic()));
    }

    public ItemStack getCassetteTape() {
        return getStackInSlot(1);
    }

    public void setCassetteTape(ItemStack stack) {
        getItems().set(1, stack);
    }

    @Override
    public CompoundNBT instructionFromClient(ServerPlayerEntity player, String s, CompoundNBT tag) {
        if (s.equals("Mode")) {
            setScreen(Screen.getScreenByName(tag.getString("name")));
        } else if (s.equals("PlayMusicSet")) {
            setWritePlayMusic(tag.getString("UUID"));
        }
        return super.instructionFromClient(player, s, tag);
    }

    public void setWritePlayMusic(String UUID) {
        this.writePlayMusic = PlayMusic.getPlayMusicByUUID(UUID);
    }

    public enum Screen {
        OFF("off"),
        SELECTION("selection"),
        PLAY("play"),
        WRITE_1("write_1"),
        WRITE_2("write_2"),
        ERASE("erase"),
        COPY("copy"),
        WRITE_NO_ANTENNA("write_no_antenna");
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
