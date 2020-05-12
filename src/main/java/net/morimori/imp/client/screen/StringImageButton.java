package net.morimori.imp.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

public class StringImageButton extends ImageButton {
	private int lethi;
	public boolean dredclos;
	private static Minecraft mc = Minecraft.getInstance();
	private int cloer;

	public StringImageButton(int p_i51134_1_, int p_i51134_2_, int p_i51134_3_, int p_i51134_4_, int p_i51134_5_,
			int p_i51134_6_, int p_i51134_7_, ResourceLocation p_i51134_8_, IPressable p_i51134_9_, int leth,
			boolean redclos) {
		this(p_i51134_1_, p_i51134_2_, p_i51134_3_, p_i51134_4_, p_i51134_5_, p_i51134_6_, p_i51134_7_, p_i51134_8_,
				256, 256, p_i51134_9_, leth, redclos);
	}

	public StringImageButton(int p_i51135_1_, int p_i51135_2_, int p_i51135_3_, int p_i51135_4_, int p_i51135_5_,
			int p_i51135_6_, int p_i51135_7_, ResourceLocation p_i51135_8_, int p_i51135_9_, int p_i51135_10_,
			IPressable p_i51135_11_, int leth, boolean redclos) {
		this(p_i51135_1_, p_i51135_2_, p_i51135_3_, p_i51135_4_, p_i51135_5_, p_i51135_6_, p_i51135_7_, p_i51135_8_,
				p_i51135_9_, p_i51135_10_, p_i51135_11_, "", leth, redclos);
	}

	public StringImageButton(int p_i51136_1_, int p_i51136_2_, int p_i51136_3_, int p_i51136_4_, int p_i51136_5_,
			int p_i51136_6_, int p_i51136_7_, ResourceLocation p_i51136_8_, int p_i51136_9_, int p_i51136_10_,
			IPressable p_i51136_11_, String p_i51136_12_, int leth, boolean redclos) {
		super(p_i51136_1_, p_i51136_2_, p_i51136_3_, p_i51136_4_, p_i51136_5_, p_i51136_6_, p_i51136_7_, p_i51136_8_,
				p_i51136_9_, p_i51136_10_, p_i51136_11_, p_i51136_12_);
		this.lethi = leth;
		this.dredclos = redclos;
		this.cloer = 0;
	}

	public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
		super.renderButton(p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
		stringRenderButton(p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);

	}

	public void setStringColoer(int coloer) {
		this.cloer = coloer;
	}

	public void stringRenderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
		Minecraft minecraft = Minecraft.getInstance();
		FontRenderer fontrenderer = minecraft.fontRenderer;
		fontrenderer.drawString(this.getMessage(), this.x + this.width / 2 + lethi,
				this.y + (this.height - 8) / 2, cloer);

		if (dredclos) {
			mc.getTextureManager().bindTexture(SoundFileUploaderScreen.SFU_GUI_TEXTURE2);
			blit(this.x, this.y, 24, 149, 12, 12);
		}
	}

}
