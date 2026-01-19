package net.challenges.timer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;

public class TimerJson {
    private static final Gson GSON = new Gson();
    private static final File CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("challenge_mod_timers").toFile();

    private static String getCurrentWorldId() {
        Minecraft client = Minecraft.getInstance();

        if (client.getCurrentServer() != null) {
            return "server_" + client.getCurrentServer().ip.replaceAll("[^a-zA-Z0-9.-]", "_");
        }else if (client.hasSingleplayerServer()) {
            return "world_" + client.getSingleplayerServer().getWorldData().getLevelName().replaceAll("[^a-zA-Z0-9.-]", "_");
        }
        return "unknown";
    }

    public static void save() {
        if (!CONFIG_DIR.exists()){
            CONFIG_DIR.mkdirs();
        }
        String worldId = getCurrentWorldId();
        if (worldId.equals("unknown")) return;
        File file = new File(CONFIG_DIR, worldId + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            JsonObject json = new JsonObject();
            json.addProperty("totalTicks", TimerHandler.totalTicks);
            json.addProperty("isRunning", TimerHandler.isRunning);

            GSON.toJson(json, writer);
            System.out.println("Timer saved:" + worldId);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void load() {
        String worldId = getCurrentWorldId();
        File file = new File(CONFIG_DIR, worldId + ".json");

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);

                if (json.has("totalTicks")) {
                    TimerHandler.totalTicks = json.get("totalTicks").getAsLong();
                }
                if (json.has("isRunning")) {
                    TimerHandler.isRunning = json.get("isRunning").getAsBoolean();
                }
                System.out.println("Timer loaded:" + worldId);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            TimerHandler.totalTicks = 0;
            TimerHandler.isRunning = false;
        }
    }
}
