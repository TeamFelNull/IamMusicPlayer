package red.felnull.imp.client.gui.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.client.gui.components.IMSDSmartRender;
import red.felnull.imp.client.renderer.PlayImageRenderer;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.dynamic.DynamicMusicList;

public class ClientCassetteTapeTooltip implements ClientTooltipComponent, IMSDSmartRender {
    private DynamicMusicList musicList;

    public ClientCassetteTapeTooltip(DynamicMusicList musicList) {
        this.musicList = musicList;
    }

    @Override
    public int getHeight() {
        int defa = 25;
        defa += Math.min(musicList.getMusics().size(), 3) * 17;
        return defa;
    }

    @Override
    public int getWidth(Font font) {
        int max = 27 + font.width(musicList.getName());
        max = Math.max(27 + font.width(new TextComponent(musicList.getMusics().size() + "Soungs")), max);
        int ct = 0;
        for (Music music : musicList.getMusics()) {
            if (ct > 3)
                continue;
            max = Math.max(19 + font.width(music.getName()), max);
            ct++;
        }
        return max;
    }

    @Override
    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int k, TextureManager textureManager) {
        PlayImageRenderer renderer = PlayImageRenderer.getInstance();
        renderer.draw(musicList.getImage(), poseStack, x, y, 24);

        int ct = 0;
        for (Music music : musicList.getMusics()) {
            if (ct > 3)
                continue;
            renderer.draw(music.getImage(), poseStack, x, y + 25 + (17 * ct), 16);
            ct++;
        }

    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        font.drawInBatch(musicList.getName(), x + 27, y, -1, true, matrix4f, bufferSource, false, 0, 15728880);
        font.drawInBatch(new TextComponent(musicList.getMusics().size() + "Soungs"), x + 27, y + font.lineHeight + 1, -1, true, matrix4f, bufferSource, false, 0, 15728880);

        int ct = 0;
        for (Music music : musicList.getMusics()) {
            if (ct > 3)
                continue;
            font.drawInBatch(music.getName(), x + 19, y + 25 + (ct * 17), -1, true, matrix4f, bufferSource, false, 0, 15728880);
            ct++;
        }
    }
}
