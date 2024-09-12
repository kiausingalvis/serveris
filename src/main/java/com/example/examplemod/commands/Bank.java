package com.example.examplemod.commands;

import com.example.examplemod.Config;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.example.examplemod.utils.CustomItemUtil.giveCustomArmor;
import static com.example.examplemod.utils.CustomItemUtil.giveCustomItem;

public class Bank {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("bank")
                .executes(context -> {
                    return Bank(context.getSource());
                })
        );
    }
    public static int Bank(CommandSourceStack source) throws CommandSyntaxException {
        Item item = Items.BLAZE_ROD;
        Item netheriteChestplate = Items.NETHERITE_CHESTPLATE;
        ServerPlayer player = source.getPlayerOrException();
        giveCustomArmor(player, "GAMBLER_CHESTPLATE", netheriteChestplate, "GAMBLER'S CHESTPLATE", "Pimpalo Chestplate", "PIMPALAS", 69,69,69,69,69,69,69,69);
        giveCustomItem(player, "TERMINATOR", item, "TERMINATOR", "Shoots arrows, idk", "TRIPLE ARROWS", "DIVINE", 69,69,69,69,69,69);
        Config.PlayerBank.getBalance(player);
        String formattedBankValue = String.format("%,d", Config.PlayerBank.getBalance(player));
        source.sendSuccess(Component.literal("§2Your Bank Balance: §a" + formattedBankValue + "§2$"),true );
        return 1;
    }
}
