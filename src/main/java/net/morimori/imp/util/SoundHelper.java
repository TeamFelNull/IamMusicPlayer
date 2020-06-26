package net.morimori.imp.util;

import java.io.IOException;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.client.screen.IMPSoundSlider;
import net.morimori.imp.item.CassetteTapeItem;
import net.morimori.imp.item.IMPItems;
import net.morimori.imp.sound.ClientSoundPlayer;
import net.morimori.imp.sound.INewSoundPlayer;
import net.morimori.imp.sound.PlayData;
import net.morimori.imp.sound.PlayData.PlayDatasTypes;
import net.morimori.imp.sound.SoundPos;
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.sound.WorldSoundKey;

public class SoundHelper {

    public static boolean canPlay(ItemStack stack) {
        return isWritedSound(stack);
    }

    public static ItemStack writeSoundOnCassette(ItemStack stack, WorldPlayListSoundData WPLSD) {

        if (!ItemHelper.isCassette(stack))
            return stack;

        if (!isWritedSound(stack) || ((CassetteTapeItem) stack.getItem()).canOverwrite()) {

            stack = ((CassetteTapeItem) stack.getItem()).afterWriting(stack);

            WorldPlayListSoundData.setWorldPlayList(stack, WPLSD);
        }
        return stack;
    }

    public static ItemStack deleteSound(ItemStack stack) {

        if (!(stack.getItem() instanceof CassetteTapeItem))
            return ItemStack.EMPTY;

        if (((CassetteTapeItem) stack.getItem()).canOverwrite()) {

            ItemStack stacki = stack.copy();

            CompoundNBT tag = stacki.getOrCreateTag();

            tag.remove("WorldPlayListSoundData");

            return stacki;
        }

        return new ItemStack(IMPItems.MAGNETIC_TAPE, stack.getCount());
    }

    public static boolean isWritedSound(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.contains("WorldPlayListSoundData");

    }

    public static String getSoundName(WorldSoundKey wsk) {

        return StringHelper.deleteExtension(wsk.getName());
    }

    public static String getSoundName(ItemStack stack) {
        if (!isWritedSound(stack))
            return stack.getDisplayName().getString();

        return getSoundName(new WorldSoundKey(WorldPlayListSoundData.getWorldPlayListData(stack)));
    }

    public static boolean canWriteCassette(ItemStack stack) {
        if (ItemHelper.isCassette(stack)) {
            return isWritedSound(stack)
                    ? ((CassetteTapeItem) stack.getItem()).canOverwrite()
                    ? ((CassetteTapeItem) stack.getItem()).canWrite(stack)
                    : false
                    : ((CassetteTapeItem) stack.getItem()).canWrite(stack);
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static float getVolumeFromSoundPos(SoundPos pos, float motovol) {
        Minecraft mc = IamMusicPlayer.proxy.getMinecraft();

        double kyori = pos.distance(mc.player.func_226277_ct_(), mc.player.func_226278_cu_() + 1,
                mc.player.func_226281_cx_());

        double mot = getDistanceAndSoundPosFromVolume(motovol);

        if (kyori <= mot) {
            return motovol;
        } else if (kyori >= mot * 2) {
            return 0;
        }

        double v1 = kyori - mot;
        float v2 = 1 - (float) (v1 / mot);

        return v2 * motovol;
    }

    public static double getDistanceAndSoundPosFromVolume(float motovol) {
        return 16 * motovol;
    }

    @OnlyIn(Dist.CLIENT)
    public static float getOptionVolume() {
        Minecraft mc = IamMusicPlayer.proxy.getMinecraft();
        float master = mc.gameSettings.getSoundLevel(SoundCategory.MASTER);

        return IMPSoundSlider.AllSoundVolume <= master ? IMPSoundSlider.AllSoundVolume : master;
    }

    public static long getSoundLength(PlayData pd, MinecraftServer ms) {

        if (pd.type == PlayDatasTypes.FILE) {
            try {
                Mp3File mfile = new Mp3File(pd.path.toString());
                return mfile.getLengthInMilliseconds();
            } catch (UnsupportedTagException | InvalidDataException | IOException e) {
                return -1;
            }

        } else if (pd.type == PlayDatasTypes.WORLD) {
            try {
                Mp3File mfile = new Mp3File(pd.wsk.getServerPath(ms));
                return mfile.getLengthInMilliseconds();
            } catch (UnsupportedTagException | InvalidDataException | IOException e) {
                return -1;
            }
        } else if (pd.type == PlayDatasTypes.URL_STREAM) {
            return -1;
        }

        return 0;

    }

    public static void soundPlayerTick(INewSoundPlayer isp, World worldIn) {

        if (!isp.canExistence()) {
            return;
        }

        if (!worldIn.isRemote) {
            long leth = getSoundLength(isp.getSound(), worldIn.getServer());
            long nowtime = System.currentTimeMillis();
            if (isp.getLastTime() != 0) {
                if (isp.isPlayed()) {
                    long keka = nowtime - isp.getLastTime();
                    long gokei = isp.getPosition() + keka;
                    if (isp.isLoop()) {
                        if (keka >= leth) {
                            int co = (int) (keka / leth);
                            long s = keka - leth * co;
                            isp.setPosition(s);
                        }
                    } else {
                        isp.setPosition(gokei);
                    }
                }
            }
            isp.setLastTime(nowtime);

            if (isp.isPlayed()) {

                if (isp.isLoop()) {
                    if (isp.getPosition() != -1 && isp.getPosition() >= leth) {
                        isp.setPlayed(false);
                        isp.setPosition(0);

                        isp.setPlayed(true);
                    }
                } else {
                    if (isp.getPosition() != -1 && isp.getPosition() >= leth) {
                        isp.setPlayed(false);
                    }
                }

            } else {
                if (isp.getPosition() != -1 && isp.getPosition() >= leth) {
                    isp.setPosition(0);
                }
            }

            if (isp.isReset()) {
                isp.setPosition(0);
            }

        } else {
            if (!ClientSoundPlayer.INSTANS.playdSounds.containsValue(isp)) {
                ClientSoundPlayer.INSTANS.playdSounds.put(isp.getUuid(), isp);
            }
            ClientSoundPlayer.INSTANS.caplSound.put(isp.getUuid(), true);
        }

    }
}
