package dev.turtywurty.testmod.network.s2c;

import dev.turtywurty.testmod.client.screen.FeaturePlacerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class S2CUpdateCFPacket {
    private final Collection<ResourceLocation> features;
    private final boolean isFirst;

    public S2CUpdateCFPacket(Collection<ResourceLocation> features, boolean isFirst) {
        this.features = features;
        this.isFirst = isFirst;
    }

    public S2CUpdateCFPacket(FriendlyByteBuf buf) {
        this(buf.readList(FriendlyByteBuf::readResourceLocation), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(this.features, FriendlyByteBuf::writeResourceLocation);
        buf.writeBoolean(this.isFirst);
    }

    public static boolean handle(S2CUpdateCFPacket pkt, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if(Minecraft.getInstance().screen instanceof FeaturePlacerScreen screen) {
                screen.setFeatures(pkt.features, pkt.isFirst);
            }
        });

        return true;
    }
}
