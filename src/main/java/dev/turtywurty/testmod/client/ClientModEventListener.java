package dev.turtywurty.testmod.client;

import dev.turtywurty.testmod.TestMod;
import dev.turtywurty.testmod.client.model.ExampleShieldModel;
import dev.turtywurty.testmod.init.ItemInit;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TestMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventListener {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ItemInit.EXAMPLE_SHIELD.get(), new ResourceLocation(TestMod.MODID, "blocking"),
                    (stack, level, entity, id) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ExampleShieldModel.LAYER_LOCATION, ExampleShieldModel::createLayer);
    }
}
