package dev.turtywurty.testmod.network;

import dev.turtywurty.testmod.TestMod;
import dev.turtywurty.testmod.network.c2s.C2SRequestCFPacket;
import dev.turtywurty.testmod.network.c2s.C2SUpdateFeaturePlacerPacket;
import dev.turtywurty.testmod.network.s2c.S2CUpdateCFPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PacketManager {
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(TestMod.MODID, "main"))
            .clientAcceptedVersions(a -> true)
            .serverAcceptedVersions(a -> true)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();

    public static void init() {
        int id = 0;

        CHANNEL.messageBuilder(C2SRequestCFPacket.class, id++)
                .encoder(C2SRequestCFPacket::encode)
                .decoder(C2SRequestCFPacket::new)
                .consumerNetworkThread(C2SRequestCFPacket::handle)
                .add();

        CHANNEL.messageBuilder(S2CUpdateCFPacket.class, id++)
                .encoder(S2CUpdateCFPacket::encode)
                .decoder(S2CUpdateCFPacket::new)
                .consumerNetworkThread(S2CUpdateCFPacket::handle)
                .add();

        CHANNEL.messageBuilder(C2SUpdateFeaturePlacerPacket.class, id++)
                .encoder(C2SUpdateFeaturePlacerPacket::encode)
                .decoder(C2SUpdateFeaturePlacerPacket::new)
                .consumerNetworkThread(C2SUpdateFeaturePlacerPacket::handle)
                .add();

        TestMod.LOGGER.info("Registered {} packets", id);
    }

    public static void sendToClient(Object msg, Supplier<ServerPlayer> player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(player), msg);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }
}
