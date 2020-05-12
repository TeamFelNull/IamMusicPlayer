package net.morimori.imp.client.screen;

import java.util.Map;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.morimori.imp.util.PlayerHelper;

public class FaseStringImageButton extends StringImageButton {
	private static Minecraft mc = Minecraft.getInstance();

	public boolean isDrawPlayerFase;
	public boolean isStringWidth;
	private boolean fileselectbuton = false;
	public boolean isReddish;

	private String faseplayername;
	public boolean isOtherPlayerFase;

	public FaseStringImageButton(int p_i51136_1_, int p_i51136_2_, int p_i51136_3_, int p_i51136_4_, int p_i51136_5_,
			int p_i51136_6_, int p_i51136_7_, ResourceLocation p_i51136_8_, int p_i51136_9_, int p_i51136_10_,
			IPressable p_i51136_11_, String p_i51136_12_, int leth, boolean redclos) {
		super(p_i51136_1_, p_i51136_2_, p_i51136_3_, p_i51136_4_, p_i51136_5_, p_i51136_6_, p_i51136_7_, p_i51136_8_,
				p_i51136_9_, p_i51136_10_, p_i51136_11_, p_i51136_12_, leth, redclos);

	}

	public FaseStringImageButton(int p_i51136_1_, int p_i51136_2_, int p_i51136_3_, int p_i51136_4_,
			int p_i51136_5_, int p_i51136_6_, int p_i51136_7_, ResourceLocation p_i51136_8_, int p_i51136_9_,
			int p_i51136_10_, IPressable p_i51136_11_, String p_i51136_12_, boolean draplayer, boolean stw) {
		this(p_i51136_1_, p_i51136_2_, p_i51136_3_, p_i51136_4_, p_i51136_5_, p_i51136_6_, p_i51136_7_, p_i51136_8_,
				p_i51136_9_, p_i51136_10_, p_i51136_11_, p_i51136_12_, draplayer);
	}

	public FaseStringImageButton(int p_i51136_1_, int p_i51136_2_, int p_i51136_3_, int p_i51136_4_,
			int p_i51136_5_, int p_i51136_6_, int p_i51136_7_, ResourceLocation p_i51136_8_, int p_i51136_9_,
			int p_i51136_10_, IPressable p_i51136_11_, String p_i51136_12_, boolean draplayer) {
		super(p_i51136_1_, p_i51136_2_, p_i51136_3_, p_i51136_4_, p_i51136_5_, p_i51136_6_, p_i51136_7_, p_i51136_8_,
				p_i51136_9_, p_i51136_10_, p_i51136_11_, p_i51136_12_, -23, false);
		this.isDrawPlayerFase = draplayer;

	}

	public void setFileSelectButton(boolean isfileselect) {
		fileselectbuton = isfileselect;

	}

	public void setFasResourceLocation(String name) {
		isOtherPlayerFase = true;

		faseplayername = name;

	}

	public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
		super.renderButton(p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
		if (isDrawPlayerFase) {
			if (!isOtherPlayerFase) {
				RenderSystem.pushMatrix();

				if (isReddish)
					RenderSystem.color4f(2.55f, 0.69f, 0.01f, 1.0F);
				else
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

				mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
				AbstractGui.blit(this.x + (fileselectbuton ? 0 : 2), this.y + (this.height - 8) / 2, 8, 8, 8, 8, 64,
						64);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.popMatrix();
			} else {
				if (!faseplayername.isEmpty()) {
					ResourceLocation faselocation;
					GameProfile GP = PlayerHelper.getPlayerTextuerProfile(faseplayername);
					Map<Type, MinecraftProfileTexture> map = mc.getSkinManager().loadSkinFromCache(GP);
					faselocation = map.containsKey(Type.SKIN)
							? mc.getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN)
							: DefaultPlayerSkin.getDefaultSkin(PlayerEntity.getUUID(GP));
					RenderSystem.pushMatrix();

					if (isReddish)
						RenderSystem.color4f(2.55f, 0.69f, 0.01f, 1.0F);
					else
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(faselocation);
					AbstractGui.blit(this.x + (fileselectbuton ? 0 : 2), this.y + (this.height - 8) / 2, 8, 8, 8, 8, 64,
							64);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					RenderSystem.popMatrix();
				}
			}
		}
	}
}
