package net.challenges;

import net.challenges.gui.ChallengeGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class challengesclient implements ClientModInitializer {
    private static KeyMapping challengeMenuKey;
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("challenges","openmenu"));

    @Override
    public void onInitializeClient(){
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
