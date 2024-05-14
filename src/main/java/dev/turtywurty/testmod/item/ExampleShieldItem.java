package dev.turtywurty.testmod.item;

import dev.turtywurty.testmod.client.renderer.ExampleBlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ExampleShieldItem extends ShieldItem {
    public ExampleShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ExampleBlockEntityWithoutLevelRenderer.INSTANCE;
            }
        });
    }
}
