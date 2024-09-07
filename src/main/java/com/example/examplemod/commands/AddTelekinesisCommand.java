package com.example.examplemod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AddTelekinesisCommand {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("addtelekinesis")
                .executes(context -> {
                    return addTelekinesis(context.getSource());
                })
        );
    }

    private static int addTelekinesis(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ItemStack itemStack = player.getMainHandItem();

        if (itemStack.isEmpty()) {
            source.sendFailure(Component.literal("You must hold an item to enchant it with Telekinesis!"));
            return 0;
        }

        ListTag lore = itemStack.getOrCreateTagElement("display").getList("Lore", 8);
        lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("§a§lTelekinesis"))));
        itemStack.getOrCreateTagElement("display").put("Lore", lore);

        source.sendSuccess(Component.literal("Added Telekinesis to the item's lore!"), true);
        return 1;
    }
}
