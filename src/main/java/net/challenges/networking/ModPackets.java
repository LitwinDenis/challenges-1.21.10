package net.challenges.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class ModPackets {

    public record TimerSyncPayload(boolean isRunning, long ticks) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<TimerSyncPayload> ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("challengemod", "timer_sync"));

        public static final StreamCodec<FriendlyByteBuf, TimerSyncPayload> CODEC = StreamCodec.ofMember(
                TimerSyncPayload::write, TimerSyncPayload::new
        );

        public TimerSyncPayload(FriendlyByteBuf buf) {
            this(buf.readBoolean(), buf.readLong());
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeBoolean(this.isRunning);
            buf.writeLong(this.ticks);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public record TimerControlPayload(boolean enableTimer, boolean reset) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<TimerControlPayload> ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("challengemod", "timer_control"));

        public static final StreamCodec<FriendlyByteBuf, TimerControlPayload> CODEC = StreamCodec.ofMember(
                TimerControlPayload::write, TimerControlPayload::new
        );

        public TimerControlPayload(FriendlyByteBuf buf) {
            this(buf.readBoolean(), buf.readBoolean());
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeBoolean(this.enableTimer);
            buf.writeBoolean(this.reset);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public static void registerPackets() {
        PayloadTypeRegistry.playS2C().register(TimerSyncPayload.ID, TimerSyncPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TimerControlPayload.ID, TimerControlPayload.CODEC);
    }
}