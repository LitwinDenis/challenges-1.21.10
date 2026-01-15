package net.challenges;

import net.challenges.gui.ChallengeGUI;
import net.challenges.timer.TimerHandler;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
//import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class challengesclient implements ClientModInitializer {
    private static KeyMapping challengeMenuKey;
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("challenges","openmenu"));


    @Override
    public void onInitializeClient(){


        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("start")
                .executes(context ->{
                    if (TimerHandler.isTimerEnabled) {
                        TimerHandler.startTime = System.currentTimeMillis();
                        TimerHandler.isRunning = true;
                        context.getSource().sendFeedback(Component.literal("Timer started"));
                    }else {
                        context.getSource().sendFeedback(Component.literal("Please Actived in Challenge Menu first!"));
                    }
                    return 1;
                })));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("reset")
                    .executes(context -> {
                        TimerHandler.isRunning = false;
                        context.getSource().sendFeedback(Component.literal("Timer reseted!"));
                        return 1;
                    }));
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.player != null && TimerHandler.isRunning) {
                    String timeText = TimerHandler.getFormattedTime();
                    client.player.displayClientMessage(Component.literal("§6" + timeText), true);
                }
            });
        });
        challengeMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                CATEGORY
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client ->{
            while (challengeMenuKey.consumeClick()){
                client.setScreen(new ChallengeGUI());
            }
        });
    }
}
