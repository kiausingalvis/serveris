package com.example.examplemod.commands;

import com.example.examplemod.Config;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SellHand {
    static int added_balance = 0;
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("sellhand")
                             .executes(context -> SellHand(context.getSource()))
        );
    }
    public static int SellHand(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if(player.getMainHandItem().getItem()== Items.SUGAR_CANE){
            int Sell_item_count = player.getMainHandItem().getCount();
            int sell_item_cost = 100;
            added_balance = Sell_item_count * sell_item_cost;
            Config.PlayerBank.addBalance(player,added_balance);
            source.sendSuccess(Component.literal("§2Sold §7x" + player.getMainHandItem().getCount() + " " + player.getMainHandItem().getItem() + " §2for: §a" + added_balance + "§2$"),true );
            player.getMainHandItem().setCount(0);
        return 1;
        }
        else{
            source.sendSuccess(Component.literal("§cNo items were found to sell"),true );
        return 0;
        }
    }
}
