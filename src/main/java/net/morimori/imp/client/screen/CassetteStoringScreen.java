package net.morimori.imp.client.screen;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.container.CassetteStoringContainer;
import net.morimori.imp.tileentity.CassetteStoringTileEntity;
import net.morimori.imp.util.ItemHelper;
import net.morimori.imp.util.SoundHelper;
import net.morimori.imp.util.StringHelper;

public class CassetteStoringScreen extends ContainerScreen<CassetteStoringContainer> {
	protected static final ResourceLocation CS_GUI_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID,
			"textures/gui/container/cassete_storing.png");
	private static Minecraft mc = Minecraft.getInstance();

	public static Map<Integer, Integer> slips = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> slipmaxs = new HashMap<Integer, Integer>();

	public CassetteStoringScreen(CassetteStoringContainer screenContainer, PlayerInventory inv,
			ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		this.ySize = 238;

	}

	public String getSlipedNameString(int num) {

		if (!(mc.world.getTileEntity(this.container.pos) instanceof CassetteStoringTileEntity))
			return null;

		CassetteStoringTileEntity tile = (CassetteStoringTileEntity) mc.world
				.getTileEntity(this.container.pos);

		ItemStack cassette = tile.getCassette(num);
		String outst = SoundHelper.getCassetteSoundName(cassette);

		slipmaxs.put(num, outst.length());

		if (slips.containsKey(num)) {
			outst = StringHelper.slipString(outst, true, slips.get(num));
		}

		return outst;
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground();
		super.render(p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredToolTip(p_render_1_, p_render_2_);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.pushMatrix();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(CS_GUI_TEXTURE);
		int xs = (this.width - this.xSize) / 2;
		int ys = (this.height - this.ySize) / 2;
		this.blit(xs, ys, 0, 0, this.xSize, this.ySize);
		RenderSystem.popMatrix();
		drawBagGrand();
	}

	private void drawString(String st, int x, int y, int col) {
		int xs = (this.width - this.xSize) / 2;
		int ys = (this.height - this.ySize) / 2;
		RenderSystem.pushMatrix();
		this.font.drawString(st, xs + x, ys + y, col);
		RenderSystem.popMatrix();
	}

	public void drawBagGrand() {

		if (!(mc.world.getTileEntity(this.container.pos) instanceof CassetteStoringTileEntity))
			return;

		CassetteStoringTileEntity tile = (CassetteStoringTileEntity) mc.world
				.getTileEntity(this.container.pos);
		int coloer = 16729344;

		for (int i = 0; i < 8; ++i) {
			ItemStack cassette = tile.getCassette(i);
			if (ItemHelper.isCassette(cassette) && SoundHelper.isWritedSound(cassette)) {
				RenderSystem.pushMatrix();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.minecraft.getTextureManager().bindTexture(CS_GUI_TEXTURE);
				int xs = (this.width - this.xSize) / 2;
				int ys = (this.height - this.ySize) / 2;
				this.blit(xs + 7, ys + 7 + 18 * i, 0, 238, 54, 18);
				RenderSystem.popMatrix();
				drawString(StringHelper.characterLimit(mc, getSlipedNameString(i), 40, false), 18,
						12 + 18 * i, coloer);
			}
		}

		for (int i = 0; i < 8; ++i) {
			ItemStack cassette = tile.getCassette(i + 8);
			if (ItemHelper.isCassette(cassette) && SoundHelper.isWritedSound(cassette)) {
				RenderSystem.pushMatrix();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.minecraft.getTextureManager().bindTexture(CS_GUI_TEXTURE);
				int xs = (this.width - this.xSize) / 2;
				int ys = (this.height - this.ySize) / 2;
				this.blit(xs + 115, ys + 7 + 18 * i, 0, 238, 54, 18);
				RenderSystem.popMatrix();
				drawString(StringHelper.characterLimit(mc, getSlipedNameString(i + 8), 40, false), 126,
						12 + 18 * i, coloer);
			}
		}

	}
}
