package dev.felnull.imp.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.felnull.fnjl.util.FNForUtil;
import dev.felnull.imp.api.client.IamMusicPlayerClientAPI;
import dev.felnull.imp.api.client.MusicPlayerAccess;
import dev.felnull.imp.api.client.MusicSpeakerAccess;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public class DebugOutline {
    public static void render(PoseStack poseStack, MultiBufferSource multiBufferSource, double camX, double camY, double camZ) {
        var me = IamMusicPlayerClientAPI.getInstance().getMusicEngine();

        for (MusicPlayerAccess player : me.getMusicPlayers().values()) {
            for (MusicSpeakerAccess speaker : player.getSpeakers().values()) {
                renderSpeaker(poseStack, multiBufferSource, camX, camY, camZ, speaker);
            }
        }
    }

    private static void renderSpeaker(PoseStack poseStack, MultiBufferSource multiBufferSource, double camX, double camY, double camZ, MusicSpeakerAccess speaker) {
        var lineVC = multiBufferSource.getBuffer(RenderType.lines());
        var pos = speaker.getInfo().getPosition();
        float x = (float) (pos.x() - camX);
        float y = (float) (pos.y() - camY);
        float z = (float) (pos.z() - camZ);

        renderLineBox(poseStack, lineVC, x, y, z, -0.05f, -0.05f, -0.05f, 0.05f, 0.05f, 0.05f, 0xFFFFFFFF);

        float range = speaker.getInfo().getRange();
        renderLine(poseStack, lineVC, x, y, z, -range, 0, 0, range, 0, 0, 0xFF0000FF);
        renderLine(poseStack, lineVC, x, y, z, 0, -range, 0, 0, range, 0, 0xFF0000FF);
        renderLine(poseStack, lineVC, x, y, z, 0, 0, -range, 0, 0, range, 0xFF0000FF);

        renderLineSphere(poseStack, lineVC, x, y, z, range, 5);
    }

    private static void renderLineSphere(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float range, int corner) {
        float degree = 360f / 16f;

        poseStack.pushPose();
        // for (int i = 0; i < 16; i++) {
        renderLineQuarterSphere(poseStack, vertexConsumer, x, y, z, range, corner, 45f);
        // }
        poseStack.popPose();


    }

    private static void renderLineQuarterSphere(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float range, int corner, float zDegree) {
        float oneDegree = 90f / (float) (corner + 1);

        float px = range;
        float py = 0;
        for (int i = 0; i < corner + 1; i++) {
            float dr = (float) Math.toRadians(oneDegree * i);
            float rx = (float) (range * Math.cos(dr));
            float ry = (float) (range * Math.sin(dr));
            renderLine(poseStack, vertexConsumer, x, y, z, px, py, 0, rx, ry, 0, 0xFFFF0000);
            px = rx;
            py = ry;
        }
        renderLine(poseStack, vertexConsumer, x, y, z, px, py, 0, 0, range, 0, 0xFFFF0000);
    }

    private static void renderLineBox(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float stX, float stY, float stZ, float enX, float enY, float enZ, int color) {
        FNForUtil.forBoxEdge(stX, stY, stZ, enX, enY, enZ, ret -> {
            var st = ret.getLeft();
            var en = ret.getRight();
            renderLine(poseStack, vertexConsumer, x, y, z, st.getX(), st.getY(), st.getZ(), en.getX(), en.getY(), en.getZ(), color);
        });
    }

    private static void renderLine(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float stX, float stY, float stZ, float enX, float enY, float enZ, int color) {
        float nx = enX - stX;
        float ny = enY - stY;
        float nz = enZ - stZ;
        float t = Mth.sqrt(nx * nx + ny * ny + nz * nz);
        nx /= t;
        ny /= t;
        nz /= t;

        float r = (float) FastColor.ARGB32.red(color) / 255f;
        float g = (float) FastColor.ARGB32.green(color) / 255f;
        float b = (float) FastColor.ARGB32.blue(color) / 255f;
        float a = (float) FastColor.ARGB32.alpha(color) / 255f;

        var pose = poseStack.last();
        vertexConsumer.vertex(pose.pose(), stX + x, stY + y, stZ + z).color(r, g, b, a).normal(pose.normal(), nx, ny, nz).endVertex();
        vertexConsumer.vertex(pose.pose(), enX + x, enY + y, enZ + z).color(r, g, b, a).normal(pose.normal(), nx, ny, nz).endVertex();
    }
}
