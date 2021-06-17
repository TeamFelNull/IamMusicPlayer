package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.api.client.IMPClientRegistry;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.IMSDSmartRender;
import red.felnull.imp.client.gui.components.MSDSmartEditBox;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.music.loader.IMusicLoader;
import red.felnull.imp.client.music.loader.LavaPlayerLoader;
import red.felnull.imp.client.music.loader.YoutubeLavaPlayerLoader;
import red.felnull.imp.client.renderer.PlayImageRenderer;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class CreateMusicMonitor extends CreateBaseMonitor {
    protected MSDSmartEditBox sourceTextBox;
    protected ResourceLocation musicLoaderLocation;
    private MusicSourceNextOrBackButton nextLoaderButton;
    private MusicSourceNextOrBackButton backLoaderButton;
    private final List<AudioTrack> searchTracks = new ArrayList<>();

    public CreateMusicMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdMonitor.createMusic"), msdScreen, parentScreen, x, y, width, height);
        this.musicLoaderLocation = IMPClientRegistry.getLoaderLocations().get(0);

        LavaPlayerLoader lpl = ((LavaPlayerLoader) IMPClientRegistry.getLoader(new ResourceLocation(IamMusicPlayer.MODID, "youtube")));
        lpl.getAudioPlayerManager().loadItemOrdered(UUID.randomUUID(), "ytsearch:野獣先輩", new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                searchTracks.add(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                searchTracks.addAll(playlist.getTracks());
            }

            @Override
            public void noMatches() {
                System.out.println("noMatches");
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                ex.printStackTrace();
            }
        });

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

        this.nextLoaderButton = addRenderableWidget(new MusicSourceNextOrBackButton(x + 137, y + 104, new TranslatableComponent("imp.msdButton.musicSourceNext"), n -> {
            List<ResourceLocation> msls = IMPClientRegistry.getLoaderLocations();
            int num = msls.indexOf(musicLoaderLocation);
            num++;
            this.musicLoaderLocation = msls.get(num);
        }, true));

        this.backLoaderButton = addRenderableWidget(new MusicSourceNextOrBackButton(x + 101, y + 104, new TranslatableComponent("imp.msdButton.musicSourceBack"), n -> {
            List<ResourceLocation> msls = IMPClientRegistry.getLoaderLocations();
            int num = msls.indexOf(musicLoaderLocation);
            num--;
            this.musicLoaderLocation = msls.get(num);
        }, false));

        this.addRenderableWidget(new SearchMusicsFixedButtonsList(x + 4, y + 74, 93, 44, 4, new TextComponent("SearchResults"), searchTracks, n -> {

        }));
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

    public class SearchMusicsFixedButtonsList extends FixedButtonsList<AudioTrack> implements IMSDSmartRender {
        private static final Map<String, SizeScale> SCALES = new HashMap<>();

        public SearchMusicsFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<AudioTrack> list, Consumer<PressState<AudioTrack>> onPress) {
            super(x, y, w, h, num, name, list, n -> new TextComponent(n.getInfo().title), onPress);
        }

        @Override
        protected void renderOneButton(PoseStack poseStack, AudioTrack item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
            int k = this.getYImage(this.isHovered(bnum));
            drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);

            boolean samune = getLoader() instanceof YoutubeLavaPlayerLoader;

            drawPrettyString(poseStack, (MutableComponent) getMessage(lnum), x + 3 + (samune ? getOneButtonHeight() - 2 : 0), y + ((float) this.getOneButtonHeight() - 8f) / 2f, 0);

            if (samune) {
                float w = 0;
                float h = 0;

                if (SCALES.containsKey(item.getIdentifier())) {
                    w = SCALES.get(item.getIdentifier()).w;
                    h = SCALES.get(item.getIdentifier()).h;
                } else {
                    try {
                        URL url = new URL(String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", item.getIdentifier()));
                        BufferedImage image = ImageIO.read(url);
                        w = image.getWidth() >= image.getHeight() ? 1 : (float) image.getHeight() / image.getWidth();
                        h = image.getHeight() >= image.getWidth() ? 1 : (float) image.getWidth() / image.getHeight();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                PlayImageRenderer.getInstance().render(new ImageInfo(ImageInfo.ImageType.YOUTUBE_THUMBNAIL, "", w, h), poseStack, x + 1, y + 1, getOneButtonHeight() - 2);
            }
        }

        private static record SizeScale(float w, float h) {

        }
    }
}
