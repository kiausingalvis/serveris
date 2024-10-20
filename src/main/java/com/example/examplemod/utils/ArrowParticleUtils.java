package com.example.examplemod.utils;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EventBusSubscriber
public class ArrowParticleUtils {

    // Map to track arrows with their associated particle configurations
    public static final Map<AbstractArrow, List<ParticleConfig>> arrowsWithParticles = new HashMap<>();

    // Inner class to store particle configuration details
    static class ParticleConfig {
        SimpleParticleType particleType;
        float red, green, blue, size;
        int frequency;
        long lastSpawnTick;

        ParticleConfig(SimpleParticleType particleType, float red, float green, float blue, float size, int frequency) {
            this.particleType = particleType;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.size = size;
            this.frequency = frequency;
            this.lastSpawnTick = 0;
        }
    }

    // Add a single particle effect to an arrow
    public static void addSingleArrowParticle(AbstractArrow arrow, SimpleParticleType particleType, float red, float green, float blue, float size, int frequency) {
        List<ParticleConfig> configs = new ArrayList<>();
        configs.add(new ParticleConfig(particleType, red, green, blue, size, frequency));
        arrowsWithParticles.put(arrow, configs);
    }

    // Add multiple particle effects to an arrow
    public static void addMultipleArrowParticles(AbstractArrow arrow, List<ParticleConfig> configs) {
        arrowsWithParticles.put(arrow, configs);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (Map.Entry<AbstractArrow, List<ParticleConfig>> entry : arrowsWithParticles.entrySet()) {
                AbstractArrow arrow = entry.getKey();
                List<ParticleConfig> configs = entry.getValue();
                if (arrow.isAlive()) {
                    ServerLevel level = (ServerLevel) arrow.level;
                    long gameTime = level.getGameTime();
                    for (ParticleConfig config : configs) {
                        if (gameTime >= config.lastSpawnTick + config.frequency) {
                            level.sendParticles(config.particleType, arrow.getX(), arrow.getY(), arrow.getZ(), 1, config.red, config.green, config.blue, config.size);
                            config.lastSpawnTick = gameTime;
                        }
                    }
                }
            }
        }
    }
}
