package red.felnull.imp.client.renderer.item;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import red.felnull.imp.client.renderer.model.CassetteBakedModel;
import red.felnull.imp.item.CassetteTapeItem;
import red.felnull.imp.sound.WorldPlayListSoundData;
import red.felnull.imp.util.PictuerUtil;
import red.felnull.imp.util.RenderHelper;
import red.felnull.imp.util.SoundHelper;
import red.felnull.imp.util.TextureHelper;


public class CassetteItemRenderer extends ItemStackTileEntityRenderer {
    private static Minecraft mc = Minecraft.getInstance();

    public static Map<CassetteTapeItem, IBakedModel> casettomodels = new HashMap<CassetteTapeItem, IBakedModel>();

    TransformType transtyepe;

    @Override
    public void func_239207_a_(ItemStack stack, TransformType trtype, MatrixStack matx, IRenderTypeBuffer bff, int p_228364_4_, int p_228364_5_) {

        ItemRenderer ir = mc.getItemRenderer();
        CassetteBakedModel md = (CassetteBakedModel) ir.getItemModelWithOverrides(stack, null, null);
        transtyepe = trtype;
        RenderHelper.matrixPush(matx);

        RenderHelper.matrixTranslatef(matx, 0.5f, 0.5f, 0.5f);

        if (casettomodels.containsKey(stack.getItem())) {
            ir.renderItem(stack, transtyepe,
                    isTransTyape(TransformType.FIRST_PERSON_LEFT_HAND)
                            || isTransTyape(TransformType.THIRD_PERSON_LEFT_HAND),
                    matx, bff, p_228364_4_, p_228364_5_,
                    casettomodels.get(stack.getItem()));

        }

        TextureManager tm = mc.getTextureManager();
        if (SoundHelper.isWritedSound(stack) && isTransTyape(TransformType.GUI)) {
            RenderSystem.pushMatrix();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.rotatef(180, 1, 0, 0);
            float scale = 0.03f;

            RenderSystem.scalef(scale, scale, scale);
            RenderSystem.translatef(-0.5f / scale, -0.5f / scale, 0);

            WorldPlayListSoundData wplsd = WorldPlayListSoundData.getWorldPlayListData(stack);

            int pxsize = 0;
            int pysize = 0;
            if (TextureHelper.isImageNotExists(wplsd.getSoundData().album_image_uuid)) {
                pxsize = 128;
                pysize = 128;
            } else {
                pxsize = PictuerUtil.getImage(wplsd.getSoundData().album_image_uuid).getWidth();
                pysize = PictuerUtil.getImage(wplsd.getSoundData().album_image_uuid).getHeight();
            }

            tm.bindTexture(WorldPlayListSoundData.getWorldPlayListData(stack).getAlbumImage());
            AbstractGui.func_238463_a_(matx,0, 0, 0, 0, pxsize / 8, pysize / 8,
                    pxsize / 8, pysize / 8);
            RenderSystem.popMatrix();

        }

        //		FontRenderer fr = mc.fontRenderer;
        //		RenderHelper.matrixRotateDegreefY(matx, 180);
        //		RenderHelper.matrixRotateDegreefZ(matx, 180);
        //		float nazo = (float) (-fr.getStringWidth("test") / 2);
        //		fr.func_228079_a_("test", nazo, 0, 0, false, matx.func_227866_c_().func_227870_a_(), bff, false, 0,
        //				p_228364_4_);
        //
        //		ResourceLocation test = new ResourceLocation(IamMusicPlayer.MODID,
        //				"textures/gui/container/soundfile_uploader_2.png");
        //
        //		mc.getTextureManager().bindTexture(test);
        //		AbstractGui.fill(matx.func_227866_c_().func_227870_a_(), 12, 149, 12, 12, 256);

        RenderHelper.matrixPop(matx);
    }

    private boolean isTransTyape(TransformType type) {
        return transtyepe == type;
    }
}
