package dev.turtywurty.testmod.network.c2s;

import dev.turtywurty.testmod.network.PacketManager;
import dev.turtywurty.testmod.network.s2c.S2CUpdateCFPacket;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class C2SRequestCFPacket {
    public void encode(FriendlyByteBuf buffer) {
    }

    public C2SRequestCFPacket() {
    }

    public C2SRequestCFPacket(FriendlyByteBuf buffer) {
    }

    public static boolean handle(C2SRequestCFPacket pkt, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender == null)
                return;

            ServerLevel level = sender.serverLevel();
            RegistryAccess registryAccess = level.registryAccess();
            Registry<ConfiguredFeature<?, ?>> registry = registryAccess.registryOrThrow(Registries.CONFIGURED_FEATURE);

            Set<ResourceKey<ConfiguredFeature<?, ?>>> registryKeySet = registry.registryKeySet();
            // split the set into multiple sets of 100 elements
            var group = new AtomicInteger();
            for (Collection<ResourceLocation> set : registryKeySet.stream().map(ResourceKey::location).collect(Collectors.groupingBy(it -> group.getAndIncrement() / 100)).values()) {
                PacketManager.sendToClient(new S2CUpdateCFPacket(set, group.get() == 0), () -> sender);
            }
        });

        return true;
    }
}
