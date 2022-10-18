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
import net.minecraft.world.phys.Vec3;
import org.lwjgl.openal.AL11;

public class DebugSpeakerRangeRenderer {
    public static void render(PoseStack poseStack, MultiBufferSource multiBufferSource, double camX, double camY, double camZ) {
        var me = IamMusicPlayerClientAPI.getInstance().getMusicEngine();

        for (MusicPlayerAccess player : me.getMusicPlayers().values()) {
            for (MusicSpeakerAccess speaker : player.getSpeakers().values()) {
                renderSpeaker(poseStack, multiBufferSource, camX, camY, camZ, player, speaker);
            }
        }
    }

    private static void renderSpeaker(PoseStack poseStack, MultiBufferSource multiBufferSource, double camX, double camY, double camZ, MusicPlayerAccess playerAccess, MusicSpeakerAccess speaker) {
        var lineVC = multiBufferSource.getBuffer(RenderType.lines());
        var pos = speaker.getInfo().getPosition();
        float x = (float) (pos.x() - camX);
        float y = (float) (pos.y() - camY);
        float z = (float) (pos.z() - camZ);
        var listener = getListenerPos();
        double distance = listener.distanceTo(speaker.getInfo().getPosition());
        boolean inRange = distance <= speaker.getInfo().getRange();

        renderLineBox(poseStack, lineVC, x, y, z, -0.05f, -0.05f, -0.05f, 0.05f, 0.05f, 0.05f, 0xFFFFFFFF);

        float range = speaker.getInfo().getRange();
        renderLine(poseStack, lineVC, x, y, z, -range, 0, 0, range, 0, 0, 0xFF0000FF);
        renderLine(poseStack, lineVC, x, y, z, 0, -range, 0, 0, range, 0, 0xFF0000FF);
        renderLine(poseStack, lineVC, x, y, z, 0, 0, -range, 0, 0, range, 0xFF0000FF);


        renderLineSphere(poseStack, lineVC, x, y, z, range, Mth.clamp((int) range, 3, 50), playerAccess.isLoading() ? 0xFFFFFF00 : (inRange ? 0xFF00FF00 : 0xFFFF0000));
    }

    private static void renderLineSphere(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float range, int corner, int color) {
        renderLineQuarterSphereX(poseStack, vertexConsumer, x, y, z, range, range, corner, color);
        renderLineQuarterSphereX(poseStack, vertexConsumer, x, y, z, range, -range, corner, color);
        renderLineQuarterSphereX(poseStack, vertexConsumer, x, y, z, -range, range, corner, color);
        renderLineQuarterSphereX(poseStack, vertexConsumer, x, y, z, -range, -range, corner, color);

        renderLineQuarterSphereY(poseStack, vertexConsumer, x, y, z, range, range, corner, color);
        renderLineQuarterSphereY(poseStack, vertexConsumer, x, y, z, range, -range, corner, color);
        renderLineQuarterSphereY(poseStack, vertexConsumer, x, y, z, -range, range, corner, color);
        renderLineQuarterSphereY(poseStack, vertexConsumer, x, y, z, -range, -range, corner, color);

        renderLineQuarterSphereZ(poseStack, vertexConsumer, x, y, z, range, range, corner, color);
        renderLineQuarterSphereZ(poseStack, vertexConsumer, x, y, z, range, -range, corner, color);
        renderLineQuarterSphereZ(poseStack, vertexConsumer, x, y, z, -range, range, corner, color);
        renderLineQuarterSphereZ(poseStack, vertexConsumer, x, y, z, -range, -range, corner, color);
    }

    private static void renderLineQuarterSphereX(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float rangeX, float rangeY, int corner, int color) {
        float oneDegree = 90f / (float) (corner + 1);

        float px = rangeX;
        float py = 0;
        for (int i = 0; i < corner + 1; i++) {
            float dr = (float) Math.toRadians(oneDegree * i);
            float rx = (float) (rangeX * Math.cos(dr));
            float ry = (float) (rangeY * Math.sin(dr));
            renderLine(poseStack, vertexConsumer, x, y, z, px, py, 0, rx, ry, 0, color);
            px = rx;
            py = ry;
        }
        renderLine(poseStack, vertexConsumer, x, y, z, px, py, 0, 0, rangeY, 0, color);
    }

    private static void renderLineQuarterSphereY(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float rangeX, float rangeZ, int corner, int color) {
        float oneDegree = 90f / (float) (corner + 1);

        float px = rangeX;
        float pz = 0;
        for (int i = 0; i < corner + 1; i++) {
            float dr = (float) Math.toRadians(oneDegree * i);
            float rx = (float) (rangeX * Math.cos(dr));
            float rz = (float) (rangeZ * Math.sin(dr));
            renderLine(poseStack, vertexConsumer, x, y, z, px, 0, pz, rx, 0, rz, color);
            px = rx;
            pz = rz;
        }
        renderLine(poseStack, vertexConsumer, x, y, z, px, 0, pz, 0, 0, rangeZ, color);
    }

    private static void renderLineQuarterSphereZ(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float z, float rangeZ, float rangeY, int corner, int color) {
        float oneDegree = 90f / (float) (corner + 1);

        float pz = rangeZ;
        float py = 0;
        for (int i = 0; i < corner + 1; i++) {
            float dr = (float) Math.toRadians(oneDegree * i);
            float rz = (float) (rangeZ * Math.cos(dr));
            float ry = (float) (rangeY * Math.sin(dr));
            renderLine(poseStack, vertexConsumer, x, y, z, 0, py, pz, 0, ry, rz, color);
            pz = rz;
            py = ry;
        }
        renderLine(poseStack, vertexConsumer, x, y, z, 0, py, pz, 0, rangeY, 0, color);
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

    private static Vec3 getListenerPos() {
        float[] x = new float[1];
        float[] y = new float[1];
        float[] z = new float[1];

        AL11.alGetListener3f(AL11.AL_POSITION, x, y, z);

        return new Vec3(x[0], y[0], z[0]);
    }
}
