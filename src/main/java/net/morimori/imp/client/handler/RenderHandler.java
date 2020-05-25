package net.morimori.imp.client.handler;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.Function;

import com.google.common.base.Strings;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.client.renderer.item.CassetteItemRenderer;
import net.morimori.imp.client.screen.IMPSoundSlider;
import net.morimori.imp.file.ClientFileReceiver;
import net.morimori.imp.file.ClientFileSender;
import net.morimori.imp.file.FileReceiverBuffer;
import net.morimori.imp.item.CassetteTapeItem;
import net.morimori.imp.item.IMPItems;
import net.morimori.imp.sound.SoundData;
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.util.IMPMath;
import net.morimori.imp.util.ItemHelper;
import net.morimori.imp.util.PictuerUtil;
import net.morimori.imp.util.StringHelper;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)

public class RenderHandler {
	private static Minecraft mc = Minecraft.getInstance();
	public static int waitThreadCrash;
	private static ResourceLocation tootiplaction = new ResourceLocation(IkisugiMusicPlayer.MODID,
			"textures/gui/cassette_tooltip.png");

	public static int MaxDraw = 3;

	@SubscribeEvent
	public static void onRender(RenderTickEvent e) {

		if (!mc.gameSettings.showDebugInfo) {
			drawPrograse();
		}
	}

	private static void drawPrograse() {

		List<String> stlist = new ArrayList<String>();

		stlist.addAll(addUpLoadPrograse());

		stlist.addAll(addDownloadPrograse());

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
				AbstractGui.fill(1, i1 - 1, 2 + k + 1, i1 + j - 1, -1873784752);
				mc.fontRenderer.drawString(s, (float) l, (float) i1, 14737632);
			}
		}
	}

	private static List<String> addUpLoadPrograse() {
		List<String> stlist = new ArrayList<String>();

		if (ClientFileSender.senderBuffer.isEmpty()) {
			return stlist;
		}
		int cont = 0;
		try {
			for (Entry<Integer, ClientFileSender> en : ClientFileSender.senderBuffer.entrySet()) {
				stlist.add(I18n.format("overlay.sending." + (mc.isSingleplayer() ? "world" : "server"),
						ClientFileSender.senderBuffer.get(en.getKey()).path.toFile().getName(),
						stlist.add(StringHelper.fileCapacityNotation(ClientFileSender.sendingprograses.get(en.getKey()))
								+ " / "
								+ StringHelper.fileCapacityNotation(ClientFileSender.sendingalls.get(en.getKey())) + " "
								+ ClientFileSender.getPrograsePar(en.getKey()))));

				cont++;
				if (cont > MaxDraw) {
					stlist.add(I18n.format("overlay.sender.othertask",
							ClientFileSender.senderBuffer.entrySet().size() - MaxDraw));
					break;
				}

			}
		} catch (Exception e) {

		}

		if (!ClientFileSender.reservationSenders.isEmpty()) {
			stlist.add(I18n.format("overlay.sender.reservation", ClientFileSender.reservationSenders.size()));
		}

		return stlist;
	}

	private static List<String> addDownloadPrograse() {
		List<String> stlist = new ArrayList<String>();

		int cont = 0;
		try {

			for (Entry<Integer, FileReceiverBuffer> en : ClientFileReceiver.receiverBufer.entrySet()) {
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
			e.addWidget(
					new IMPSoundSlider(e.getGui().width / 2 - 155 + 160, e.getGui().height / 6 - 12 + 24 * 5, 150, 20));
		}

	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onModelBaked(ModelBakeEvent e) {

		Map<ResourceLocation, IBakedModel> map = e.getModelRegistry();

		List<CassetteTapeItem> tapes = new ArrayList<CassetteTapeItem>();
		tapes.add((CassetteTapeItem) IMPItems.NO_RECORD_CASSETTE_TAPE);
		tapes.add((CassetteTapeItem) IMPItems.RECORD_CASSETTE_TAPE);
		tapes.add((CassetteTapeItem) IMPItems.OVERWRITABLE_CASSETTE_TAPE);

		for (CassetteTapeItem tape : tapes) {
			bakaItemModel(map, tape,
					md -> (tape).getModel(md));
			CassetteItemRenderer.casettomodels.put(tape, e.getModelLoader().func_217845_a(
					new ResourceLocation(IkisugiMusicPlayer.MODID, "item/" + tape.getRegistryName()
							.getPath()),
					ModelRotation.X0_Y0));
		}

	}

	public static void onModelRegistry(ModelRegistryEvent e) {

		List<CassetteTapeItem> tapes = new ArrayList<CassetteTapeItem>();
		tapes.add((CassetteTapeItem) IMPItems.NO_RECORD_CASSETTE_TAPE);
		tapes.add((CassetteTapeItem) IMPItems.RECORD_CASSETTE_TAPE);
		tapes.add((CassetteTapeItem) IMPItems.OVERWRITABLE_CASSETTE_TAPE);

		for (CassetteTapeItem tape : tapes) {
			ModelLoader.addSpecialModel(tape.getModel(null).getLocation());
		}

	}

	private static <T extends IBakedModel> void bakaItemModel(Map<ResourceLocation, IBakedModel> map, Item item,
			Function<IBakedModel, T> factory) {
		map.put(new ModelResourceLocation(item.getRegistryName(), "inventory"),
				factory.apply(map.get(new ModelResourceLocation(item.getRegistryName(), "inventory"))));
	}

	@SubscribeEvent
	public static void onToolTipRender(RenderTooltipEvent.PostBackground e) {

		if (!ItemHelper.isWritedSound(e.getStack())
				|| WorldPlayListSoundData.getWorldPlayListData(e.getStack()).getSoundData().album_image == null) {
			return;
		}

		int x = e.getX();
		int y = e.getY();
		drawSoundDataTooltip(x - 2, y + e.getHeight() + 5, e.getStack());
	}

	public static void drawSoundDataTooltip(int x, int y, ItemStack stack) {

		WorldPlayListSoundData wplsd = WorldPlayListSoundData.getWorldPlayListData(stack);

		byte[] bytes = wplsd.getSoundData().album_image;

		TextureManager tm = mc.getTextureManager();

		Random rnd = new Random(
				IMPMath.stringDecimalConverter(WorldPlayListSoundData.getWorldPlayListData(stack).getName()));

		drawSoundDataBaggrand(x, y,
				getSoundDataTooltipWidth(stack), getSoundDataTooltipHeight(stack), rnd.nextFloat() / 2f,
				rnd.nextFloat() / 2f, rnd.nextFloat() / 2f);

		int pxsize = 0;
		int pysize = 0;

		if (bytes.length <= 0) {
			pxsize = 128;
			pysize = 128;
		} else {
			pxsize = PictuerUtil.getImage(bytes, wplsd.getSoundData().album_image_uuid).getWidth();
			pysize = PictuerUtil.getImage(bytes, wplsd.getSoundData().album_image_uuid).getHeight();
		}

		RenderSystem.pushMatrix();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		tm.bindTexture(WorldPlayListSoundData.getWorldPlayListData(stack).getAlbumImage());
		AbstractGui.blit(x + 2, y + 2, 0, 0, pxsize / 4, pysize / 4,
				pxsize / 4, pysize / 4);
		RenderSystem.popMatrix();

		FontRenderer fr = mc.fontRenderer;

		RenderSystem.pushMatrix();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		fr.drawString(ItemHelper.getCassetteMusicName(stack), x + 2 + pxsize / 4 + 3, y + 2, 0);

		List<ITextComponent> itc = new ArrayList<ITextComponent>();
		SoundData.addSoundDataTooltip(stack, itc);

		for (int i = 0; i < itc.size(); i++) {
			fr.drawString(itc.get(i).getString(), x + 2 + pxsize / 4 + 3, y + 2 + 9 + 9 * i, 0);
		}

		RenderSystem.popMatrix();

	}

	public static int getSoundDataTooltipHeight(ItemStack stack) {

		int size1 = 0;
		WorldPlayListSoundData wplsd = WorldPlayListSoundData.getWorldPlayListData(stack);

		int pysize = 0;
		byte[] bytes = wplsd.getSoundData().album_image;
		if (bytes.length <= 0) {
			pysize = 32;
		} else {
			pysize = PictuerUtil.getImage(bytes, wplsd.getSoundData().album_image_uuid).getHeight() / 4;
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
		FontRenderer fr = mc.fontRenderer;
		WorldPlayListSoundData wplsd = WorldPlayListSoundData.getWorldPlayListData(stack);

		int pysize = 0;
		byte[] bytes = wplsd.getSoundData().album_image;
		if (bytes.length <= 0) {
			pysize = 32;
		} else {
			pysize = PictuerUtil.getImage(bytes, wplsd.getSoundData().album_image_uuid).getWidth() / 4;
		}

		size += pysize;

		int namesize = fr.getStringWidth(ItemHelper.getCassetteMusicName(stack)) + 3;

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

	public static void drawSoundDataBaggrand(int x, int y, int w, int h, float r, float g, float b) {
		TextureManager tm = mc.getTextureManager();

		RenderSystem.pushMatrix();
		RenderSystem.color4f(r + 0.5f, g + 0.5f, b + 0.5f, 1.0f);

		int mw = 200;
		int mh = 100;

		int awc = w / mw;
		int awa = w - awc * mw;
		for (int i = 0; i < awc; i++) {
			tm.bindTexture(tootiplaction);
			AbstractGui.blit(x + i * mw, y, 0, 0, mw, 1, 256, 256);
		}
		tm.bindTexture(tootiplaction);
		AbstractGui.blit(x + awc * mw, y, 0, 0, awa, 1, 256, 256);

		for (int i = 0; i < awc; i++) {
			tm.bindTexture(tootiplaction);
			AbstractGui.blit(x + i * mw, y + h - 1, 0, 0, mw, 1, 256, 256);
		}
		tm.bindTexture(tootiplaction);
		AbstractGui.blit(x + awc * mw, y + h - 1, 0, 0, awa, 1, 256, 256);

		int ahc = h / mh;
		int aha = h - ahc * mh;
		for (int i = 0; i < ahc; i++) {
			tm.bindTexture(tootiplaction);
			AbstractGui.blit(x, y + i * mh, 0, 0, 1, mh, 256, 256);
		}
		tm.bindTexture(tootiplaction);
		AbstractGui.blit(x, y + ahc * mh, 0, 0, 1, aha, 256, 256);

		for (int i = 0; i < ahc; i++) {
			tm.bindTexture(tootiplaction);
			AbstractGui.blit(x + w - 1, y + i * mh, 0, 0, 1, mh, 256, 256);
		}
		tm.bindTexture(tootiplaction);
		AbstractGui.blit(x + w - 1, y + ahc * mh, 0, 0, 1, aha, 256, 256);

		int mwb = mw - 2;
		int mhb = mh - 2;

		int awcb = (w - 2) / mwb;
		int awab = (w - 2) - awcb * mwb;

		int ahcb = (h - 2) / mhb;
		int ahab = (h - 2) - ahcb * mhb;

		for (int i = 0; i < awcb; i++) {
			for (int i2 = 0; i2 < ahcb; i2++) {
				tm.bindTexture(tootiplaction);
				AbstractGui.blit(x + 1 + i * mwb, y + 1 + i2 * mhb, 1, 1, mwb, mhb, 256, 256);
			}
			tm.bindTexture(tootiplaction);
			AbstractGui.blit(x + 1 + i * mwb, y + 1+ ahcb * mhb, 1, 1, mwb, ahab, 256, 256);
		}
		tm.bindTexture(tootiplaction);
		AbstractGui.blit(x + 1 + awcb * mwb, y + 1 + ahcb * mhb, 1, 1, awab, ahab, 256, 256);

		RenderSystem.popMatrix();
	}

}
