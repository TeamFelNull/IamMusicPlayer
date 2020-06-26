package net.morimori.imp.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public class IMPCommands {
    public static void registerCommand(CommandDispatcher<CommandSource> d) {
        ReloadWorldPlaylistCommand.register(d);
    }
}
