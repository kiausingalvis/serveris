package com.example.examplemod.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Delay {
    /**
     * a method to delay {@code run} by delayTicks
     * @author Kapitencraft
     * @param delayTicks time (in ticks) to delay
     * @param run runnable to execute at the end of the delay
     */
    public static void schedule(int delayTicks, Runnable run) {
        new Object() {
            private int ticks = 0;
            private float waitTicks;

            public void start(int waitTicks) {
                this.waitTicks = waitTicks;
                MinecraftForge.EVENT_BUS.register(this);
            }
            @SubscribeEvent
            public void tick(TickEvent.ServerTickEvent event) {
                if (event.phase == TickEvent.Phase.END) {
                    this.ticks += 1;
                    if (this.ticks >= this.waitTicks)
                        end();
                }
            }
            private void end() {
                MinecraftForge.EVENT_BUS.unregister(this);
                run.run();
            }
        }.start(delayTicks);
    }
}
