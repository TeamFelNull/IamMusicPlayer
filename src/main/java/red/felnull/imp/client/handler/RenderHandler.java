package red.felnull.imp.client.handler;


import com.google.common.base.Strings;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.OptionsSoundsScreen;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.file.ClientSoundFileSender;
import red.felnull.imp.client.renderer.item.CassetteItemRenderer;
import red.felnull.imp.client.renderer.item.ParabolicAntennaRenderer;
import red.felnull.imp.client.screen.IMPSoundSlider;
import red.felnull.imp.file.ClientFileReceiver;
import red.felnull.imp.file.FileReceiverBuffer;
import red.felnull.imp.item.CassetteTapeItem;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.item.ParabolicAntennaItem;
import red.felnull.imp.sound.SoundData;
import red.felnull.imp.sound.WorldPlayListSoundData;
import red.felnull.imp.util.*;

import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

//@Mod.EventBusSubscriber(modid = IamMusicPlayer.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderHandler {
    //   private static Minecraft mc = Minecraft.getInstance();
    public static int waitThreadCrash;
    private static ResourceLocation tootiplaction = new ResourceLocation(IamMusicPlayer.MODID,
            "textures/gui/cassette_tooltip.png");

    public static int MaxDraw = 3;

    public static Map<String, Integer> expations = new HashMap<String, Integer>();

    @SubscribeEvent
    public static void onRender(TickEvent.RenderTickEvent e) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.gameSettings.showDebugInfo) {
            drawStrings(new MatrixStack());
        }
    }

    private static void drawStrings(MatrixStack matx) {
        Minecraft mc = Minecraft.getInstance();
        List<String> stlist = new ArrayList<String>();

        stlist.addAll(addUpLoadPrograse());

        stlist.addAll(addDownloadPrograse());

        for (Map.Entry<String, Integer> exs : RenderHandler.expations.entrySet()) {
            stlist.add(exs.getKey());
        }

        if (waitThreadCrash != 0) {
            stlist.add(I18n.format("overlay.soundwaitthread.crash"));
        }

        for (int i = 0; i < stlist.size(); ++i) {
            String s = stlist.get(i);
            if (!Strings.isNullOrEmpty(s)) {
                int j = 9;
                int k = mc.fontRenderer.getStringWidth(s);
                int l = 2;
                int i1 = 2 + j * i;
                AbstractGui.func_238467_a_(matx, 1, i1 - 1, 2 + k + 1, i1 + j - 1, -1873784752);
                mc.fontRenderer.func_238422_b_(matx, new StringTextComponent(s), (float) l, (float) i1, 14737632);
            }
        }
    }

    private static List<String> addUpLoadPrograse() {
        List<String> stlist = new ArrayList<String>();
        Minecraft mc = Minecraft.getInstance();
        for (Map.Entry<String, ClientSoundFileSender> en : ClientSoundFileSender.getSender().entrySet()) {
            stlist.add(I18n.format("overlay.sending." + (mc.isSingleplayer() ? "world" : "server"),
                    en.getKey(),
                    stlist.add(StringHelper.fileCapacityNotation(ClientSoundFileSender.getPrograses(en.getKey()))
                            + " / "
                            + StringHelper.fileCapacityNotation(ClientSoundFileSender.getLength(en.getKey())) + " "
                            + StringHelper.getPercentage(ClientSoundFileSender.getLength(en.getKey()),
                            ClientSoundFileSender.getPrograses(en.getKey())))));
        }

        if (!ClientSoundFileSender.getReservations().isEmpty()) {
            stlist.add(I18n.format("overlay.sender.reservation", ClientSoundFileSender.getReservations().size()));
        }

        return stlist;
    }

    private static List<String> addDownloadPrograse() {
        List<String> stlist = new ArrayList<String>();
        Minecraft mc = Minecraft.getInstance();
        int cont = 0;
        try {

            for (Map.Entry<Integer, FileReceiverBuffer> en : ClientFileReceiver.receiverBufer.entrySet()) {
                stlist.add(I18n.format("overlay.receiver." + (mc.isSingleplayer() ? "world" : "server"),
                        Paths.get(en.getValue().filepath).toFile().getName()));
                stlist.add(StringHelper.fileCapacityNotation(en.getValue().getCont()) + " / "
                        + StringHelper.fileCapacityNotation(en.getValue().allcont) + " "
                        + en.getValue().getPrograsePar());
                cont++;
                if (cont > MaxDraw) {
                    stlist.add(
                            I18n.format("overlay.receiver.othertask",
                                    ClientFileReceiver.receiverBufer.entrySet().size() - MaxDraw));
                    break;
                }
            }
        } catch (Exception e) {

        }

        return stlist;
    }


    @SubscribeEvent
    public static void onGui(GuiScreenEvent.InitGuiEvent.Post e) {
        if (e.getGui() instanceof OptionsSoundsScreen) {
            e.addWidget(new IMPSoundSlider(e.getGui().field_230708_k_ / 2 - 155 + 160, e.getGui().field_230709_l_ / 6 - 12 + 24 * 5, 150, 20));
        }

    }

    @SubscribeEvent
    public static void onModelBaked(ModelBakeEvent e) {

        Map<ResourceLocation, IBakedModel> map = e.getModelRegistry();

        List<CassetteTapeItem> tapes = new ArrayList<CassetteTapeItem>();
        tapes.add((CassetteTapeItem) IMPItems.NO_RECORD_CASSETTE_TAPE);
        tapes.add((CassetteTapeItem) IMPItems.RECORD_CASSETTE_TAPE);
        tapes.add((CassetteTapeItem) IMPItems.OVERWRITABLE_CASSETTE_TAPE);

        bakaItemModel(map, IMPItems.PARABOLIC_ANTENNA,
                md -> ((ParabolicAntennaItem) IMPItems.PARABOLIC_ANTENNA).getModel(md, false));

        ParabolicAntennaRenderer.pmodel = e.getModelLoader().bake(
                new ResourceLocation(IamMusicPlayer.MODID, "item/" + IMPItems.PARABOLIC_ANTENNA.getRegistryName()
                        .getPath()),
                ModelRotation.X0_Y0);

        ParabolicAntennaRenderer.pmodel_null = e.getModelLoader().bake(
                new ResourceLocation(IamMusicPlayer.MODID, "item/" + IMPItems.PARABOLIC_ANTENNA.getRegistryName()
                        .getPath() + "_null"),
                ModelRotation.X0_Y0);

        for (CassetteTapeItem tape : tapes) {
            bakaItemModel(map, tape, md -> (tape).getModel(md));
            CassetteItemRenderer.casettomodels.put(tape, e.getModelLoader().bake(
                    new ResourceLocation(IamMusicPlayer.MODID, "item/" + tape.getRegistryName()
                            .getPath()),
                    ModelRotation.X0_Y0));
        }

    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent e) {

        List<CassetteTapeItem> tapes = new ArrayList<CassetteTapeItem>();
        tapes.add((CassetteTapeItem) IMPItems.NO_RECORD_CASSETTE_TAPE);
        tapes.add((CassetteTapeItem) IMPItems.RECORD_CASSETTE_TAPE);
        tapes.add((CassetteTapeItem) IMPItems.OVERWRITABLE_CASSETTE_TAPE);

        for (CassetteTapeItem tape : tapes) {
            ModelLoader.addSpecialModel(tape.getModel(null).getLocation());
        }

        ModelLoader.addSpecialModel(
                ((ParabolicAntennaItem) IMPItems.PARABOLIC_ANTENNA).getModel(null, false).getLocation());

        ModelLoader.addSpecialModel(
                ((ParabolicAntennaItem) IMPItems.PARABOLIC_ANTENNA).getModel(null, true).getLocation());

    }

    private static <T extends IBakedModel> void bakaItemModel(Map<ResourceLocation, IBakedModel> map, Item item, Function<IBakedModel, T> factory) {
        map.put(new ModelResourceLocation(item.getRegistryName(), "inventory"),
                factory.apply(map.get(new ModelResourceLocation(item.getRegistryName(), "inventory"))));
    }


    @SubscribeEvent
    public static void onToolTipRender(RenderTooltipEvent.PostBackground e) {

        if (!SoundHelper.isWritedSound(e.getStack())
                || WorldPlayListSoundData.getWorldPlayListData(e.getStack()).getSoundData().album_image_uuid
                .equals("uuid")) {
            return;
        }

        int x = e.getX();
        int y = e.getY();
        drawSoundDataTooltip(new MatrixStack(), x - 2, y + e.getHeight() + 5, e.getStack());
    }

    public static void drawSoundDataTooltip(MatrixStack matx, int x, int y, ItemStack stack) {

        WorldPlayListSoundData wplsd = WorldPlayListSoundData.getWorldPlayListData(stack);
        Minecraft mc = Minecraft.getInstance();
        String imageuuid = wplsd.getSoundData().album_image_uuid;

        TextureManager tm = mc.getTextureManager();

        Random rnd = new Random(
                IMPMath.stringDecimalConverter(WorldPlayListSoundData.getWorldPlayListData(stack).getName()));

        drawSoundDataBaggrand(matx, x, y,
                getSoundDataTooltipWidth(stack), getSoundDataTooltipHeight(stack), rnd.nextFloat() / 2f,
                rnd.nextFloat() / 2f, rnd.nextFloat() / 2f);

        int pxsize = 0;
        int pysize = 0;

        if (TextureHelper.isImageNotExists(imageuuid)) {
            pxsize = 128;
            pysize = 128;
        } else {
            pxsize = PictuerUtil.getImage(imageuuid).getWidth();
            pysize = PictuerUtil.getImage(imageuuid).getHeight();
        }

        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        tm.bindTexture(WorldPlayListSoundData.getWorldPlayListData(stack).getAlbumImage());
        AbstractGui.func_238463_a_(matx, x + 2, y + 2, 0, 0, pxsize / 4, pysize / 4,
                pxsize / 4, pysize / 4);
        RenderSystem.popMatrix();

        FontRenderer fr = mc.fontRenderer;

        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        fr.func_238422_b_(matx, new StringTextComponent(SoundHelper.getSoundName(stack)), x + 2 + pxsize / 4 + 3, y + 2, 0);

        List<ITextComponent> itc = new ArrayList<ITextComponent>();
        SoundData.addSoundDataTooltip(stack, itc);

        for (int i = 0; i < itc.size(); i++) {
            fr.func_238422_b_(matx, new StringTextComponent(itc.get(i).getString()), x + 2 + pxsize / 4 + 3, y + 2 + 9 + 9 * i, 0);
        }

        RenderSystem.popMatrix();

    }

    public static int getSoundDataTooltipHeight(ItemStack stack) {

        int size1 = 0;
        WorldPlayListSoundData wplsd = WorldPlayListSoundData.getWorldPlayListData(stack);

        int pysize = 0;
        if (TextureHelper.isImageNotExists(wplsd.getSoundData().album_image_uuid)) {
            pysize = 32;
        } else {
            pysize = PictuerUtil.getImage(wplsd.getSoundData().album_image_uuid).getHeight() / 4;
        }
        size1 = pysize;
        int size2 = 0;
        List<ITextComponent> itc = new ArrayList<ITextComponent>();
        SoundData.addSoundDataTooltip(stack, itc);

        size2 = 9 + 9 * itc.size();

        int out = IMPMath.mostNumber(size1, size2);
        out += 4;
        return out;
    }

    public static int getSoundDataTooltipWidth(ItemStack stack) {
        int size = 0;
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.fontRenderer;
        WorldPlayListSoundData wplsd = WorldPlayListSoundData.getWorldPlayListData(stack);

        int pysize = 0;
        if (TextureHelper.isImageNotExists(wplsd.getSoundData().album_image_uuid)) {
            pysize = 32;
        } else {
            pysize = PictuerUtil.getImage(wplsd.getSoundData().album_image_uuid).getWidth() / 4;
        }

        size += pysize;

        int namesize = fr.getStringWidth(SoundHelper.getSoundName(stack)) + 3;

        List<ITextComponent> itc = new ArrayList<ITextComponent>();
        SoundData.addSoundDataTooltip(stack, itc);
        int wc = 0;
        for (int i = 0; i < itc.size(); i++) {
            if (wc < fr.getStringWidth(itc.get(i).getString())) {
                wc = fr.getStringWidth(itc.get(i).getString());
            }
        }
        size += IMPMath.mostNumber(namesize, wc + 3);

        size += 4;

        return size;
    }

    public static void drawSoundDataBaggrand(MatrixStack matx, int x, int y, int w, int h, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        TextureManager tm = mc.getTextureManager();

        RenderSystem.pushMatrix();
        RenderSystem.color4f(r + 0.5f, g + 0.5f, b + 0.5f, 1.0f);

        int mw = 200;
        int mh = 100;

        int awc = w / mw;
        int awa = w - awc * mw;
        for (int i = 0; i < awc; i++) {
            tm.bindTexture(tootiplaction);
            AbstractGui.func_238463_a_(matx, x + i * mw, y, 0, 0, mw, 1, 256, 256);
        }
        tm.bindTexture(tootiplaction);
        AbstractGui.func_238463_a_(matx, x + awc * mw, y, 0, 0, awa, 1, 256, 256);

        for (int i = 0; i < awc; i++) {
            tm.bindTexture(tootiplaction);
            AbstractGui.func_238463_a_(matx, x + i * mw, y + h - 1, 0, 0, mw, 1, 256, 256);
        }
        tm.bindTexture(tootiplaction);
        AbstractGui.func_238463_a_(matx, x + awc * mw, y + h - 1, 0, 0, awa, 1, 256, 256);

        int ahc = h / mh;
        int aha = h - ahc * mh;
        for (int i = 0; i < ahc; i++) {
            tm.bindTexture(tootiplaction);
            AbstractGui.func_238463_a_(matx, x, y + i * mh, 0, 0, 1, mh, 256, 256);
        }
        tm.bindTexture(tootiplaction);
        AbstractGui.func_238463_a_(matx, x, y + ahc * mh, 0, 0, 1, aha, 256, 256);

        for (int i = 0; i < ahc; i++) {
            tm.bindTexture(tootiplaction);
            AbstractGui.func_238463_a_(matx, x + w - 1, y + i * mh, 0, 0, 1, mh, 256, 256);
        }
        tm.bindTexture(tootiplaction);
        AbstractGui.func_238463_a_(matx, x + w - 1, y + ahc * mh, 0, 0, 1, aha, 256, 256);

        int mwb = mw - 2;
        int mhb = mh - 2;

        int awcb = (w - 2) / mwb;
        int awab = (w - 2) - awcb * mwb;

        int ahcb = (h - 2) / mhb;
        int ahab = (h - 2) - ahcb * mhb;

        for (int i = 0; i < awcb; i++) {
            for (int i2 = 0; i2 < ahcb; i2++) {
                tm.bindTexture(tootiplaction);
                AbstractGui.func_238463_a_(matx, x + 1 + i * mwb, y + 1 + i2 * mhb, 1, 1, mwb, mhb, 256, 256);
            }
            tm.bindTexture(tootiplaction);
            AbstractGui.func_238463_a_(matx, x + 1 + i * mwb, y + 1 + ahcb * mhb, 1, 1, mwb, ahab, 256, 256);
        }
        tm.bindTexture(tootiplaction);
        AbstractGui.func_238463_a_(matx, x + 1 + awcb * mwb, y + 1 + ahcb * mhb, 1, 1, awab, ahab, 256, 256);

        RenderSystem.popMatrix();
    }

}
