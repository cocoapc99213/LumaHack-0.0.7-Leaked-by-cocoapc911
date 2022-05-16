//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.util.RenderUtil;
import java.awt.Color;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.gui.ScaledResolution;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.util.ResourceLocation;
import me.alpha432.oyvey.features.modules.Module;

public final class HitMarkers extends Module
{
    public final ResourceLocation image;
    private int renderTicks;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    public Setting<Integer> thickness;
    public Setting<Double> time;
    
    public HitMarkers() {
        super("HitMarkers", "hitmarker thingys", Category.RENDER, false, false, false);
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.thickness = (Setting<Integer>)this.register(new Setting("Thickness", (T)2, (T)1, (T)6));
        this.time = (Setting<Double>)this.register(new Setting("Time", (T)20.0, (T)1.0, (T)50.0));
        this.image = new ResourceLocation("hitmarker.png");
        this.renderTicks = 100;
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (this.renderTicks < this.time.getValue()) {
            final ScaledResolution resolution = new ScaledResolution(HitMarkers.mc);
            this.drawHitMarkers();
        }
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (!event.getEntity().equals((Object)HitMarkers.mc.player)) {
            return;
        }
        this.renderTicks = 0;
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent event) {
        ++this.renderTicks;
    }
    
    public void drawHitMarkers() {
        final ScaledResolution resolution = new ScaledResolution(HitMarkers.mc);
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
    }
}
