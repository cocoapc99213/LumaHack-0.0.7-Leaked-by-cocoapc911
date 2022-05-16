//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.event.events.BlockEvent;
import me.alpha432.oyvey.util.RenderUtil;
import java.awt.Color;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.Render3DEvent;
import net.minecraft.block.BlockAir;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.api.util.events.Timer;
import me.alpha432.oyvey.features.modules.Module;

public class CandySpeedmine extends Module
{
    private static Speedmine INSTANCE;
    private final Timer timer;
    public Setting<Mode> mode;
    public Setting<Float> damage;
    public Setting<Float> startDamage;
    public Setting<Float> endDamage;
    public Setting<Integer> breakDelay;
    public Setting<Boolean> webSwitch;
    public Setting<Boolean> doubleBreak;
    public Setting<Boolean> render;
    public Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    public Setting<Boolean> outline;
    private final Setting<Float> lineWidth;
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private int breakTick;
    private BlockPos lastBreak;
    private EnumFacing facing;
    private boolean before;
    
    public CandySpeedmine() {
        super("CandySpeedmine", "Speeds up mining.", Category.PLAYER, true, false, false);
        this.timer = new Timer();
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.PACKET));
        this.damage = (Setting<Float>)this.register(new Setting("Damage", (T)0.7f, (T)0.0f, (T)1.0f, v -> this.mode.getValue() == Mode.DAMAGE));
        this.startDamage = (Setting<Float>)this.register(new Setting("Start Damage", (T)0.2f, (T)0.0f, (T)1.0f, v -> this.mode.getValue() == Mode.BREAKER));
        this.endDamage = (Setting<Float>)this.register(new Setting("End Damage", (T)0.7f, (T)0.0f, (T)1.0f, v -> this.mode.getValue() == Mode.BREAKER));
        this.breakDelay = (Setting<Integer>)this.register(new Setting("Break Delay", (T)2, (T)0, (T)10, v -> this.mode.getValue() == Mode.BREAKER));
        this.webSwitch = (Setting<Boolean>)this.register(new Setting("WebSwitch", (T)false));
        this.doubleBreak = (Setting<Boolean>)this.register(new Setting("DoubleBreak", (T)false));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)false));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)false, v -> this.render.getValue()));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)85, (T)0, (T)255, v -> this.box.getValue() && this.render.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true, v -> this.render.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("Width", (T)1.0f, (T)0.1f, (T)5.0f, v -> this.outline.getValue() && this.render.getValue()));
        this.breakTick = 0;
        this.lastBreak = null;
        this.facing = null;
        this.before = false;
        this.setInstance();
    }
    
    public static Speedmine getInstance() {
        if (CandySpeedmine.INSTANCE == null) {
            CandySpeedmine.INSTANCE = new Speedmine();
        }
        return CandySpeedmine.INSTANCE;
    }
    
    private void setInstance() {
    }
    
    @Override
    public void onTick() {
        if (this.currentPos != null) {
            if (!CandySpeedmine.mc.world.getBlockState(this.currentPos).equals(this.currentBlockState) || CandySpeedmine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
                this.currentPos = null;
                this.currentBlockState = null;
            }
            else if (this.webSwitch.getValue() && this.currentBlockState.getBlock() == Blocks.WEB && CandySpeedmine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        CandySpeedmine.mc.playerController.blockHitDelay = 0;
        if (this.mode.getValue() == Mode.BREAKER && this.lastBreak != null) {
            if (this.before) {
                if (!(this.getBlock(this.lastBreak) instanceof BlockAir)) {
                    this.breakerBreak();
                }
            }
            else if (this.getBlock(this.lastBreak) instanceof BlockAir) {
                this.before = true;
            }
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.render.getValue() && this.currentPos != null && this.currentBlockState.getBlock() == Blocks.OBSIDIAN) {
            final Color color = new Color(this.timer.passedMs((int)(2000.0f * OyVey.serverManager.getTpsFactor())) ? 0 : 255, this.timer.passedMs((int)(2000.0f * OyVey.serverManager.getTpsFactor())) ? 255 : 0, 0, 255);
            RenderUtil.drawBoxESP(this.currentPos, color, false, color, this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }
    
    @SubscribeEvent
    public void onBlockEvent(final BlockEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && CandySpeedmine.mc.playerController.curBlockDamageMP > 0.1f) {
            CandySpeedmine.mc.playerController.isHittingBlock = true;
        }
        if (event.getStage() == 4) {
            if (BlockUtil.canBreak(event.pos)) {
                CandySpeedmine.mc.playerController.isHittingBlock = false;
                switch (this.mode.getValue()) {
                    case PACKET: {
                        if (this.currentPos == null) {
                            this.currentPos = event.pos;
                            this.currentBlockState = CandySpeedmine.mc.world.getBlockState(this.currentPos);
                            this.timer.reset();
                        }
                        CandySpeedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        CandySpeedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        CandySpeedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        event.setCanceled(true);
                        break;
                    }
                    case DAMAGE: {
                        if (CandySpeedmine.mc.playerController.curBlockDamageMP < this.damage.getValue()) {
                            break;
                        }
                        CandySpeedmine.mc.playerController.curBlockDamageMP = 1.0f;
                        break;
                    }
                    case BREAKER: {
                        this.breakerAlgo(event);
                        this.breakerBreak();
                    }
                    case INSTANT: {
                        CandySpeedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        CandySpeedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        CandySpeedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        CandySpeedmine.mc.playerController.onPlayerDestroyBlock(event.pos);
                        CandySpeedmine.mc.world.setBlockToAir(event.pos);
                        break;
                    }
                }
            }
            final BlockPos above;
            if (this.doubleBreak.getValue() && BlockUtil.canBreak(above = event.pos.add(0, 1, 0)) && CandySpeedmine.mc.player.getDistance((double)above.getX(), (double)above.getY(), (double)above.getZ()) <= 5.0) {
                CandySpeedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                CandySpeedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
                CandySpeedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
                CandySpeedmine.mc.playerController.onPlayerDestroyBlock(above);
                CandySpeedmine.mc.world.setBlockToAir(above);
            }
        }
    }
    
    private void breakerAlgo(final BlockEvent event) {
        if (this.lastBreak == null || event.pos.getX() != this.lastBreak.getX() || event.pos.getY() != this.lastBreak.getY() || event.pos.getZ() != this.lastBreak.getZ()) {
            CandySpeedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
            CandySpeedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
            this.before = true;
            this.lastBreak = event.pos;
            this.facing = event.facing;
        }
        if (this.breakDelay.getValue() <= this.breakTick++) {
            this.breakerBreak();
            event.setCanceled(true);
            this.breakTick = 0;
        }
    }
    
    private void breakerBreak() {
        CandySpeedmine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.lastBreak, this.facing));
    }
    
    private Block getBlock(final BlockPos b) {
        return CandySpeedmine.mc.world.getBlockState(b).getBlock();
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    static {
        CandySpeedmine.INSTANCE = new Speedmine();
    }
    
    public enum Mode
    {
        PACKET, 
        DAMAGE, 
        INSTANT, 
        BREAKER;
    }
}
