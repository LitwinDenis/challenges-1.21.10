package net.challenges;

import net.challenges.gui.ChallengeGUI;
import net.challenges.networking.ModPackets;
import net.challenges.timer.TimerHandler;
import net.challenges.timer.TimerJson;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;



public class challengesclient implements ClientModInitializer {
    private static KeyMapping challengeMenuKey;
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("challenges","openmenu"));


    @Override
    public void onInitializeClient(){

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.TimerSyncPayload.ID, (payload, context) -> {
            // Wir holen die Daten direkt aus dem Payload-Objekt
            boolean isRunning = payload.isRunning();
            long ticks = payload.ticks();

            // Auf dem Client Thread ausführen
            context.client().execute(() -> {
                TimerHandler.isRunning = isRunning;
                TimerHandler.totalTicks = ticks;
            });
        });


        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("start")
                .executes(context ->{
                    if (TimerHandler.isTimerEnabled) {
                        TimerHandler.start();

                        context.getSource().sendFeedback(Component.literal("Timer started"));
                    }else {
                        context.getSource().sendFeedback(Component.literal("Please Actived in Challenge Menu first!"));
                    }
                    return 1;
                })));
        ClientTickEvents.END_CLIENT_TICK.register(client ->{
            while (challengeMenuKey.consumeClick()) {
                client.setScreen(new ChallengeGUI());
            }
            if (client.player != null){
                TimerHandler.update(client.isPaused());
                if (TimerHandler.isRunning){
                    String timeText = TimerHandler.getFormattedTime();
                    String color = client.isPaused() ? "§7(Paused) §r" : "§6";
                    client.player.displayClientMessage(Component.literal(color + timeText), true);
                }
            }
        });
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
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->{
            TimerJson.load();
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->{
            TimerJson.save();
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{

        }));
    }
}
