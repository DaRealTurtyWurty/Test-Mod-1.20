package dev.turtywurty.testmod;

import com.mojang.logging.LogUtils;
import dev.turtywurty.testmod.init.BlockInit;
import dev.turtywurty.testmod.init.ItemInit;
import dev.turtywurty.testmod.network.PacketManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = TestMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(TestMod.MODID)
public class TestMod {
    public static final String MODID = "testmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TestMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketManager::init);
    }
}
