package com.example.examplemod.commands;

import com.example.examplemod.Config;
import com.example.examplemod.ItemConfigManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.example.examplemod.utils.CustomItemUtil.giveCustomArmor;
import static com.example.examplemod.utils.CustomItemUtil.giveCustomItem;
import static com.example.examplemod.utils.CircleEffect.startRadius;

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

        Item netheriteChestplate = Items.NETHERITE_CHESTPLATE;
        Item albuGay = Items.DIAMOND_SHOVEL;
        ServerPlayer player = source.getPlayerOrException();

        //giveCustomArmor(player, "GAMBLER_CHESTPLATE", netheriteChestplate, "GAMBLER'S CHESTPLATE", "Pimpalo Chestplate", "PIMPALAS", 69,69,69,69,69,69,69,69);
        //giveCustomItem(player, "PIDERELLA", albuGay, "PIDERELLA", "teleports you x amount of blocks", "teleport a few blocks on right click", "RARE", 2011, 11, 0, 0, 0, 9,40,true, "SHIFT + RIGHT CLICK", "Teleports you 5693 amount of blocks");
        Config.PlayerBank.getBalance(player);
        System.out.println(ItemConfigManager.loadItemData("PIDERELLA").getDamage());
        String formattedBankValue = String.format("%,d", Config.PlayerBank.getBalance(player));
        source.sendSuccess(Component.literal("§2Your Bank Balance: §a" + formattedBankValue + "§2$"),true );
        return 1;
    }
}
