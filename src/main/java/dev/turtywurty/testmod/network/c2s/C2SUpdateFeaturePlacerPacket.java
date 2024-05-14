package dev.turtywurty.testmod.network.c2s;

import dev.turtywurty.testmod.TestMod;
import dev.turtywurty.testmod.init.ItemInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SUpdateFeaturePlacerPacket {
    private final ResourceLocation selectedFeature;

    public C2SUpdateFeaturePlacerPacket(ResourceLocation selectedFeature) {
        this.selectedFeature = selectedFeature;
    }

    public C2SUpdateFeaturePlacerPacket(FriendlyByteBuf buffer) {
        this(buffer.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.selectedFeature);
    }

    public static boolean handle(C2SUpdateFeaturePlacerPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null)
                return;

            ItemStack handStack = player.getMainHandItem();
            if (handStack.isEmpty() || !handStack.is(ItemInit.FEATURE_PLACER.get()))
                return;

            handStack.getOrCreateTagElement(TestMod.MODID).putString("Feature", pkt.selectedFeature.toString());
        });

        return true;
    }
}
