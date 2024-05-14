package dev.turtywurty.testmod.client;

import com.mojang.blaze3d.platform.Window;
import dev.turtywurty.testmod.TestMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = TestMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ExampleHUDDamageIndicator {
    private static final Map<LivingEntity, DamageIndicator> DAMAGE_INDICATORS = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onLivingAttacked(LivingAttackEvent event) {
        LivingEntity attackedEntity = event.getEntity();
        if (attackedEntity.level().isClientSide()) {
            boolean contained = DAMAGE_INDICATORS.containsKey(attackedEntity);
            DamageIndicator damageIndicator = DAMAGE_INDICATORS.computeIfAbsent(attackedEntity, entity -> new DamageIndicator(attackedEntity, event.getAmount()));
            if(contained) {
                damageIndicator.damage += event.getAmount();
                damageIndicator.timeCreated = System.currentTimeMillis();
            }
        }
    }

    @SubscribeEvent
    public static void onHudRender(RenderGuiEvent.Post event) {
        for (DamageIndicator damageIndicator : DAMAGE_INDICATORS.values()) {
            damageIndicator.render(event.getGuiGraphics(), event.getWindow());
        }
    }

    private static class DamageIndicator {
        private final LivingEntity entity;
        private float damage;
        private long timeCreated;
        private final Font font = Minecraft.getInstance().font;

        public DamageIndicator(LivingEntity entity, float damage) {
            this.entity = entity;
            this.damage = damage;
            this.timeCreated = System.currentTimeMillis();
        }

        public void render(GuiGraphics guiGraphics, Window window) {
            String damage = String.valueOf(this.damage);

            int x = (window.getGuiScaledWidth() - font.width(damage)) / 2 + 10;
            int y = (window.getGuiScaledHeight() - font.lineHeight) / 2 - 10;
            guiGraphics.drawString(font, damage, x, y, 0xFF0000);

            if (System.currentTimeMillis() - this.timeCreated >= 1000) {
                DAMAGE_INDICATORS.remove(this.entity);
            }
        }
    }
}
