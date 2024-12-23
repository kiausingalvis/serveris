package com.example.examplemod.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.example.examplemod.utils.UNIQUEID_RETRIEVE.getUniqueId;

@Mod.EventBusSubscriber
public class HeartEffectUtils {
    static int timer = 5;

    // Function to calculate heart-shaped points
    private static Vec3 calculateHeartPoint(double t, double scale) {
        double x = scale * 16 * Math.pow(Math.sin(t), 3);
        double y = scale * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t));
        return new Vec3(x, y, 0);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer player)) return;
        if (!(player.level instanceof ServerLevel level)) return;

        // Check if the player is wearing the correct helmet
        ItemStack helmet = player.getInventory().getArmor(3); // Slot 3 = helmet
        if (getUniqueId(helmet).equals("HEARTHELMET") && timer<=0) {
            spawnHeartParticlesAbovePlayer(player, level);
            timer=20;
        }else{
            timer--;
        }
    }

    private static void spawnHeartParticlesAbovePlayer(ServerPlayer player, ServerLevel level) {
        Vec3 playerPos = player.position();
        double scale = 0.05; // Scale of the heart
        double height = 3; // Height above the player's head
        Vec3 center = playerPos.add(0, height, 0);

        // Generate heart shape using parametric equations
        for (double t = 0; t < 2 * Math.PI; t += 0.2) {
            Vec3 heartPoint = calculateHeartPoint(t, scale);
            Vec3 particlePos = center.add(heartPoint.x, heartPoint.y, heartPoint.z);

            // Spawn particles at calculated positions
            level.sendParticles(
                    ParticleTypes.FLAME, // Particle type
                    particlePos.x, particlePos.y, particlePos.z,
                    1, // Number of particles
                    0, 0, 0, // Offset
                    0.0 // Speed
            );
        }
    }
}
