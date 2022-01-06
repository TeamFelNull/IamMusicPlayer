package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.components.SmartRadioButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.otyacraftengine.client.gui.components.RadioButton;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class CreatePlayListMMMonitor extends CreateBaseMMMonitor {
    private static final Component PUBLIC_ST_TEXT = new TranslatableComponent("imp.text.publishingSettings");
    private static final Component PUBLIC_RDO_TEXT = new TranslatableComponent("imp.radioButton.public");
    private static final Component PRIVATE_RDO_TEXT = new TranslatableComponent("imp.radioButton.private");
    private RadioButton publicRadio;
    private RadioButton privateRadio;

    public CreatePlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void create(ImageInfo imageInfo, String name) {

    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        this.publicRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 5, getStartY() + 132, 20, 20, PUBLIC_RDO_TEXT, getPublishingType() == PublishingType.PUBLIC, true, () -> new RadioButton[]{this.publicRadio, this.privateRadio}, n -> {
            setPublishingType(PublishingType.PUBLIC);
        }));
        this.privateRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 95, getStartY() + 132, 20, 20, PRIVATE_RDO_TEXT, getPublishingType() == PublishingType.PRIVATE, true, () -> new RadioButton[]{this.publicRadio, this.privateRadio}, n -> {
            setPublishingType(PublishingType.PRIVATE);
        }));
    }

    @Override
    public boolean canCreate(MusicManagerBlockEntity blockEntity) {
        return super.canCreate(blockEntity) && getPublishingType(blockEntity) != null;
    }

    @Override
    public List<Component> getNotEntered(List<Component> names, MusicManagerBlockEntity blockEntity) {
        if (getPublishingType(blockEntity) == null)
            names.add(PUBLIC_ST_TEXT);
        return super.getNotEntered(names, blockEntity);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        drawSmartText(poseStack, PUBLIC_ST_TEXT, getStartX() + 5, getStartY() + 123);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        renderSmartTextSprite(poseStack, multiBufferSource, PUBLIC_ST_TEXT, 5, 123, OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);

        renderSmartRadioButton(poseStack, multiBufferSource, 5, 132, OERenderUtil.MIN_BREADTH * 3, 20, 20, i, j, onPxW, onPxH, monitorHeight, PUBLIC_RDO_TEXT, getPublishingType(blockEntity) == PublishingType.PUBLIC);
        renderSmartRadioButton(poseStack, multiBufferSource, 95, 132, OERenderUtil.MIN_BREADTH * 3, 20, 20, i, j, onPxW, onPxH, monitorHeight, PRIVATE_RDO_TEXT, getPublishingType(blockEntity) == PublishingType.PRIVATE);
    }

    public PublishingType getPublishingType() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getPublishingType(musicManagerBlock);
        return null;
    }

    public PublishingType getPublishingType(MusicManagerBlockEntity musicManagerBlockEntity) {
        return PublishingType.getTypeByName(musicManagerBlockEntity.getMyPublishing());
    }

    private void setPublishingType(PublishingType publishingType) {
        getScreen().insPublishing(publishingType == null ? "" : publishingType.name);
    }

    private static enum PublishingType {
        PUBLIC("public"), PRIVATE("private");
        private final String name;

        private PublishingType(String name) {
            this.name = name;
        }

        public static PublishingType getTypeByName(String type) {
            for (PublishingType value : values()) {
                if (value.name.equals(type))
                    return value;
            }
            return null;
        }
    }
}
