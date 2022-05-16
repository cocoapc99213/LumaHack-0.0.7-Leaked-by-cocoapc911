//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import me.alpha432.oyvey.util.NordTessellator;
import net.minecraft.util.math.MathHelper;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.util.TotemPopCham;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.misc.PopCounter;
import java.util.HashMap;
import me.alpha432.oyvey.features.modules.Module;

public class PopChams extends Module
{
    public static HashMap<String, Integer> TotemPopContainer;
    private static PopCounter INSTANCE;
    public static final Setting<Boolean> self;
    public static final Setting<Integer> rL;
    public static final Setting<Integer> gL;
    public static final Setting<Integer> bL;
    public static final Setting<Integer> aL;
    public static final Setting<Integer> rF;
    public static final Setting<Integer> gF;
    public static final Setting<Integer> bF;
    public static final Setting<Integer> aF;
    public static final Setting<Integer> fadestart;
    public static final Setting<Double> fadetime;
    public static final Setting<Boolean> onlyOneEsp;
    public static final Setting<Boolean> rainbow;
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;
    
    public PopChams() {
        super("PopChams", "Renders when some1 pops", Category.RENDER, true, false, false);
        this.register(PopChams.self);
        this.register(PopChams.rL);
        this.register(PopChams.gL);
        this.register(PopChams.bL);
        this.register(PopChams.aL);
        this.register(PopChams.rF);
        this.register(PopChams.gF);
        this.register(PopChams.bF);
        this.register(PopChams.aF);
        this.register(PopChams.fadestart);
        this.register(PopChams.fadetime);
        this.register(PopChams.onlyOneEsp);
        this.register(PopChams.rainbow);
    }
    
    @SubscribeEvent
    public void onPacketReceived(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity((World)PopChams.mc.world) != null && (PopChams.self.getValue() || packet.getEntity((World)PopChams.mc.world).getEntityId() != PopChams.mc.player.getEntityId())) {
                final GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
                (this.player = new EntityOtherPlayerMP((World)PopChams.mc.world, profile)).copyLocationAndAnglesFrom(packet.getEntity((World)PopChams.mc.world));
                this.playerModel = new ModelPlayer(0.0f, false);
                this.startTime = System.currentTimeMillis();
                this.playerModel.bipedHead.showModel = false;
                this.playerModel.bipedBody.showModel = false;
                this.playerModel.bipedLeftArmwear.showModel = false;
                this.playerModel.bipedLeftLegwear.showModel = false;
                this.playerModel.bipedRightArmwear.showModel = false;
                this.playerModel.bipedRightLegwear.showModel = false;
                this.alphaFill = PopChams.aF.getValue();
                this.alphaLine = PopChams.aL.getValue();
                if (!PopChams.onlyOneEsp.getValue()) {
                    final TotemPopCham totemPopCham = new TotemPopCham(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (PopChams.onlyOneEsp.getValue()) {
            if (this.player == null || PopChams.mc.world == null || PopChams.mc.player == null) {
                return;
            }
            GL11.glLineWidth(1.0f);
            final Color lineColorS = new Color(PopChams.rL.getValue(), PopChams.bL.getValue(), PopChams.gL.getValue(), PopChams.aL.getValue());
            final Color fillColorS = new Color(PopChams.rF.getValue(), PopChams.bF.getValue(), PopChams.gF.getValue(), PopChams.aF.getValue());
            int lineA = lineColorS.getAlpha();
            int fillA = fillColorS.getAlpha();
            final long time = System.currentTimeMillis() - this.startTime - PopChams.fadestart.getValue();
            if (System.currentTimeMillis() - this.startTime > PopChams.fadestart.getValue()) {
                double normal = this.normalize((double)time, 0.0, PopChams.fadetime.getValue());
                normal = MathHelper.clamp(normal, 0.0, 1.0);
                normal = -normal + 1.0;
                lineA *= (int)normal;
                fillA *= (int)normal;
            }
            final Color lineColor = newAlpha(lineColorS, lineA);
            final Color fillColor = newAlpha(fillColorS, fillA);
            if (this.player != null && this.playerModel != null) {
                NordTessellator.prepareGL();
                GL11.glPushAttrib(1048575);
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                if (this.alphaFill > 1.0) {
                    this.alphaFill -= PopChams.fadetime.getValue();
                }
                final Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)this.alphaFill);
                if (this.alphaLine > 1.0) {
                    this.alphaLine -= PopChams.fadetime.getValue();
                }
                final Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)this.alphaLine);
                glColor(fillFinal);
                GL11.glPolygonMode(1032, 6914);
                renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float)this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
                glColor(outlineFinal);
                GL11.glPolygonMode(1032, 6913);
                renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float)this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                NordTessellator.releaseGL();
            }
        }
    }
    
    double normalize(final double value, final double min, final double max) {
        return (value - min) / (max - min);
    }
    
    public static void renderEntity(final EntityLivingBase entity, final ModelBase modelBase, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (PopChams.mc.getRenderManager() == null) {
            return;
        }
        final float partialTicks = PopChams.mc.getRenderPartialTicks();
        final double x = entity.posX - PopChams.mc.getRenderManager().viewerPosX;
        double y = entity.posY - PopChams.mc.getRenderManager().viewerPosY;
        final double z = entity.posZ - PopChams.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        final float interpolateRotation = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        final float interpolateRotation2 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        final float rotationInterp = interpolateRotation2 - interpolateRotation;
        final float renderPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        renderLivingAt(x, y, z);
        final float f8 = handleRotationFloat(entity, partialTicks);
        prepareRotations(entity);
        final float f9 = prepareScale(entity, scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9, (Entity)entity);
        modelBase.render((Entity)entity, limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9);
        GlStateManager.popMatrix();
    }
    
    public static void prepareTranslate(final EntityLivingBase entityIn, final double x, final double y, final double z) {
        renderLivingAt(x - PopChams.mc.getRenderManager().viewerPosX, y - PopChams.mc.getRenderManager().viewerPosY, z - PopChams.mc.getRenderManager().viewerPosZ);
    }
    
    public static void renderLivingAt(final double x, final double y, final double z) {
        GlStateManager.translate((float)x, (float)y, (float)z);
    }
    
    public static float prepareScale(final EntityLivingBase entity, final float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        final double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        final double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale(scale + widthX, (double)(scale * entity.height), scale + widthZ);
        final float f = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return 0.0625f;
    }
    
    public static void prepareRotations(final EntityLivingBase entityLivingBase) {
        GlStateManager.rotate(180.0f - entityLivingBase.rotationYaw, 0.0f, 1.0f, 0.0f);
    }
    
    public static float interpolateRotation(final float prevYawOffset, final float yawOffset, final float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }
    
    public static Color newAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static float handleRotationFloat(final EntityLivingBase livingBase, final float partialTicks) {
        return 0.0f;
    }
    
    static {
        PopChams.TotemPopContainer = new HashMap<String, Integer>();
        PopChams.INSTANCE = new PopCounter();
        self = new Setting<Boolean>("Self", false);
        rL = new Setting<Integer>("RedLine", 255, 0, 255);
        gL = new Setting<Integer>("GreenLine", 26, 0, 255);
        bL = new Setting<Integer>("BlueLine", 42, 0, 255);
        aL = new Setting<Integer>("AlphaLine", 42, 0, 255);
        rF = new Setting<Integer>("RedFill", 255, 0, 255);
        gF = new Setting<Integer>("GreenFill", 26, 0, 255);
        bF = new Setting<Integer>("BlueFill", 42, 0, 255);
        aF = new Setting<Integer>("AlphaFill", 42, 0, 255);
        fadestart = new Setting<Integer>("FadeStart", 200, 0, 3000);
        fadetime = new Setting<Double>("FadeStart", 0.5, 0.0, 2.0);
        onlyOneEsp = new Setting<Boolean>("OnlyOneEsp", true);
        rainbow = new Setting<Boolean>("Rainbow", false);
    }
}
