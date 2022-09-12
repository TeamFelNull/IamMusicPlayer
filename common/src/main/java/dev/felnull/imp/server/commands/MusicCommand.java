package dev.felnull.imp.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.api.IamMusicPlayerAPIOld;
import dev.felnull.imp.api.music.MusicRingerAccess;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MusicCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var literalCommandNode = dispatcher.register(Commands.literal(IamMusicPlayer.MODID).requires(n -> n.hasPermission(2))
                .then(Commands.literal("ringer").executes(n -> ringerInfo(n.getSource(), null))
                        .then(Commands.literal("info").executes(n -> ringerInfo(n.getSource(), null))
                                .then(Commands.argument("dimension", DimensionArgument.dimension()).executes(n -> ringerInfo(n.getSource(), DimensionArgument.getDimension(n, "dimension")))))
                        .then(Commands.literal("list").executes(n -> ringerList(n.getSource(), null))
                                .then(Commands.argument("dimension", DimensionArgument.dimension()).executes(n -> ringerList(n.getSource(), DimensionArgument.getDimension(n, "dimension")))))));

        dispatcher.register(Commands.literal("imp").requires(n -> n.hasPermission(2)).redirect(literalCommandNode));
    }

    private static int ringerInfo(CommandSourceStack src, ServerLevel level) {
        if (level == null) {
            src.sendSuccess(Component.translatable("commands.imp.ringer.info.all", IamMusicPlayerAPIOld.getRingerCount(), IamMusicPlayerAPIOld.getPlayingRingerCount()), false);
        } else {
            var name = level.dimension().location();
            src.sendSuccess(Component.translatable("commands.imp.ringer.info", IamMusicPlayerAPIOld.getRingerCount(level), IamMusicPlayerAPIOld.getPlayingRingerCount(level), name), false);
        }
        return 1;
    }

    private static int ringerList(CommandSourceStack src, ServerLevel level) {
        List<MusicRingerAccess> ringers = level != null ? IamMusicPlayerAPIOld.getRingers(level) : IamMusicPlayerAPIOld.getRingers();

        if (level == null) {
            if (ringers.isEmpty()) {
                src.sendFailure(Component.translatable("commands.imp.ringer.list.all.notFound"));
            } else {
                src.sendSuccess(Component.translatable("commands.imp.ringer.list.all"), false);
                for (MusicRingerAccess ringer : ringers) {
                    src.sendSuccess(Component.translatable("commands.imp.ringer.list.all.entry" + (ringer.isPlaying() ? ".playing" : ""), ringer.getName(), createPosComponent(ringer.getSpatialPosition(), ringer.getLevel()), ringer.getLevel().dimension().location()), false);
                }
            }
        } else {
            if (ringers.isEmpty()) {
                src.sendFailure(Component.translatable("commands.imp.ringer.list.notFound", level.dimension().location()));
            } else {
                src.sendSuccess(Component.translatable("commands.imp.ringer.list", level.dimension().location()), false);
                for (MusicRingerAccess ringer : ringers) {
                    src.sendSuccess(Component.translatable("commands.imp.ringer.list.entry" + (ringer.isPlaying() ? ".playing" : ""), ringer.getName(), createPosComponent(ringer.getSpatialPosition(), ringer.getLevel())), false);
                }
            }
        }

        return 1;
    }

    private static Component createPosComponent(Vec3 pos, ServerLevel level) {
        return ComponentUtils.wrapInSquareBrackets(Component.translatable("chat.coordinates", pos.x, pos.y, pos.z)).withStyle((style) -> style.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/execute in " + level.dimension().location() + " run tp @s " + pos.x + " " + pos.y + " " + pos.z)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.coordinates.tooltip"))));
    }
}
