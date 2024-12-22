package com.example.examplemod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class QuestionCommand {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("iamaretardpleasebanme")
                .then(Commands.argument("choice", StringArgumentType.string())
                        .executes(context -> {
                            String choice = StringArgumentType.getString(context, "choice");
                            if (choice.equals("yes")) {
                                // Handle Yes action
                                banPerson(context.getSource().getPlayerOrException(), "§4Cannot be BLACK!!!!!!!!!");
                            } else if (choice.equals("no")) {
                                // Handle No action
                                context.getSource().sendSuccess(Component.literal("You chose: No!"), false);
                            }
                            return 1;
                        })));
    }
    public static void banPerson(ServerPlayer player, String reason){
        player.server.getPlayerList().getBans().add(new UserBanListEntry(player.getGameProfile(), null, "Server", null, reason));
        player.connection.disconnect(Component.literal("You have been banned: " + reason));
    }
}
