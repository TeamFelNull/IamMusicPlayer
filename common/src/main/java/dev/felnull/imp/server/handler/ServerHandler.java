package dev.felnull.imp.server.handler;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.felnull.imp.server.commands.MusicCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ServerHandler {
    public static void init() {
        CommandRegistrationEvent.EVENT.register(ServerHandler::registerCommand);
    }

    private static void registerCommand(CommandDispatcher<CommandSourceStack> commandDispatcher, Commands.CommandSelection commandSelection) {
        MusicCommand.register(commandDispatcher);
    }
}
