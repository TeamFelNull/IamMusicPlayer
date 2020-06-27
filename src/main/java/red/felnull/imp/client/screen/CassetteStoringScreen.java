package red.felnull.imp.client.screen;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.container.CassetteStoringContainer;
import red.felnull.imp.tileentity.CassetteStoringTileEntity;
import red.felnull.imp.util.ItemHelper;
import red.felnull.imp.util.SoundHelper;
import red.felnull.imp.util.StringHelper;

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
        String outst = SoundHelper.getSoundName(cassette);

        slipmaxs.put(num, outst.length());

        if (slips.containsKey(num)) {
            outst = StringHelper.slipString(outst, true, slips.get(num));
        }

        return outst;
    }

    @Override
    public void func_230430_a_(MatrixStack maxt, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.func_230446_a_(maxt);
        super.func_230430_a_(maxt, p_render_1_, p_render_2_, p_render_3_);
        this.func_230459_a_(maxt, p_render_1_, p_render_2_);
    }

    @Override
    protected void func_230450_a_(MatrixStack maxt, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(CS_GUI_TEXTURE);
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(maxt, xs, ys, 0, 0, this.xSize, this.ySize);
        RenderSystem.popMatrix();
        drawBagGrand(maxt);
    }

    private void drawString(MatrixStack maxt, String st, int x, int y, int col) {
        int xs = (this.field_230708_k_ - this.xSize) / 2;
        int ys = (this.field_230709_l_ - this.ySize) / 2;
        RenderSystem.pushMatrix();
        this.field_230712_o_.func_238405_a_(maxt, st, xs + x, ys + y, col);
        RenderSystem.popMatrix();
    }

    public void drawBagGrand(MatrixStack matx) {

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
                this.getMinecraft().getTextureManager().bindTexture(CS_GUI_TEXTURE);
                int xs = (this.field_230708_k_ - this.xSize) / 2;
                int ys = (this.field_230709_l_ - this.ySize) / 2;
                this.func_238474_b_(matx, xs + 7, ys + 7 + 18 * i, 0, 238, 54, 18);
                RenderSystem.popMatrix();
                drawString(matx, StringHelper.characterLimit(mc, getSlipedNameString(i), 40, false), 18,
                        12 + 18 * i, coloer);
            }
        }

        for (int i = 0; i < 8; ++i) {
            ItemStack cassette = tile.getCassette(i + 8);
            if (ItemHelper.isCassette(cassette) && SoundHelper.isWritedSound(cassette)) {
                RenderSystem.pushMatrix();
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.getMinecraft().getTextureManager().bindTexture(CS_GUI_TEXTURE);
                int xs = (this.field_230708_k_ - this.xSize) / 2;
                int ys = (this.field_230709_l_ - this.ySize) / 2;
                this.func_238474_b_(matx, xs + 115, ys + 7 + 18 * i, 0, 238, 54, 18);
                RenderSystem.popMatrix();
                drawString(matx, StringHelper.characterLimit(mc, getSlipedNameString(i + 8), 40, false), 126,
                        12 + 18 * i, coloer);
            }
        }

    }
}
