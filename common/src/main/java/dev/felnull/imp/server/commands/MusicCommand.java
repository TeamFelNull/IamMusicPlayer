package dev.felnull.imp.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.api.IamMusicPlayerAPI;
import dev.felnull.imp.server.music.ringer.IMusicRinger;
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
        var literalCommandNode = dispatcher.register(Commands.literal(IamMusicPlayer.MODID)
                .then(Commands.literal("ringer").requires(n -> n.hasPermission(2))
                        .then(Commands.literal("info").executes(n -> ringerInfo(n.getSource(), null))
                                .then(Commands.argument("dimension", DimensionArgument.dimension()).executes(n -> ringerInfo(n.getSource(), DimensionArgument.getDimension(n, "dimension")))))
                        .then(Commands.literal("list").executes(n -> ringerList(n.getSource(), null))
                                .then(Commands.argument("dimension", DimensionArgument.dimension()).executes(n -> ringerList(n.getSource(), DimensionArgument.getDimension(n, "dimension")))))));

        dispatcher.register(Commands.literal("imp").requires(n -> n.hasPermission(2)).redirect(literalCommandNode));
    }

    private static int ringerInfo(CommandSourceStack src, ServerLevel level) {
        if (level == null) {
            src.sendSuccess(new TranslatableComponent("commands.imp.ringer.info.all", IamMusicPlayerAPI.getRingerCount(), IamMusicPlayerAPI.getPlayingRingerCount()), false);
        } else {
            var name = level.dimension().location();
            src.sendSuccess(new TranslatableComponent("commands.imp.ringer.info", IamMusicPlayerAPI.getRingerCount(level), IamMusicPlayerAPI.getPlayingRingerCount(level), name), false);
        }
        return 1;
    }

    private static int ringerList(CommandSourceStack src, ServerLevel level) {
        List<IMusicRinger> ringers = level != null ? IamMusicPlayerAPI.getRingers(level) : IamMusicPlayerAPI.getRingers();

        if (level == null) {
            if (ringers.isEmpty()) {
                src.sendFailure(new TranslatableComponent("commands.imp.ringer.list.all.notFound"));
            } else {
                src.sendSuccess(new TranslatableComponent("commands.imp.ringer.list.all"), false);
                for (IMusicRinger ringer : ringers) {
                    src.sendSuccess(new TranslatableComponent("commands.imp.ringer.list.all.entry" + (ringer.isRingerPlaying(level) ? ".playing" : ""), ringer.getRingerName(ringer.getRingerLevel()), createPosComponent(ringer.getRingerVec3Position(level), ringer.getRingerLevel()), ringer.getRingerLevel().dimension().location()), false);
                }
            }
        } else {
            if (ringers.isEmpty()) {
                src.sendFailure(new TranslatableComponent("commands.imp.ringer.list.notFound", level.dimension().location()));
            } else {
                src.sendSuccess(new TranslatableComponent("commands.imp.ringer.list", level.dimension().location()), false);
                for (IMusicRinger ringer : ringers) {
                    src.sendSuccess(new TranslatableComponent("commands.imp.ringer.list.entry" + (ringer.isRingerPlaying(level) ? ".playing" : ""), ringer.getRingerName(ringer.getRingerLevel()), createPosComponent(ringer.getRingerVec3Position(level), ringer.getRingerLevel())), false);
                }
            }
        }

        return 1;
    }

    private static Component createPosComponent(Vec3 pos, ServerLevel level) {
        return ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("chat.coordinates", pos.x, pos.y, pos.z)).withStyle((style) -> style.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/execute in " + level.dimension().location() + " run tp @s " + pos.x + " " + pos.y + " " + pos.z)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("chat.coordinates.tooltip"))));
    }
}