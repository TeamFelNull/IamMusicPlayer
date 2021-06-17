package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.api.client.IMPClientRegistry;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.IMSDSmartRender;
import red.felnull.imp.client.gui.components.MSDSmartEditBox;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.music.loader.IMusicLoader;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;

public class CreateMusicMonitor extends CreateBaseMonitor {
    protected MSDSmartEditBox sourceTextBox;
    protected ResourceLocation musicLoaderLocation;
    private MusicSourceNextOrBackButton nextLoaderButton;

    public CreateMusicMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdMonitor.createMusic"), msdScreen, parentScreen, x, y, width, height);
        this.musicLoaderLocation = IMPClientRegistry.getLoaderLocations().get(0);
    }

    @Override
    public void init() {
        super.init();
        this.sourceTextBox = addCreateSmartTextEditBox(new TranslatableComponent("imp.msdSourceBox.name"), x + 101, y + 73, 95, n -> {

        });

        addRenderableWidget(new MusicSourceButton(x + 110, y + 104, 27, 15, n -> {
      /*      List<IMusicLoader> msls = IMPClientRegistry.getLoaders();
            int num = msls.indexOf(musicLoader);
            num++;
            if (num >= msls.size())
                num = 0;
            this.musicLoader = msls.get(num);*/
        }));

        this.nextLoaderButton = addRenderableWidget(new MusicSourceNextOrBackButton(x + 101, y + 104, new TranslatableComponent("imp.msdButton.musicSourceNext"), n -> {
            List<ResourceLocation> msls = IMPClientRegistry.getLoaderLocations();
            int num = msls.indexOf(musicLoaderLocation);
            num++;
            if (num >= msls.size())
                return;
            this.musicLoaderLocation = msls.get(num);
        }, false));

        addRenderableWidget(new MusicSourceNextOrBackButton(x + 137, y + 104, new TranslatableComponent("imp.msdButton.musicSourceBack"), n -> {

        }, true));
    }

    protected IMusicLoader getLoader() {
        return IMPClientRegistry.getLoader(musicLoaderLocation);
    }

    @Override
    public void tick() {
        super.tick();
        List<ResourceLocation> msls = IMPClientRegistry.getLoaderLocations();
        int lnum = msls.indexOf(musicLoaderLocation);
        nextLoaderButton.active = msls.size() > lnum;
    }

    @Override
    protected void created() {

    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        drawPrettyString(poseStack, new TranslatableComponent("imp.msdText.searchResults"), x + 4, y + 64, 0);
    }

    @Override
    public void renderBg(PoseStack poseStack, int mousX, int mousY, float parTick) {
        super.renderBg(poseStack, mousX, mousY, parTick);
        drawDarkBox(poseStack, x + 3, y + 73, 95, 46);
    }

    public class MusicSourceButton extends Button implements IMSDSmartRender {

        public MusicSourceButton(int i, int j, int k, int l, OnPress onPress) {
            super(i, j, k, l, new TranslatableComponent("imp.msdButton.musicSource"), onPress);
        }

        @Override
        public void renderButton(PoseStack poseStack, int i, int j, float f) {
            int k = this.getYImage(this.isHovered());
            drawSmartButtonBox(poseStack, x, y, width, height, k);
            this.renderBg(poseStack, getMinecraft(), i, j);

            getLoader().renderIcon(poseStack, this.x, this.y, this.width, this.height);
        }
    }

    public class MusicSourceNextOrBackButton extends Button implements IMSDSmartRender {
        private final boolean revers;


        public MusicSourceNextOrBackButton(int i, int j, Component component, OnPress onPress, boolean revers) {
            super(i, j, 9, 15, component, onPress);
            this.revers = revers;
        }

        @Override
        public void renderButton(PoseStack poseStack, int i, int j, float f) {
            int k = this.getYImage(this.isHovered());
            IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, this.x, this.y, 18 + (revers ? this.width : 0), 20 + (this.height * k), this.width, this.height);
            this.renderBg(poseStack, getMinecraft(), i, j);
        }
    }

}
