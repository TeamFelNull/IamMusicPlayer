package red.felnull.imp.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.SoundfileUploaderBlock;
import red.felnull.imp.client.file.ClientSoundFileSender;
import red.felnull.imp.container.SoundFileUploaderContainer;
import red.felnull.imp.file.ClientFileReceiver;
import red.felnull.imp.file.PlayList;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.SoundFileUploaderMessage;
import red.felnull.imp.tileentity.SoundFileUploaderTileEntity;
import red.felnull.imp.util.FileHelper;
import red.felnull.imp.util.PlayerHelper;
import red.felnull.imp.util.StringHelper;

import java.io.File;
import java.nio.file.Paths;

public class SoundFileUploaderScreen extends ContainerScreen<SoundFileUploaderContainer> {
    protected static final ResourceLocation SFU_GUI_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID,
            "textures/gui/container/soundfile_uploader.png");
    protected static final ResourceLocation SFU_GUI_TEXTURE2 = new ResourceLocation(IamMusicPlayer.MODID,
            "textures/gui/container/soundfile_uploader_2.png");
    private static Minecraft mc = Minecraft.getInstance();

    private boolean k8y = false;//9
    private boolean k1y = false;//2
    private boolean k0y = false;//11
    private boolean k1d = false;//2
    private boolean k9d = false;//10
    private boolean k4d = false;//5

    private int page;

    private File[] files;

    private File selectFile;

    private ServerFileSelectTarget serverSelectTargetR;

    private FaseStringImageButton fuleb1;
    private FaseStringImageButton fuleb2;
    private FaseStringImageButton fuleb3;
    private FaseStringImageButton fuleb4;
    private FaseStringImageButton fuleb5;
    private FaseStringImageButton fuleb6;
    private FaseStringImageButton fuleb7;
    private FaseStringImageButton fuleb8;

    private Button openfolder;
    private Button back;
    private Button next;
    private Button reload;
    private Button exit;

    private Button updatericon;
    private Button servericon;

    private Button uploadstart;
    private Button uploadno;

    private Button serverselecttarget;

    private Button uploadtarget;

    private Button editno;
    private Button editremove;
    private Button editdownload;

    public SoundFileUploaderScreen(SoundFileUploaderContainer screenContainer, PlayerInventory inv,
                                   ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        serverSelectTargetR = ServerFileSelectTarget.MAIN;

        this.ySize = 217;
        this.xSize = 215;
    }

    @Override
    public void func_231160_c_() {
        super.func_231160_c_();
        if (hasWindows(SoundFileUploaderWindwos.UPLOAD_FILE) || hasWindows(SoundFileUploaderWindwos.UPLOAD_COFIN)
                || hasWindows(SoundFileUploaderWindwos.EDIT_FILE)) {

            if (mc.world.getTileEntity(this.container.pos) instanceof SoundFileUploaderTileEntity) {
                SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) mc.world
                        .getTileEntity(this.container.pos);
                if (sfit.getUsePlayerUUID().equals(PlayerHelper.getUUID(mc.player))
                        && !sfit.getFliePath().equals("null") && !sfit.getFliePath().isEmpty()) {
                    this.selectFile = Paths.get(sfit.getFliePath()).toFile();
                }

            } else {
                return;
            }
        }
        if (mc.world.getTileEntity(this.container.pos) instanceof SoundFileUploaderTileEntity) {
            SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) mc.world
                    .getTileEntity(this.container.pos);

            if (sfit.playerstager.containsKey(PlayerHelper.getUUID(mc.player))) {

                serverSelectTargetR = ServerFileSelectTarget
                        .valueOf(sfit.playerstager.get(PlayerHelper.getUUID(mc.player)).toUpperCase());

            }
        }
        filesUpdate();
        serverFilelistUpdate();
        super.func_231160_c_();
        page = 1;
        k8y = false;
        k1y = false;
        k0y = false;
        k1d = false;
        k9d = false;
        k4d = false;
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        this.func_230480_a_(new ImageButton(xs + 181, ys + 181, 20,
                20, 215, 0, 20, SFU_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
            sendSFUPacket(0);
            sendSFUPacket(6);
            sendSFUPacket(8);
        }, new StringTextComponent(I18n.format("narrator.button.power"))));
        fristNoneIcons();
        fristMusicFiles();
        fristFileUpload();
        fritstUploadCofin();
        fritstEditFile();
        exit = new ImageButton(xs + 17 + 180 - 8, ys + 27, 8,
                8, 229, 24, 8, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            page = 1;
            sendSFUPacket(3);
            sendSFUPacket(6);
            sendSFUPacket(8);
        }, new StringTextComponent(I18n.format("narrator.button.exit")));

        this.func_230480_a_(exit);
        exit.field_230694_p_ = false;
    }

    private boolean isUsingMain() {

        if (!(mc.world.getTileEntity(this.container.pos) instanceof SoundFileUploaderTileEntity)) {

            return false;
        }
        SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) mc.world
                .getTileEntity(this.container.pos);

        return sfit.getUsePlayerUUID().equals(PlayerHelper.getUUID(mc.player));
    }

    private void filesUpdate() {
        page = 1;
        files = PlayList.getClientSoundFileList();

    }

    private void serverFilelistUpdate() {
        page = 1;

        if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
            sendSFUPacket(10);
        } else if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
            sendSFUPacket(11);
        }

    }

    @Override
    public void func_230430_a_(MatrixStack maxt, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.func_230446_a_(maxt);
        super.func_230430_a_(maxt, p_render_1_, p_render_2_, p_render_3_);
        this.func_230459_a_(maxt, p_render_1_, p_render_2_);
    }


    @Override
    public boolean func_231043_a_(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        if (hasWindows(SoundFileUploaderWindwos.CLIENT_FILESELECT)) {
            if (!(getMaxPage(files) == 0 || getMaxPage(files) == 1)) {
                this.page = page - (int) p_mouseScrolled_5_;
                this.page = MathHelper.clamp(page, 1, getMaxPage(files));
            }
        } else if (hasWindows(SoundFileUploaderWindwos.SERVER_FILESELECT)) {
            if (!(getMaxPage(PlayList.worldPlayList) == 0 || getMaxPage(PlayList.worldPlayList) == 1)) {
                this.page = page - (int) p_mouseScrolled_5_;
                this.page = MathHelper.clamp(page, 1, getMaxPage(PlayList.worldPlayList));
            }
        }
        return false;

    }

    @Override
    protected void func_230450_a_(MatrixStack maxt, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(SFU_GUI_TEXTURE);
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(maxt, xs, ys, 0, 0, this.xSize, this.ySize);
        RenderSystem.popMatrix();

        if (mc.world.getBlockState(this.container.pos).get(SoundfileUploaderBlock.ON)) {
            RenderSystem.pushMatrix();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.getMinecraft().getTextureManager().bindTexture(mc.world.getBlockState(this.container.pos)
                    .get(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_MONITOR).getResourceLocation());
            AbstractGui.func_238463_a_(maxt, (this.field_230708_k_ - this.xSize) / 2 + 7, (this.field_230709_l_ - this.xSize) / 2 + 6, 0, 0, 201, 124, 201,
                    124);
            RenderSystem.popMatrix();
            if (hasWindows(SoundFileUploaderWindwos.CLIENT_FILESELECT)) {
                drawUploader(maxt);
            } else if (hasWindows(SoundFileUploaderWindwos.UPLOAD_FILE)) {
                drawFileUpload(maxt);
            } else if (hasWindows(SoundFileUploaderWindwos.NO_ANTENA)) {
                drawErrorAntena(maxt);
            } else if (hasWindows(SoundFileUploaderWindwos.SERVER_FILESELECT)) {
                drawServerFileselect(maxt);
            } else if (hasWindows(SoundFileUploaderWindwos.UPLOAD_COFIN)) {
                drawUploadCofin(maxt);
            } else if (hasWindows(SoundFileUploaderWindwos.EDIT_FILE)) {
                drawEditFile(maxt);
            }
        }

    }

    public void fritstEditFile() {
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        editno = new StringImageButton(xs + 18, ys + 99, 60,
                12, 181, 108, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            sendSFUPacket(9);
        }, new StringTextComponent(I18n.format("gui.back")), -17, false);

        editno.field_230694_p_ = false;
        this.func_230480_a_(editno);

        editremove = new StringImageButton(xs + 17 + 180 - 60, ys + 99, 60,
                12, 181, 132, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            sendSFUPacket(18, this.selectFile.getParentFile().getName() + ":" + this.selectFile.getName());
            serverFilelistUpdate();
            sendSFUPacket(9);
        }, new StringTextComponent(I18n.format("selectWorld.delete")), -17, false);
        editremove.field_230694_p_ = false;
        this.func_230480_a_(editremove);

        editdownload = new StringImageButton(xs + 17 + 180 - 60 - 59, ys + 99, 59,
                12, 181, 156, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {

            if (!ClientFileReceiver.isReceving(this.selectFile)) {
                sendSFUPacket(16, this.selectFile.toString());
            } else {
                sendSFUPacket(17, String.valueOf(ClientFileReceiver.getReceivingId(this.selectFile)));

            }

        }, new StringTextComponent(I18n.format("sfu.download")), -17, false);
        editdownload.field_230694_p_ = false;
        this.func_230480_a_(editdownload);

    }

    public void updateEditFile() {

        if (hasWindows(SoundFileUploaderWindwos.EDIT_FILE)) {

            if (!(mc.world.getTileEntity(this.container.pos) instanceof SoundFileUploaderTileEntity)) {
                return;
            }

            SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) mc.world
                    .getTileEntity(this.container.pos);

            if (sfit.getUsePlayerUUID().equals(PlayerHelper.getUUID(mc.player))) {
                serverFilelistUpdate();
                editno.field_230694_p_ = true;
                if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
                    editremove.field_230694_p_ = true;
                } else if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                    if (PlayList.worldPlayListUUIDs.containsKey(selectFile)
                            && PlayList.worldPlayListUUIDs.get(selectFile).equals(PlayerHelper.getUUID(mc.player))) {
                        editremove.field_230694_p_ = true;
                    } else {
                        editremove.field_230694_p_ = false;
                    }

                }

                editdownload.field_230694_p_ = true;

                if (ClientFileReceiver.isReceving(this.selectFile)) {
                    ((StringImageButton) editdownload).dredclos = true;
                    editdownload.func_238482_a_(new StringTextComponent(I18n.format("sfu.stop")));
                } else {
                    ((StringImageButton) editdownload).dredclos = false;
                    editdownload.func_238482_a_(new StringTextComponent(I18n.format("sfu.download")));
                }
            } else {
                editno.field_230694_p_ = false;
                editremove.field_230694_p_ = false;
                editdownload.field_230694_p_ = false;

            }

        } else {
            editno.field_230694_p_ = false;
            editremove.field_230694_p_ = false;
            editdownload.field_230694_p_ = false;

        }

    }

    public void drawEditFile(MatrixStack maxt) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(maxt, xs + 17, ys + 27, 0, 0, 181, 85);
        RenderSystem.popMatrix();

        if (!(mc.world.getTileEntity(this.container.pos) instanceof SoundFileUploaderTileEntity)) {
            return;
        }

        SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) mc.world
                .getTileEntity(this.container.pos);


        if (sfit.getUsePlayerUUID().equals(PlayerHelper.getUUID(mc.player))) {
            if (selectFile != null && PlayList.isExistsWorldPlaylistFile(selectFile)) {
                this.drawCenterStrig(maxt, new StringTextComponent(selectFile.getName()), this.field_230708_k_ / 2, ys + 27 + 12, 16777215);

                if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE
                        && PlayList.worldPlayListNamesMap.containsKey(selectFile)
                        && !PlayList.worldPlayListNamesMap.get(selectFile).equals(PlayList.FakePlayerName)) {
                    this.drawCenterStrig(maxt,
                            new StringTextComponent(I18n.format("sfu.selectfileupdate.updloadby", PlayList.worldPlayListNamesMap.get(selectFile))),
                            this.field_230708_k_ / 2, ys + 27 + 20, 16777215);
                }

            } else if (selectFile != null && !PlayList.isExistsWorldPlaylistFile(selectFile)) {

                this.drawCenterStrig(maxt,
                        new StringTextComponent(I18n.format("sfu.selectfileupdate.nofile")),
                        this.field_230708_k_ / 2, ys + 27 + 20, 16777215);

                this.drawCenterStrig(maxt, new StringTextComponent(selectFile.getName()),
                        this.field_230708_k_ / 2, ys + 27 + 12, 16777215);

            } else {
                this.drawCenterStrig(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.error")), this.field_230708_k_ / 2,
                        ys + 27 + 20, 11797508);
            }
        } else {
            this.drawCenterStrig(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.anotherplater")),
                    this.field_230708_k_ / 2, ys + 27 + 20, 16777215);
        }

    }

    private void drawCenterStrig(MatrixStack maxt, StringTextComponent stringTextComponent, int i, int i1, int i2) {
        this.func_238472_a_(maxt, this.field_230712_o_, stringTextComponent, i, i1, i2);
    }

    public void fritstUploadCofin() {

        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;

        uploadtarget = new FaseStringImageButton(xs + 18, ys + 25 + 44,
                71, 12, 0, 125, 12,
                SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {

            if (!ClientSoundFileSender.isReservationOrSending(this.selectFile.getName())) {
                if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
                    serverSelectTargetR = ServerFileSelectTarget.EVERYONE;
                    sendSFUPacket(19, serverSelectTargetR.toString());
                } else if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                    serverSelectTargetR = ServerFileSelectTarget.MAIN;
                    sendSFUPacket(19, serverSelectTargetR.toString());
                }
                serverFilelistUpdate();
            }
        }, new StringTextComponent("Unknow"), false);

        this.func_230480_a_(uploadtarget);
        uploadtarget.field_230694_p_ = false;
    }

    public void updateUploadCofin() {
        SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) mc.world
                .getTileEntity(this.container.pos);

        if (hasWindows(SoundFileUploaderWindwos.UPLOAD_COFIN)
                && sfit.getUsePlayerUUID().equals(PlayerHelper.getUUID(mc.player))) {

            serverFilelistUpdate();
            if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
                uploadtarget.func_238482_a_(new StringTextComponent(I18n.format("sfu.main")));
            } else if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                uploadtarget.func_238482_a_(new StringTextComponent(I18n.format("sfu.everyone")));
            }
            uploadtarget.field_230694_p_ = true;

            if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
                ((FaseStringImageButton) uploadtarget).isDrawPlayerFase = true;
            } else {
                ((FaseStringImageButton) uploadtarget).isDrawPlayerFase = false;
            }

        } else {
            uploadtarget.field_230694_p_ = false;
        }

    }

    public void drawUploadCofin(MatrixStack maxt) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(maxt, xs + 17, ys + 27, 0, 0, 181, 85);
        RenderSystem.popMatrix();

        if (!(mc.world.getTileEntity(this.container.pos) instanceof SoundFileUploaderTileEntity)) {
            return;
        }

        SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) mc.world
                .getTileEntity(this.container.pos);

        if (sfit.getUsePlayerUUID().equals(PlayerHelper.getUUID(mc.player))) {
            if (selectFile != null && selectFile.exists()) {
                this.field_230712_o_.func_238422_b_(maxt, new StringTextComponent(StringHelper.characterLimit(mc, selectFile.getName(), 177)), xs + 20,
                        ys + 37, 0);
                this.field_230712_o_.func_238422_b_(maxt,
                        new StringTextComponent(StringHelper.fileCapacityNotation((selectFile.length()))),
                        xs + 20,
                        ys + 47, 0);
            } else if (!selectFile.exists()) {
                this.field_230712_o_.func_238422_b_(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.nofile")), xs + 20, ys + 37, 0);
            }

            RenderSystem.pushMatrix();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
            AbstractGui.func_238463_a_(maxt, xs + 20, ys + 25 + 30, 12, 149, 12, 12, 256, 256);
            RenderSystem.popMatrix();
            try {
                if (ClientSoundFileSender.isSending(this.selectFile.getName())) {

                    int b = (int) (12
                            * ((float) ClientSoundFileSender.getPrograses(this.selectFile.getName())
                            / (float) ClientSoundFileSender.getLength(this.selectFile.getName())));

                    RenderSystem.pushMatrix();
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                    mc.getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
                    AbstractGui.func_238463_a_(maxt, xs + 20, ys + 25 + 30, 0, 149, 12, b, 256, 256);
                    RenderSystem.popMatrix();
                    this.field_230712_o_.func_238422_b_(maxt, new StringTextComponent(StringHelper.getPercentage(ClientSoundFileSender.getLength(this.selectFile.getName()),
                            ClientSoundFileSender.getPrograses(this.selectFile.getName()))),
                            xs + 20 + 15,
                            ys + 25 + 32,
                            0);

                }
            } catch (Exception e) {

            }
            if (PlayList.isExistsWorldPlaylistFile(selectFile)) {
                this.field_230712_o_.func_238422_b_(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.overwrite")), xs + 20 + 71, ys + 25 + 46,
                        0);
                this.field_230712_o_.func_238422_b_(maxt,
                        new StringTextComponent(I18n.format("sfu.selectfileupdate.samenameexists")), xs + 20, ys + 25 + 65, 0);
            } else {
                this.field_230712_o_.func_238422_b_(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.forupload")), xs + 20 + 71, ys + 25 + 46,
                        0);
            }

            if (PlayList.worldPlayList != null) {
                this.field_230712_o_.func_238422_b_(maxt,
                        new StringTextComponent(I18n.format("sfu.selectfileupdate.worldfilecont", PlayList.worldPlayList.length,
                                StringHelper.fileCapacityNotation(PlayList.worldPlayListSize))),
                        xs + 20, ys + 25 + 57, 0);
            }
        } else {
            this.drawCenterStrig(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.anotherplater")),
                    this.field_230708_k_ / 2, ys + 27 + 20, 16777215);
        }
    }

    public void drawServerFileselect(MatrixStack maxt) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(maxt, xs + 17, ys + 27, 0, 0, 181, 85);
        RenderSystem.popMatrix();

        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
        this.func_238474_b_(maxt, xs + 18 + 65, ys + 99, 181, 72, 56, 12);
        RenderSystem.popMatrix();

        if (PlayList.worldPlayList != null) {
            if (PlayList.worldPlayList.length == 0) {
                this.drawCenterStrig(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.nofile")),
                        this.field_230708_k_ / 2, ys + 27 + 12, 16777215);
            }

            if (!(getMaxPage(PlayList.worldPlayList) == 0 || getMaxPage(PlayList.worldPlayList) == 1)) {
                RenderSystem.pushMatrix();
                this.drawCenterStrig(maxt, new StringTextComponent(this.page + "/" + this.getMaxPage(PlayList.worldPlayList)), xs + 18 + 96,
                        ys + 101, 0);
                RenderSystem.popMatrix();
            }
        }
    }

    public void drawErrorAntena(MatrixStack maxt) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(maxt, xs + 17, ys + 27, 0, 0, 181, 85);
        RenderSystem.popMatrix();
        this.drawCenterStrig(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.error")), this.field_230708_k_ / 2,
                ys + 27 + 20, 11797508);
        this.drawCenterStrig(maxt,
                new StringTextComponent(I18n.format("sfu.selectfileupdate.noantena")),
                this.field_230708_k_ / 2, ys + 27 + 28, 16777215);

    }

    public void fristFileUpload() {

        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        uploadstart = new StringImageButton(xs + 17 + 181 - 61, ys + 99, 60,
                12, 181, 84, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {

            if (hasWindows(SoundFileUploaderWindwos.UPLOAD_FILE)) {

                if (ClientSoundFileSender.isReservationOrSending(this.selectFile.getName())) {

                    ClientSoundFileSender.deletReseOrStopSend(this.selectFile.getName());

                } else {
                    serverFilelistUpdate();
                    sendSFUPacket(13);
                }

            } else if (hasWindows(SoundFileUploaderWindwos.UPLOAD_COFIN)) {
                if (ClientSoundFileSender.isReservationOrSending(this.selectFile.getName())) {
                    ClientSoundFileSender.deletReseOrStopSend(this.selectFile.getName());
                } else {
                    ClientSoundFileSender.addReseOrStartSend(selectFile.getName(),
                            this.serverSelectTargetR == ServerFileSelectTarget.MAIN ? ClientSoundFileSender.SendFolderType.MAIN
                                    : ClientSoundFileSender.SendFolderType.EVERYONE);
                }
            }
        }, new StringTextComponent(I18n.format("gui.yes")), -17, false);

        uploadstart.field_230694_p_ = false;
        this.func_230480_a_(uploadstart);
        uploadno = new StringImageButton(xs + 18, ys + 99, 60,
                12, 181, 108, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            sendSFUPacket(2);
            sendSFUPacket(6);
            sendSFUPacket(8);
        }, new StringTextComponent(I18n.format("gui.no")), -17, false);

        uploadno.field_230694_p_ = false;
        this.func_230480_a_(uploadno);
    }

    public void updateFileUpload() {
        if (this.selectFile != null) {
            if (!hasWindows(SoundFileUploaderWindwos.UPLOAD_FILE)) {

                if (hasWindows(SoundFileUploaderWindwos.UPLOAD_COFIN)) {

                    if (isUsingMain()) {
                        if (selectFile.exists()) {
                            uploadstart.field_230694_p_ = true;
                        } else {
                            uploadstart.field_230694_p_ = false;
                        }
                    } else {
                        uploadstart.field_230694_p_ = false;

                    }

                    if (!ClientSoundFileSender.isReservationOrSending(this.selectFile.getName())) {

                        uploadstart.func_238482_a_(new StringTextComponent(I18n.format("sfu.start")));
                        ((StringImageButton) uploadstart).dredclos = false;
                    } else {
                        uploadstart.func_238482_a_(new StringTextComponent(I18n.format("sfu.stop")));
                        ((StringImageButton) uploadstart).dredclos = true;
                    }

                    uploadno.field_230694_p_ = true;
                    uploadno.func_238482_a_(new StringTextComponent(I18n.format("gui.back")));
                } else {
                    uploadstart.field_230694_p_ = false;
                    uploadno.field_230694_p_ = false;
                }
                return;
            }
        } else {

            uploadstart.field_230694_p_ = false;
            uploadno.field_230694_p_ = false;
        }
        if (!(mc.world.getTileEntity(this.container.pos) instanceof SoundFileUploaderTileEntity)) {
            return;
        }

        if (isUsingMain()) {
            if (selectFile == null || ClientSoundFileSender.isReservationOrSending(this.selectFile.getName())
                    || !selectFile.exists()) {
                try {

                    if (selectFile != null) {
                        if (ClientSoundFileSender.isReservationOrSending(this.selectFile.getName())) {
                            uploadstart.field_230694_p_ = true;
                            uploadstart.func_238482_a_(new StringTextComponent(I18n.format("sfu.stop")));
                            ((StringImageButton) uploadstart).dredclos = true;
                        } else {
                            uploadstart.field_230694_p_ = false;
                            uploadno.func_238482_a_(new StringTextComponent(I18n.format("gui.back")));
                            ((StringImageButton) uploadstart).dredclos = false;
                        }
                    }

                } catch (Exception e) {
                }
            } else {
                uploadstart.field_230694_p_ = true;
                uploadstart.func_238482_a_(new StringTextComponent(I18n.format("gui.yes")));
                ((StringImageButton) uploadstart).dredclos = false;

            }
            uploadno.field_230694_p_ = true;
            uploadno.func_238482_a_(new StringTextComponent(I18n.format("gui.no")));
        } else {
            uploadstart.field_230694_p_ = false;
            uploadno.field_230694_p_ = false;
        }

    }

    public void drawFileUpload(MatrixStack maxt) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(maxt, xs + 17, ys + 27, 0, 0, 181, 85);
        RenderSystem.popMatrix();

        if (!(mc.world.getTileEntity(this.container.pos) instanceof SoundFileUploaderTileEntity)) {
            return;
        }

        SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) mc.world
                .getTileEntity(this.container.pos);
        if (sfit.getUsePlayerUUID().equals(PlayerHelper.getUUID(mc.player))) {
            if (!ClientSoundFileSender.isReservationOrSending(this.selectFile.getName())) {
                if (selectFile != null && selectFile.exists()) {
                    this.drawCenterStrig(maxt,
                            new StringTextComponent(I18n.format("sfu.selectfileupdate." + (mc.isSingleplayer() ? "world" : "server"))),
                            this.field_230708_k_ / 2, ys + 27 + 20, 16777215);
                    this.drawCenterStrig(maxt, new StringTextComponent(selectFile.getName()),
                            this.field_230708_k_ / 2, ys + 27 + 12, 16777215);
                } else if (selectFile != null && !selectFile.exists()) {
                    this.drawCenterStrig(maxt,
                            new StringTextComponent(I18n.format("sfu.selectfileupdate.nofile")),
                            this.field_230708_k_ / 2, ys + 27 + 20, 16777215);

                    this.drawCenterStrig(maxt, new StringTextComponent(selectFile.getName()),
                            this.field_230708_k_ / 2, ys + 27 + 12, 16777215);

                } else {
                    this.drawCenterStrig(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.error")), this.field_230708_k_ / 2,
                            ys + 27 + 20, 11797508);
                }

            } else {
                if (ClientSoundFileSender.getSender(this.selectFile.getName()) == null || selectFile.getName() == null
                        || !selectFile.exists()) {
                    this.drawCenterStrig(maxt, new StringTextComponent("Error"), this.field_230708_k_ / 2, ys + 27 + 20, 11797508);

                } else {

                    this.drawCenterStrig(maxt,
                            new StringTextComponent(I18n.format("sfu.selectfileupding." + (mc.isSingleplayer() ? "world" : "server"))),
                            this.field_230708_k_ / 2, ys + 27 + 20, 16777215);
                    this.drawCenterStrig(maxt, new StringTextComponent(selectFile.getName()),
                            this.field_230708_k_ / 2, ys + 27 + 12, 16777215);
                    this.drawCenterStrig(maxt,
                            new StringTextComponent(StringHelper.getPercentage(ClientSoundFileSender.getLength(this.selectFile.getName()),
                                    ClientSoundFileSender.getPrograses(this.selectFile.getName()))),
                            this.field_230708_k_ / 2, ys + 27 + 28, 16777215);

                }
            }
        } else {
            this.drawCenterStrig(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.anotherplater")),
                    this.field_230708_k_ / 2, ys + 27 + 20, 16777215);
        }

    }

    private int getMaxPage(File[] afilse) {

        return (int) Math.ceil((float) afilse.length / 8f);
    }

    public void selectMusicFile(int num) {

        if (hasWindows(SoundFileUploaderWindwos.CLIENT_FILESELECT)) {
            selectFile = fileGetter(num, files);
            filesUpdate();
            sendSFUPacket(4);
            sendSFUPacket(5);
            sendSFUPacket(7, this.selectFile.getPath());
        } else if (hasWindows(SoundFileUploaderWindwos.SERVER_FILESELECT)) {
            selectFile = fileGetter(num, PlayList.worldPlayList);
            sendSFUPacket(14);
            sendSFUPacket(5);
            sendSFUPacket(7, this.selectFile.getPath());
        }
    }

    public void fristMusicFiles() {

        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        fuleb1 = new FaseStringImageButton(xs + 17 + 1, ys + 27 + 8, 179,
                8, 0, 85, 8, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            selectMusicFile(1);
        }, "", -80, false);
        fuleb2 = new FaseStringImageButton(xs + 17 + 1, ys + 27 + 8 * 2, 179,
                8, 0, 85, 8, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            selectMusicFile(2);
        }, "", -80, false);
        fuleb3 = new FaseStringImageButton(xs + 17 + 1, ys + 27 + 8 * 3, 179,
                8, 0, 85, 8, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            selectMusicFile(3);
        }, "", -80, false);
        fuleb4 = new FaseStringImageButton(xs + 17 + 1, ys + 27 + 8 * 4, 179,
                8, 0, 85, 8, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            selectMusicFile(4);
        }, "", -80, false);
        fuleb5 = new FaseStringImageButton(xs + 17 + 1, ys + 27 + 8 * 5, 179,
                8, 0, 85, 8, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            selectMusicFile(5);
        }, "", -80, false);
        fuleb6 = new FaseStringImageButton(xs + 17 + 1, ys + 27 + 8 * 6, 179,
                8, 0, 85, 8, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            selectMusicFile(6);
        }, "", -80, false);
        fuleb7 = new FaseStringImageButton(xs + 17 + 1, ys + 27 + 8 * 7, 179,
                8, 0, 85, 8, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            selectMusicFile(7);
        }, "", -80, false);
        fuleb8 = new FaseStringImageButton(xs + 17 + 1, ys + 27 + 8 * 8, 179,
                8, 0, 85, 8, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            selectMusicFile(8);
        }, "", -80, false);
        fuleb1.field_230694_p_ = false;
        fuleb2.field_230694_p_ = false;
        fuleb3.field_230694_p_ = false;
        fuleb4.field_230694_p_ = false;
        fuleb5.field_230694_p_ = false;
        fuleb6.field_230694_p_ = false;
        fuleb7.field_230694_p_ = false;
        fuleb8.field_230694_p_ = false;

        fuleb1.setFileSelectButton(true);
        fuleb2.setFileSelectButton(true);
        fuleb3.setFileSelectButton(true);
        fuleb4.setFileSelectButton(true);
        fuleb5.setFileSelectButton(true);
        fuleb6.setFileSelectButton(true);
        fuleb7.setFileSelectButton(true);
        fuleb8.setFileSelectButton(true);

        this.func_230480_a_(fuleb1);
        this.func_230480_a_(fuleb2);
        this.func_230480_a_(fuleb3);
        this.func_230480_a_(fuleb4);
        this.func_230480_a_(fuleb5);
        this.func_230480_a_(fuleb6);
        this.func_230480_a_(fuleb7);
        this.func_230480_a_(fuleb8);

        openfolder = new StringImageButton(xs + 18, ys + 99,
                71, 12, 181, 0, 12,
                SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            Util.getOSType().openFile(FileHelper.getClientPlayFileDataPath().toFile());
        }, new StringTextComponent(I18n.format("sfu.folder")), -25, false);

        this.func_230480_a_(openfolder);
        openfolder.field_230694_p_ = false;
        back = new ImageButton(xs + 18 + 71, ys + 99, 10,
                12, 181, 24, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {

            if (page > 1)
                page--;
            else
                page = 1;

        });

        this.func_230480_a_(back);
        back.field_230694_p_ = false;
        next = new ImageButton(xs + 18 + 81, ys + 99, 10,
                12, 191, 24, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            if (hasWindows(SoundFileUploaderWindwos.CLIENT_FILESELECT)) {
                if (page < getMaxPage(files))
                    page++;
                else
                    page = getMaxPage(files);
            } else if (hasWindows(SoundFileUploaderWindwos.SERVER_FILESELECT)) {
                if (page < getMaxPage(PlayList.worldPlayList))
                    page++;
                else
                    page = getMaxPage(PlayList.worldPlayList);
            }
        });

        this.func_230480_a_(next);
        next.field_230694_p_ = false;

        reload = new StringImageButton(xs + 17 + 181 - 61, ys + 99, 60,
                12, 181, 48, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            if (hasWindows(SoundFileUploaderWindwos.CLIENT_FILESELECT)) {
                filesUpdate();
            } else if (hasWindows(SoundFileUploaderWindwos.SERVER_FILESELECT)) {
                serverFilelistUpdate();
            }
        }, new StringTextComponent(I18n.format("sfu.reload")), -17, false);

        this.func_230480_a_(reload);
        reload.field_230694_p_ = false;

        serverselecttarget = new FaseStringImageButton(xs + 18, ys + 99,
                71, 12, 0, 125, 12,
                SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {

            if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
                serverSelectTargetR = ServerFileSelectTarget.EVERYONE;
                sendSFUPacket(19, serverSelectTargetR.toString());
            } else if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                serverSelectTargetR = ServerFileSelectTarget.MAIN;
                sendSFUPacket(19, serverSelectTargetR.toString());
            }
            serverFilelistUpdate();

        }, new StringTextComponent("Unknow"), false);

        this.func_230480_a_(serverselecttarget);
        serverselecttarget.field_230694_p_ = false;
    }

    public void fristNoneIcons() {
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        updatericon = new ImageButton(xs + this.xSize / 2 - 14, ys + this.ySize / 4, 12,
                12, 0, 101, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            filesUpdate();
            sendSFUPacket(2);
        });
        updatericon.field_230694_p_ = false;
        this.func_230480_a_(updatericon);

        servericon = new ImageButton(xs + this.xSize / 2 + 2, ys + this.ySize / 4, 12,
                12, 12, 101, 12, SFU_GUI_TEXTURE2, 256, 256, (p_213096_1_) -> {
            serverFilelistUpdate();
            sendSFUPacket(9);
        });
        servericon.field_230694_p_ = false;
        this.func_230480_a_(servericon);
    }

    public void updateNoneIcons() {
        if (mc.world
                .getBlockState(this.container.pos)
                .get(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS) != SoundFileUploaderWindwos.DESKTOP) {
            updatericon.field_230694_p_ = false;
            servericon.field_230694_p_ = false;
            return;
        }
        updatericon.field_230694_p_ = true;
        servericon.field_230694_p_ = true;
    }

    public void updateMusicFiles() {

        if (hasWindows(SoundFileUploaderWindwos.CLIENT_FILESELECT)) {

            int filecont = files.length;
            int nowpagecont = page != getMaxPage(files) ? 8 : 8 - (getMaxPage(files) * 8 - filecont);

            if (page == 1) {
                nowpagecont = filecont;
                if (filecont == 0) {
                    nowpagecont = 0;
                }
            }

            if (nowpagecont >= 1) {
                fuleb1.field_230694_p_ = true;
                fuleb1.func_238482_a_(new StringTextComponent(fileNameGetter(1, files)));
                fuleb1.isDrawPlayerFase = false;
            } else {
                fuleb1.field_230694_p_ = false;
            }
            if (nowpagecont >= 2) {
                fuleb2.field_230694_p_ = true;
                fuleb2.func_238482_a_(new StringTextComponent(fileNameGetter(2, files)));
                fuleb2.isDrawPlayerFase = false;
            } else {
                fuleb2.field_230694_p_ = false;
            }
            if (nowpagecont >= 3) {
                fuleb3.field_230694_p_ = true;
                fuleb3.func_238482_a_(new StringTextComponent(fileNameGetter(3, files)));
                fuleb3.isDrawPlayerFase = false;
            } else {
                fuleb3.field_230694_p_ = false;
            }
            if (nowpagecont >= 4) {
                fuleb4.field_230694_p_ = true;
                fuleb4.func_238482_a_(new StringTextComponent(fileNameGetter(4, files)));
                fuleb4.isDrawPlayerFase = false;
            } else {
                fuleb4.field_230694_p_ = false;
            }
            if (nowpagecont >= 5) {
                fuleb5.field_230694_p_ = true;
                fuleb5.func_238482_a_(new StringTextComponent(fileNameGetter(5, files)));
                fuleb5.isDrawPlayerFase = false;
            } else {
                fuleb5.field_230694_p_ = false;
            }
            if (nowpagecont >= 6) {
                fuleb6.field_230694_p_ = true;
                fuleb6.func_238482_a_(new StringTextComponent(fileNameGetter(6, files)));
                fuleb6.isDrawPlayerFase = false;
            } else {
                fuleb6.field_230694_p_ = false;
            }
            if (nowpagecont >= 7) {
                fuleb7.field_230694_p_ = true;
                fuleb7.func_238482_a_(new StringTextComponent(fileNameGetter(7, files)));
                fuleb7.isDrawPlayerFase = false;
            } else {
                fuleb7.field_230694_p_ = false;
            }
            if (nowpagecont >= 8) {
                fuleb8.field_230694_p_ = true;
                fuleb8.func_238482_a_(new StringTextComponent(fileNameGetter(8, files)));
                fuleb8.isDrawPlayerFase = false;
            } else {
                fuleb8.field_230694_p_ = false;
            }

            openfolder.field_230694_p_ = true;

            if (!(getMaxPage(files) == 0 || getMaxPage(files) == 1)) {
                back.field_230694_p_ = true;
                next.field_230694_p_ = true;
            } else {
                back.field_230694_p_ = false;
                next.field_230694_p_ = false;

            }
            reload.field_230694_p_ = true;
            serverselecttarget.field_230694_p_ = false;
            return;
        } else if (hasWindows(SoundFileUploaderWindwos.SERVER_FILESELECT)) {
            if (PlayList.worldPlayList != null) {
                int filecont = PlayList.worldPlayList.length;
                int nowpagecont = page != getMaxPage(PlayList.worldPlayList) ? 8
                        : 8 - (getMaxPage(PlayList.worldPlayList) * 8 - filecont);

                if (page == 1) {
                    nowpagecont = filecont;
                    if (filecont == 0) {
                        nowpagecont = 0;
                    }
                }

                if (nowpagecont >= 1) {
                    fuleb1.field_230694_p_ = true;
                    fuleb1.func_238482_a_(new StringTextComponent(fileNameGetter(1, PlayList.worldPlayList)));
                    if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                        fuleb1.isDrawPlayerFase = true;
                        fuleb1.setFasResourceLocation(playerNameGetter(1));
                    } else {
                        fuleb1.isDrawPlayerFase = false;
                    }
                } else {
                    fuleb1.field_230694_p_ = false;
                }
                if (nowpagecont >= 2) {
                    fuleb2.field_230694_p_ = true;
                    fuleb2.func_238482_a_(new StringTextComponent(fileNameGetter(2, PlayList.worldPlayList)));
                    if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                        fuleb2.isDrawPlayerFase = true;
                        fuleb2.setFasResourceLocation(playerNameGetter(2));
                    } else {
                        fuleb2.isDrawPlayerFase = false;
                    }
                } else {
                    fuleb2.field_230694_p_ = false;
                }
                if (nowpagecont >= 3) {
                    fuleb3.field_230694_p_ = true;
                    fuleb3.func_238482_a_(new StringTextComponent(fileNameGetter(3, PlayList.worldPlayList)));
                    if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                        fuleb3.isDrawPlayerFase = true;
                        fuleb3.setFasResourceLocation(playerNameGetter(3));
                    } else {
                        fuleb3.isDrawPlayerFase = false;
                    }
                } else {
                    fuleb3.field_230694_p_ = false;
                }
                if (nowpagecont >= 4) {
                    fuleb4.field_230694_p_ = true;
                    fuleb4.func_238482_a_(new StringTextComponent(fileNameGetter(4, PlayList.worldPlayList)));
                    if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                        fuleb4.isDrawPlayerFase = true;
                        fuleb4.setFasResourceLocation(playerNameGetter(4));
                    } else {
                        fuleb4.isDrawPlayerFase = false;
                    }
                } else {
                    fuleb4.field_230694_p_ = false;
                }
                if (nowpagecont >= 5) {
                    fuleb5.field_230694_p_ = true;
                    fuleb5.func_238482_a_(new StringTextComponent(fileNameGetter(5, PlayList.worldPlayList)));
                    if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                        fuleb5.isDrawPlayerFase = true;
                        fuleb5.setFasResourceLocation(playerNameGetter(5));
                    } else {
                        fuleb5.isDrawPlayerFase = false;
                    }
                } else {
                    fuleb5.field_230694_p_ = false;
                }
                if (nowpagecont >= 6) {
                    fuleb6.field_230694_p_ = true;
                    fuleb6.func_238482_a_(new StringTextComponent(fileNameGetter(6, PlayList.worldPlayList)));
                    if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                        fuleb6.isDrawPlayerFase = true;
                        fuleb6.setFasResourceLocation(playerNameGetter(6));
                    } else {
                        fuleb6.isDrawPlayerFase = false;
                    }
                } else {
                    fuleb6.field_230694_p_ = false;
                }
                if (nowpagecont >= 7) {
                    fuleb7.field_230694_p_ = true;
                    fuleb7.func_238482_a_(new StringTextComponent(fileNameGetter(7, PlayList.worldPlayList)));
                    if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                        fuleb7.isDrawPlayerFase = true;
                        fuleb7.setFasResourceLocation(playerNameGetter(7));
                    } else {
                        fuleb7.isDrawPlayerFase = false;
                    }
                } else {
                    fuleb7.field_230694_p_ = false;
                }
                if (nowpagecont >= 8) {
                    fuleb8.field_230694_p_ = true;
                    fuleb8.func_238482_a_(new StringTextComponent(fileNameGetter(8, PlayList.worldPlayList)));
                    if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                        fuleb8.isDrawPlayerFase = true;
                        fuleb8.setFasResourceLocation(playerNameGetter(8));
                    } else {
                        fuleb8.isDrawPlayerFase = false;
                    }
                } else {
                    fuleb8.field_230694_p_ = false;
                }

                if (!(getMaxPage(PlayList.worldPlayList) == 0 || getMaxPage(PlayList.worldPlayList) == 1)) {
                    back.field_230694_p_ = true;
                    next.field_230694_p_ = true;
                } else {
                    back.field_230694_p_ = false;
                    next.field_230694_p_ = false;
                }

                reload.field_230694_p_ = true;

                if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
                    serverselecttarget.func_238482_a_(new StringTextComponent(I18n.format("sfu.main")));
                } else if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
                    serverselecttarget.func_238482_a_(new StringTextComponent(I18n.format("sfu.everyone")));
                }

                serverselecttarget.field_230694_p_ = true;
                if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
                    ((FaseStringImageButton) serverselecttarget).isDrawPlayerFase = true;
                } else {
                    ((FaseStringImageButton) serverselecttarget).isDrawPlayerFase = false;
                }
                return;
            }
        }
        fuleb1.field_230694_p_ = false;
        fuleb2.field_230694_p_ = false;
        fuleb3.field_230694_p_ = false;
        fuleb4.field_230694_p_ = false;
        fuleb5.field_230694_p_ = false;
        fuleb6.field_230694_p_ = false;
        fuleb7.field_230694_p_ = false;
        fuleb8.field_230694_p_ = false;

        openfolder.field_230694_p_ = false;
        back.field_230694_p_ = false;
        next.field_230694_p_ = false;

        reload.field_230694_p_ = false;
        serverselecttarget.field_230694_p_ = false;
        return;

    }

    private void sendSFUPacket(int num, String st) {
        PacketHandler.INSTANCE.sendToServer(new SoundFileUploaderMessage(0, this.container.pos, num, st));
    }

    private void sendSFUPacket(int num) {
        PacketHandler.INSTANCE.sendToServer(new SoundFileUploaderMessage(0, this.container.pos, num));
    }

    private boolean hasWindows(SoundFileUploaderWindwos sfuw) {
        return mc.world.getBlockState(this.container.pos)
                .get(SoundfileUploaderBlock.SOUNDFILE_UPLOADER_WINDWOS) == sfuw;
    }

    private String fileNameGetter(int contb, File[] afilses) {
        if (fileGetter(contb, afilses) == null)
            return "Error";

        return StringHelper.characterLimit(mc, fileGetter(contb, afilses).getName(), 171);

    }

    private String playerNameGetter(int contb) {

        if (PlayList.worldPlayListNames.length == 0) {
            return null;
        }
        if (getMaxPage(PlayList.worldPlayList) == 0) {
            return PlayList.worldPlayListNames[contb - 1];
        }

        return PlayList.worldPlayListNames[8 * (page - 1) + contb - 1];
    }

    private File fileGetter(int contb, File[] afilses) {

        if (afilses.length == 0) {
            return null;
        }
        if (getMaxPage(afilses) == 0) {
            return afilses[contb - 1];
        }

        return afilses[8 * (page - 1) + contb - 1];
    }

    public void drawUploader(MatrixStack maxt) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(maxt, xs + 17, ys + 27, 0, 0, 181, 85);
        RenderSystem.popMatrix();

        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(SFU_GUI_TEXTURE2);
        this.func_238474_b_(maxt, xs + 18 + 65, ys + 99, 181, 72, 56, 12);
        RenderSystem.popMatrix();

        if (files.length == 0) {
            this.drawCenterStrig(maxt, new StringTextComponent(I18n.format("sfu.selectfileupdate.nofile")),
                    this.field_230708_k_ / 2, ys + 27 + 12, 16777215);
        }

        if (!(getMaxPage(files) == 0 || getMaxPage(files) == 1)) {
            RenderSystem.pushMatrix();
            this.field_230712_o_.func_238422_b_(maxt, new StringTextComponent(this.page + "/" + this.getMaxPage(files)),
                    xs + 18 + 96,
                    ys + 101, 0);
            RenderSystem.popMatrix();
        }
    }

    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        updateMusicFiles();
        updateNoneIcons();
        updateFileUpload();
        updateUploadCofin();
        updateEditFile();
        if (!(mc.world.getTileEntity(this.container.pos) instanceof SoundFileUploaderTileEntity)) {
            return;
        }

        SoundFileUploaderTileEntity sfit = (SoundFileUploaderTileEntity) mc.world
                .getTileEntity(this.container.pos);

        if (hasWindows(SoundFileUploaderWindwos.CLIENT_FILESELECT) || (hasWindows(SoundFileUploaderWindwos.UPLOAD_FILE))
                && sfit.getUsePlayerUUID()
                .equals(PlayerHelper.getUUID(mc.player))
                || hasWindows(SoundFileUploaderWindwos.SERVER_FILESELECT)
                || (hasWindows(SoundFileUploaderWindwos.UPLOAD_COFIN))
                && sfit.getUsePlayerUUID()
                .equals(PlayerHelper.getUUID(mc.player))) {
            exit.field_230694_p_ = true;
        } else {
            exit.field_230694_p_ = false;
        }
    }

    @Override
    public boolean func_231046_a_(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        int k8n = 9;
        int k1n = 2;
        int k0n = 11;

        if (p_keyPressed_2_ == k8n) {
            k8y = true;
        } else if (p_keyPressed_2_ != k1n && p_keyPressed_2_ != k0n) {
            k8y = false;
        }
        if (k8y && p_keyPressed_2_ == k1n) {
            k1y = true;
        } else if (p_keyPressed_2_ != k0n) {
            k1y = false;
        }
        if (k1y && p_keyPressed_2_ == k0n) {
            k0y = true;
        } else {
            k0y = false;
        }

        if (k8y && k1y && k0y) {
            sendSFUPacket(1);
            k8y = false;
            k1y = false;
            k0y = false;
        }

        int k9n = 10;
        int k4n = 5;

        if (p_keyPressed_2_ == k1n) {
            k1d = true;
        } else if (p_keyPressed_2_ != k9n && p_keyPressed_2_ != k4n) {
            k1d = false;
        }
        if (k1d && p_keyPressed_2_ == k9n) {
            k9d = true;
        } else if (p_keyPressed_2_ != k4n) {
            k9d = false;
        }
        if (k9d && p_keyPressed_2_ == k4n) {
            k4d = true;
        } else {
            k4d = false;
        }

        if (k1d && k9d && k4d) {
            sendSFUPacket(12);
            k1d = false;
            k9d = false;
            k4d = false;
        }
        InputMappings.Input mouseKey = InputMappings.getInputByCode(p_keyPressed_1_, p_keyPressed_2_);
        if (p_keyPressed_1_ == 256 || this.getMinecraft().gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)) {
            func_231175_as__();
            return true;
        }

        if (this.func_195363_d(p_keyPressed_1_, p_keyPressed_2_))
            return true;
        if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            if (this.getMinecraft().gameSettings.keyBindPickBlock.isActiveAndMatches(mouseKey)) {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
                return true;
            } else if (this.getMinecraft().gameSettings.keyBindDrop.isActiveAndMatches(mouseKey)) {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, func_231172_r_() ? 1 : 0,
                        ClickType.THROW);
                return true;
            }
        } else if (this.getMinecraft().gameSettings.keyBindDrop.isActiveAndMatches(mouseKey)) {
            return true;
        }

        return false;
    }

    @Override
    public void func_231175_as__() {
        super.func_231175_as__();
        k8y = false;
        k1y = false;
        k0y = false;
        k1d = false;
        k9d = false;
        k4d = false;
    }

}
