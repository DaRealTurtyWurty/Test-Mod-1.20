package dev.turtywurty.testmod.item;

import dev.turtywurty.testmod.TestMod;
import dev.turtywurty.testmod.client.screen.FeaturePlacerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

public class FeaturePlacerItem extends Item {
    public FeaturePlacerItem(Properties properties) {
        super(properties);
    }

    private static boolean checkLoaded(ServerLevel level, ChunkPos fromPos, ChunkPos toPos) {
        return ChunkPos.rangeClosed(fromPos, toPos).allMatch(position -> level.isLoaded(position.getWorldPosition()));
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        ItemStack stack = context.getItemInHand();
        CompoundTag nbt = stack.getOrCreateTagElement(TestMod.MODID);
        if (nbt.contains("Feature", Tag.TAG_STRING)) {
            String featureKey = nbt.getString("Feature");
            var location = ResourceLocation.tryParse(featureKey);
            if (location != null) {
                RegistryAccess registryAccess = level.registryAccess();
                Registry<ConfiguredFeature<?, ?>> registry = registryAccess.registryOrThrow(Registries.CONFIGURED_FEATURE);
                Holder.Reference<ConfiguredFeature<?, ?>> holder = registry.getHolderOrThrow(ResourceKey.create(Registries.CONFIGURED_FEATURE, location));
                ConfiguredFeature<?, ?> feature = holder.value();

                ServerLevel serverLevel = (ServerLevel) level;
                BlockPos clickedPos = context.getClickedPos();
                var chunkPos = new ChunkPos(clickedPos);
                if (!checkLoaded(serverLevel, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1), new ChunkPos(chunkPos.x + 1, chunkPos.z + 1))) {
                    return InteractionResult.FAIL;
                }

                if (feature.place(serverLevel, serverLevel.getChunkSource().getGenerator(), serverLevel.random, clickedPos)) {
                    String key = holder.key().location().toString();
                    if (context.getPlayer() != null) {
                        context.getPlayer().displayClientMessage(Component.translatable("commands.place.feature.success", key, clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()), false);
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientAccess.openStructurePlacerScreen(stack));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private static class ClientAccess {
        public static void openStructurePlacerScreen(ItemStack stack) {
            Minecraft.getInstance().setScreen(new FeaturePlacerScreen(stack));
        }
    }
}
