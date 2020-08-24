package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.otyacraftengine.client.gui.widget.IkisugiWidget;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;

public class ScrollListButton extends IkisugiWidget {
    protected float nowSclooled;
    private ScrollListButton.IPressable pressed;
    protected List<ResourceLocation> locations;
    protected int onesize;
    protected int locationCont;

    protected ScrollBarSlider scrollBar;

    public ScrollListButton(int x, int y, int sizeX, int sizeY, int sizeOne, int count, ScrollBarSlider scrollBar, List<ResourceLocation> locations, ScrollListButton.IPressable pressed) {
        this(x, y, sizeX, sizeY, sizeOne, count, 0f, locations, pressed);
        this.scrollBar = scrollBar;
    }

    public ScrollListButton(int x, int y, int sizeX, int sizeY, int sizeOne, int count, float defaltscolled, List<ResourceLocation> locations, ScrollListButton.IPressable pressed) {
        super(x, y, sizeX, sizeY, new TranslationTextComponent("list"));
        this.pressed = pressed;
        this.locations = locations;
        this.onesize = sizeOne;
        this.locationCont = count;
        this.nowSclooled = defaltscolled;
    }

    public float getNowSclooled() {

        if (scrollBar != null)
            return scrollBar.getValue() / scrollBar.getMaxValue();

        return nowSclooled;
    }

    public void setSclooled(float nowsclooled) {
        this.nowSclooled = nowsclooled;
    }

    @Override
    public void renderBgByIKSG(MatrixStack matrix, int mouseX, int mouseY, float parTick) {
        int allsize = onesize * locationCont;
        int zure = 0;
        if (allsize > this.field_230689_k_) {
            int zuresize = allsize - field_230689_k_;
            zure = (int) (zuresize * getNowSclooled());
        }
        for (int i = 0; i < locationCont; i++) {
            int y = this.field_230691_m_ + i * onesize - zure;

            if (y < this.field_230691_m_ - onesize || y > this.field_230691_m_ + this.field_230689_k_)
                continue;

            renderOneList(matrix, this.field_230690_l_, y, i, y < this.field_230691_m_ ? field_230691_m_ - y : 0, y > this.field_230691_m_ + this.field_230689_k_ - onesize ? -(field_230691_m_ + field_230689_k_ - (y + onesize)) : 0);
        }
    }

    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(locations.get(num), matrix, x, y + upOver, 0, upOver, this.field_230688_j_, onesize - downOver, this.field_230688_j_, onesize );
    }

    @Override
    public void onClickByIKSG(double mouseX, double mouseY) {
        super.onClickByIKSG(mouseX, mouseY);
        this.pressed.onPress(this, 0);
    }


    @OnlyIn(Dist.CLIENT)
    public interface IPressable {
        void onPress(ScrollListButton button, int number);
    }
}
