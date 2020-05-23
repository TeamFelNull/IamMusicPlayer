package net.morimori.imp.client.handler;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.base.Strings;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.OptionsSoundsScreen;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.util.ItemHelper;
import net.morimori.imp.util.PictuerUtil;
import net.morimori.imp.util.RenderHelper;
import net.morimori.imp.util.StringHelper;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)

public class RenderHandler {
	private static Minecraft mc = Minecraft.getInstance();
	public static int waitThreadCrash;

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
		byte[] bytes = WorldPlayListSoundData.getWorldPlayListData(e.getStack()).getSoundData().album_image;

		if (bytes.length <= 0) {
			return;
		}
		int x = e.getX();
		int y = e.getY();

		int pxsize = PictuerUtil.getImage(bytes).getWidth();
		int pysize = PictuerUtil.getImage(bytes).getHeight();
		int size = 128;

		RenderSystem.pushMatrix();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(RenderHelper.getBytePictuerLocation(bytes));
		AbstractGui.blit(x + (size - pxsize) / 4, y + (size - pysize) / 4, 0, 0, pxsize / 2, pysize / 2, pxsize / 2,
				pysize / 2);
		RenderSystem.popMatrix();

	}
}
