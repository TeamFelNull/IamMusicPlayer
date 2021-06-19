package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.api.client.IMPClientRegistry;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.IMSDSmartRender;
import red.felnull.imp.client.gui.components.MSDSmartEditBox;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.music.loader.IMusicLoader;
import red.felnull.imp.client.music.loader.IMusicSearchable;
import red.felnull.imp.client.renderer.PlayImageRenderer;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.music.resource.MusicSource;
import red.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CreateMusicMonitor extends CreateBaseMonitor {
    private final List<IMusicLoader.SearchData> searchDataList = new ArrayList<>();
    private MusicSearchThread musicsearchthread;
    private MusicPlayCheckThread musicplaycheckthread;
    protected MSDSmartEditBox sourceTextBox;
    protected ResourceLocation musicLoaderLocation;
    private MusicSourceNextOrBackButton nextLoaderButton;
    private MusicSourceNextOrBackButton backLoaderButton;
    private SearchMusicsFixedButtonsList searchMusicsButtonsList;
    private IMusicLoader.SearchData checkableData;

    public CreateMusicMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdMonitor.createMusic"), msdScreen, parentScreen, x, y, width, height);
        this.musicLoaderLocation = IMPClientRegistry.getLoaderLocations().get(0);
    }

    @Override
    public void init() {
        super.init();
        this.sourceTextBox = addCreateSmartTextEditBox(new TranslatableComponent("imp.msdTextBox.source"), x + 101, y + 73, 95, n -> {
            checkStop();
            searchStop();
            musicplaycheckthread = new MusicPlayCheckThread(n);
            musicplaycheckthread.start();
        });


        addRenderableWidget(new MusicSourceButton(x + 110, y + 104, 27, 15, n -> {
        }));

        this.nextLoaderButton = addRenderableWidget(new MusicSourceNextOrBackButton(x + 137, y + 104, new TranslatableComponent("imp.msdButton.musicSourceNext"), n -> {
            List<ResourceLocation> msls = IMPClientRegistry.getLoaderLocations();
            int num = msls.indexOf(musicLoaderLocation);
            num++;
            if (num >= msls.size())
                return;
            this.musicLoaderLocation = msls.get(num);
            sourceTextBox.setValue("");
            searchStop();
            checkStop();
            searchDataList.clear();
        }, true));

        this.backLoaderButton = addRenderableWidget(new MusicSourceNextOrBackButton(x + 101, y + 104, new TranslatableComponent("imp.msdButton.musicSourceBack"), n -> {
            List<ResourceLocation> msls = IMPClientRegistry.getLoaderLocations();
            int num = msls.indexOf(musicLoaderLocation);
            num--;
            this.musicLoaderLocation = msls.get(num);
            sourceTextBox.setValue("");
            searchStop();
            checkStop();
            searchDataList.clear();
        }, false));

        this.searchMusicsButtonsList = this.addRenderableWidget(new SearchMusicsFixedButtonsList(x + 4, y + 74, 93, 44, 4, new TranslatableComponent("imp.msdText.searchResults"), searchDataList, n -> {
            sourceTextBox.setValue(n.item().identifier());
        }));
    }

    @Override
    public void disable() {
        super.disable();
        searchStop();
        checkStop();
    }

    private void checkStop() {
        if (musicplaycheckthread != null) {
            musicplaycheckthread.stopped();
            musicplaycheckthread = null;
        }
        checkableData = null;
    }

    private void searchStop() {
        if (musicsearchthread != null) {
            musicsearchthread.stopped();
            musicsearchthread = null;
        }
        searchDataList.clear();
    }

    protected IMusicLoader getLoader() {
        return IMPClientRegistry.getLoader(musicLoaderLocation);
    }


    @Override
    public void tick() {
        super.tick();
        List<ResourceLocation> msls = IMPClientRegistry.getLoaderLocations();
        int lnum = msls.indexOf(musicLoaderLocation);
        nextLoaderButton.active = lnum + 1 < msls.size();
        backLoaderButton.active = lnum > 0;
        searchMusicsButtonsList.active = searchMusicsButtonsList.visible = getLoader() instanceof IMusicSearchable && !searchDataList.isEmpty();
        sourceTextBox.setMessage((getLoader() instanceof IMusicSearchable) ? new TranslatableComponent("imp.msdTextBox.sourceOrSearch") : new TranslatableComponent("imp.msdTextBox.source"));
    }

    @Override
    protected void created() {
        String name = nameTextBox.getValue();
        MusicSource musicSource = new MusicSource(musicLoaderLocation, checkableData.identifier(), checkableData.duration());
        ImageInfo image = imageInfo;

        insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.PLAYLIST);
    }

    @Override
    protected boolean canCreate() {
        return super.canCreate() && checkableData != null;
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        if (!searchDataList.isEmpty()) {
            drawPrettyString(poseStack, new TranslatableComponent("imp.msdText.searchResults"), x + 4, y + 64, 0);
        } else {
            if (checkableData != null) {
                drawPrettyString(poseStack, new TranslatableComponent("imp.msdText.musicInfo"), x + 3, y + 65, 0);
                drawPrettyString(poseStack, new TranslatableComponent("imp.msdText.musicTitle", checkableData.name()), x + 3, y + 65 + (getFont().lineHeight + 1), 0);

                if (!checkableData.author().isEmpty())
                    drawPrettyString(poseStack, new TranslatableComponent("imp.msdText.musicAuthor", checkableData.author()), x + 3, y + 65 + (getFont().lineHeight + 1) * 2, 0);

                drawPrettyString(poseStack, new TranslatableComponent("imp.msdText.musicDuration", checkableData.duration() + "ms"), x + 3, y + 65 + (getFont().lineHeight + 1) * (checkableData.author().isEmpty() ? 2 : 3), 0);
            }
        }
    }

    @Override
    public void renderBg(PoseStack poseStack, int mousX, int mousY, float parTick) {
        super.renderBg(poseStack, mousX, mousY, parTick);
        if (!searchDataList.isEmpty()) {
            drawDarkBox(poseStack, x + 3, y + 73, 95, 46);
        }
    }

    private class MusicPlayCheckThread extends Thread {
        private boolean stopped;
        private final String identifier;

        private MusicPlayCheckThread(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public void run() {
            IMusicLoader.SearchData data = getLoader().getPlayMusicData(identifier);
            if (stopped)
                return;

            if (data != null) {
                if (!identifier.equals(data.identifier())) {
                    sourceTextBox.setValue(data.identifier());
                    return;
                }

                if (stopped)
                    return;

                checkableData = data;
                if (data.setName() != null) {
                    nameTextBox.setValue(data.setName());
                } else {
                    if (nameTextBox.getValue().isEmpty())
                        nameTextBox.setValue(data.name());
                }

                if (data.image() != null)
                    imageInfo = data.image();

                return;
            }

            if (stopped)
                return;

            if (getLoader() instanceof IMusicSearchable) {
                searchStop();
                musicsearchthread = new MusicSearchThread(identifier);
                musicsearchthread.start();
            }
        }

        public void stopped() {
            this.stopped = true;
        }
    }

    private class MusicSearchThread extends Thread {
        private boolean stopped;
        private final String searchIdentifier;

        private MusicSearchThread(String searchIdentifier) {
            this.searchIdentifier = searchIdentifier;
        }

        @Override
        public void run() {
            searchDataList.clear();

            if (stopped)
                return;

            List<IMusicLoader.SearchData> scd = ((IMusicSearchable) getLoader()).search(searchIdentifier);

            if (stopped)
                return;

            searchDataList.addAll(scd);
        }

        public void stopped() {
            this.stopped = true;
        }
    }

    private class MusicSourceButton extends Button implements IMSDSmartRender {

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

    private class MusicSourceNextOrBackButton extends Button implements IMSDSmartRender {
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

    private class SearchMusicsFixedButtonsList extends FixedButtonsList<IMusicLoader.SearchData> implements IMSDSmartRender {

        public SearchMusicsFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<IMusicLoader.SearchData> list, Consumer<PressState<IMusicLoader.SearchData>> onPress) {
            super(x, y, w, h, num, name, list, n -> new TextComponent(n.name()), onPress);
        }

        @Override
        protected void renderOneButton(PoseStack poseStack, IMusicLoader.SearchData item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
            int k = this.getYImage(this.isHovered(bnum));
            drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);
            boolean samune = item.image() != null;
            drawPrettyString(poseStack, (MutableComponent) getMessage(lnum), x + 3 + (samune ? getOneButtonHeight() - 2 : 0), y + ((float) this.getOneButtonHeight() - 8f) / 2f, 0);

            if (samune) {
                PlayImageRenderer.getInstance().render(item.image(), poseStack, x + 1, y + 1, getOneButtonHeight() - 2, false);
            }
        }
    }
}
