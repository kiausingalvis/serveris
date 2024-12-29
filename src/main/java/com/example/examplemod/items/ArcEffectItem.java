package com.example.examplemod.items;

import com.example.examplemod.utils.UNIQUEID_RETRIEVE;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ArcEffectItem extends Item {

    public ArcEffectItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW; // Example animation
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack itemStack = event.getItemStack();

        if (UNIQUEID_RETRIEVE.getUniqueId(player.getMainHandItem()).equals("CUMBLASTER")) {
            event.setCanceled(true); // Prevent default action
            spawnParticleArc(player);
        }
    }

    public static void spawnParticleArc(ServerPlayer player) {
        if (!(player.level instanceof ServerLevel level)) return;

        Vec3 playerPos = player.position();
        Vec3 lookVec = player.getLookAngle();

        // Adjust angle for arc calculation
        double arcHeight = 1.0; // Maximum height of the arc
        double range = 10.0;     // Total range of the arc

        // Iterate over points along the arc
        for (double t = 0; t <= 1.0; t += 0.05) { // 10 points in the arc
            double x = lookVec.x * range * t;
            double z = lookVec.z * range * t;
            double y = arcHeight * (4 * t * (1 - t)); // Parabola formula for arc

            Vec3 particlePos = playerPos.add(x, y+0.5, z);

            // Spawn particles at calculated positions
            level.sendParticles(
                    ParticleTypes.INSTANT_EFFECT, // White particle effect
                    particlePos.x, particlePos.y, particlePos.z,
                    1, // Number of particles
                    0, 0, 0, // Offset
                    0.0 // Speed
            );
            level.sendParticles(
                    ParticleTypes.FIREWORK, // White particle effect
                    particlePos.x, particlePos.y, particlePos.z,
                    1, // Number of particles
                    0, 0, 0, // Offset
                    0.0 // Speed
            );
        }
    }
}
