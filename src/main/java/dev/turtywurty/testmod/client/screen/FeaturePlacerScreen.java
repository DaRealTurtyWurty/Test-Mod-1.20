package dev.turtywurty.testmod.client.screen;

import dev.turtywurty.testmod.TestMod;
import dev.turtywurty.testmod.network.PacketManager;
import dev.turtywurty.testmod.network.c2s.C2SRequestCFPacket;
import dev.turtywurty.testmod.network.c2s.C2SUpdateFeaturePlacerPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FeaturePlacerScreen extends Screen {
    private static final Component TITLE = Component.translatable("screen." + TestMod.MODID + ".feature_placer");
    private static final ResourceLocation TEXTURE = new ResourceLocation(TestMod.MODID, "textures/gui/feature_placer.png");
    private static final Component SELECT_BUTTON = Component.translatable("button." + TestMod.MODID + ".feature_placer.select");

    private final ItemStack stack;
    private final Map<ResourceLocation, Component> features = new HashMap<>();
    private final List<ResourceLocation> backingFeatureList = new ArrayList<>();

    private final int imageWidth = 128, imageHeight = 64;
    private ResourceLocation selectedFeature;
    private int leftPos, topPos;

    public FeaturePlacerScreen(ItemStack stack) {
        super(TITLE);
        this.stack = stack;

        PacketManager.sendToServer(new C2SRequestCFPacket());
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        var index = new AtomicInteger(0);
        addRenderableWidget(Button.builder(Component.literal("⬅️"), button -> {
                    if (index.updateAndGet(i -> i - 1) < 0)
                        index.set(this.backingFeatureList.size() - 1);

                    if(!this.backingFeatureList.isEmpty())
                        this.selectedFeature = this.backingFeatureList.get(index.get());
                })
                .bounds(this.leftPos + 10, this.topPos + 10, 20, 20)
                .build());

        addRenderableWidget(Button.builder(Component.literal("➡️"), button -> {
                    index.updateAndGet(i -> i + 1);
                    if (index.get() >= this.backingFeatureList.size())
                        index.set(0);

                    if(!this.backingFeatureList.isEmpty())
                        this.selectedFeature = this.backingFeatureList.get(index.get());
                })
                .bounds(this.leftPos + 98, this.topPos + 10, 20, 20)
                .build());

        addRenderableWidget(Button.builder(SELECT_BUTTON, button -> {
                    CompoundTag nbt = this.stack.getOrCreateTagElement(TestMod.MODID);
                    nbt.putString("Feature", this.selectedFeature == null ? "" : this.selectedFeature.toString());
                    this.minecraft.popGuiLayer();

                    PacketManager.sendToServer(new C2SUpdateFeaturePlacerPacket(this.selectedFeature));
                })
                .bounds(this.leftPos + 10, this.topPos + 50, 108, 20)
                .build());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.selectedFeature != null) {
            graphics.drawString(this.font, this.features.get(this.selectedFeature), this.leftPos + 10, this.topPos + 40, 0xFFFFFF);
        }
    }

    public void setFeatures(Collection<ResourceLocation> features, boolean isFirst) {
        if(isFirst) {
            this.backingFeatureList.clear();
            this.features.clear();
        }

        features.forEach(key -> {
            this.features.put(key, Component.literal(key.toString()));
            this.backingFeatureList.add(key);

            if(this.selectedFeature == null) {
                this.selectedFeature = key;
            }
        });

        System.out.printf("Added %d features%n", features.size());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
