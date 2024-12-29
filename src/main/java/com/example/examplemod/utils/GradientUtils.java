package com.example.examplemod.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class GradientUtils {

    /**
     * Creates a Component with a custom gradient applied to the text.
     *
     * @param text       The text to which the gradient will be applied.
     * @param startColor The starting color of the gradient (hexadecimal, e.g., 0xFFA500 for orange).
     * @param endColor   The ending color of the gradient (hexadecimal, e.g., 0xFF4500 for red).
     * @return A Component with a gradient applied to the text.
     */
    public static Component applyGradient(String text, int startColor, int endColor) {
        Component result = Component.empty(); // Start with an empty component
        int length = text.length();

        // Extract RGB components of start and end colors
        int startRed = (startColor >> 16) & 0xFF;
        int startGreen = (startColor >> 8) & 0xFF;
        int startBlue = startColor & 0xFF;

        int endRed = (endColor >> 16) & 0xFF;
        int endGreen = (endColor >> 8) & 0xFF;
        int endBlue = endColor & 0xFF;

        // Apply gradient to each character
        for (int i = 0; i < length; i++) {
            float ratio = (float) i / (length - 1); // Calculate gradient ratio
            int red = (int) (startRed + ratio * (endRed - startRed));
            int green = (int) (startGreen + ratio * (endGreen - startGreen));
            int blue = (int) (startBlue + ratio * (endBlue - startBlue));
            int color = (red << 16) | (green << 8) | blue; // Combine RGB into a single integer

            // Create a styled component for this character
            Component charComponent = Component.literal(String.valueOf(text.charAt(i)))
                    .setStyle(Style.EMPTY.withColor(color));

            // Append to the result
            result = ((MutableComponent) result).append(charComponent);
        }

        return result;
    }
}
