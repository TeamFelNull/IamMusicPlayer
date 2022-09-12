package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class PlayListBaseMMMonitor extends ImageNameBaseMMMonitor {
    protected static final Component PUBLIC_ST_TEXT = Component.translatable("imp.text.publishingSettings");
    protected static final Component INITIAL_AUTHORITY_TEXT = Component.translatable("imp.text.initialAuthority");
    protected static final Component PUBLIC_RDO_TEXT = Component.translatable("imp.radioButton.public");
    protected static final Component PRIVATE_RDO_TEXT = Component.translatable("imp.radioButton.private");
    protected static final Component READONLY_RDO_TEXT = Component.translatable("imp.radioButton.readonly");
    protected static final Component MEMBER_RDO_TEXT = Component.translatable("imp.radioButton.member");

    public PlayListBaseMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        drawSmartText(poseStack, PUBLIC_ST_TEXT, getStartX() + 5, getStartY() + 131);
        drawSmartText(poseStack, INITIAL_AUTHORITY_TEXT, getStartX() + 189, getStartY() + 131);

        drawSmartText(poseStack, getPlayerListName(), getStartX() + 189, getStartY() + 13);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        renderSmartTextSprite(poseStack, multiBufferSource, PUBLIC_ST_TEXT, 5, 131, OERenderUtils.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);
        renderSmartTextSprite(poseStack, multiBufferSource, INITIAL_AUTHORITY_TEXT, 189, 131, OERenderUtils.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);

        renderSmartTextSprite(poseStack, multiBufferSource, getPlayerListName(), 189, 13, OERenderUtils.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
    }

    @Nullable
    protected PublishingType getPublishingType() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getPublishingType(musicManagerBlock);
        return null;
    }

    @Nullable
    protected PublishingType getPublishingType(MusicManagerBlockEntity musicManagerBlockEntity) {
        return PublishingType.getTypeByName(musicManagerBlockEntity.getPublishing(mc.player));
    }

    @Nullable
    protected InitialAuthorityType getInitialAuthorityType() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getInitialAuthorityType(musicManagerBlock);
        return null;
    }

    @Nullable
    protected InitialAuthorityType getInitialAuthorityType(MusicManagerBlockEntity musicManagerBlockEntity) {
        return InitialAuthorityType.getTypeByName(musicManagerBlockEntity.getInitialAuthority(mc.player));
    }

    @NotNull
    protected String getInvitePlayerName(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getInvitePlayerName(mc.player);
    }

    @NotNull
    protected String getInvitePlayerName() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getInvitePlayerName(musicManagerBlock);
        return "";
    }

    @NotNull
    protected List<UUID> getInvitePlayers() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getInvitePlayers(musicManagerBlock);
        return Collections.emptyList();
    }

    @NotNull
    protected List<UUID> getInvitePlayers(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getInvitePlayers(mc.player);
    }

    abstract protected Component getPlayerListName();

    protected static enum PublishingType {
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

        public String getName() {
            return name;
        }
    }

    protected static enum InitialAuthorityType {
        READ_ONLY("read_only"), MEMBER("member");
        private final String name;

        private InitialAuthorityType(String name) {
            this.name = name;
        }

        public static InitialAuthorityType getTypeByName(String type) {
            for (InitialAuthorityType value : values()) {
                if (value.name.equals(type))
                    return value;
            }
            return null;
        }

        @NotNull
        public String getName() {
            return name;
        }
    }

}
