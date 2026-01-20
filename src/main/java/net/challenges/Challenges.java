package net.challenges;

import net.challenges.networking.ModPackets;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.challenges.networking.ModPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.server.MinecraftServer;
import java.util.List;

public class Challenges implements ModInitializer {
	public static final String MOD_ID = "challenges";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static boolean serverIsRunning = false;
    public static long serverTotalTicks = 0;

	@Override
	public void onInitialize() {
        ModPackets.registerPackets();

        ServerPlayNetworking.registerGlobalReceiver(ModPackets.TimerControlPayload.ID, (payload, context) ->{
            boolean enableTimer = payload.enableTimer();
            boolean reset = payload.reset();

            ServerPlayer player = context.player();
            MinecraftServer server = context.server();

            if (player.hasPermissions(2)){
                server.execute(() ->{
                    serverIsRunning = enableTimer;
                    if (reset) {
                        serverTotalTicks = 0;
                    }
                    syncTimerToAllPlayers(server.getPlayerList().getPlayers());
                });
            }
        });
        ServerTickEvents.END_SERVER_TICK.register(server ->{
            if (serverIsRunning){
                serverTotalTicks++;

                if(serverTotalTicks %20 == 0) {
                    syncTimerToAllPlayers(server.getPlayerList().getPlayers());
                }
            }
        });
    }

    public static void syncTimerToAllPlayers(List<ServerPlayer> players){
        for(ServerPlayer player: players){
            ServerPlayNetworking.send(player, new ModPackets.TimerSyncPayload(serverIsRunning, serverTotalTicks));
        }
    }
}