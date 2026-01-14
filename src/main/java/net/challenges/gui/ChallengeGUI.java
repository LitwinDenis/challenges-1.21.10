package net.challenges.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ChallengeGUI extends Screen {
    public static boolean noJumpChallengeActive = false;
    public ChallengeGUI(){
        super(Component.literal("Challenges"));
    }
    @Override
    protected void init(){
        Checkbox noJumpCheckBox = Checkbox.builder(Component.literal("No Jump Challenge"),this.font)
                .pos(this.width / 2 - 100, 60)
                .selected(noJumpChallengeActive)
                .onValueChange((checkbox, checked) ->{
                  noJumpChallengeActive = checked;
                  System.out.println("No Jump:" + checked);
                })
                .build();
        this.addRenderableWidget(noJumpCheckBox);
    }
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }
    @Override
    public boolean isPauseScreen(){
        return false;
    }
}






