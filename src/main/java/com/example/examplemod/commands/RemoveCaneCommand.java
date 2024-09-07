package com.example.examplemod.commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class RemoveCaneCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("removecane")
                .requires(source -> source.hasPermission(2)) // Require OP permission level (2)
                .then(Commands.argument("amount", IntegerArgumentType.integer(1)) // Require positive integer amount argument
                        .executes(context -> {
                            int amount = IntegerArgumentType.getInteger(context, "amount");
                            return removeCane(context.getSource(), amount);
                        }))
        );
    }

    private static int removeCane(CommandSourceStack source, int amount) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Inventory playerInventory = player.getInventory();
        int removedCount = 0;

        // Loop through player's inventory to find and remove sugar cane
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack stack = playerInventory.getItem(i);
            if (stack.getItem() == Items.SUGAR_CANE) {
                // Determine how much of the item to remove
                int countToRemove = Math.min(stack.getCount(), amount - removedCount);

                // Remove the item stack or decrease count if more than needed
                if (countToRemove > 0) {
                    if (countToRemove < stack.getCount()) {
                        stack.shrink(countToRemove);
                    } else {
                        playerInventory.setItem(i, ItemStack.EMPTY);
                    }
                    removedCount += countToRemove;
                }

                // Exit the loop if the required amount has been removed
                if (removedCount >= amount) {
                    break;
                }
            }
        }

        // Send feedback to the player about the action
        Component feedback;
        if (removedCount > 0) {
            feedback = Component.literal("Removed ").append(Component.literal(String.valueOf(removedCount))).append(Component.literal(" sugar cane."));
        } else {
            feedback = Component.literal("No sugar cane found or removed.");
        }
        source.sendSuccess(feedback, true);

        return removedCount > 0 ? 1 : 0; // Return success if any items were removed
    }
}
