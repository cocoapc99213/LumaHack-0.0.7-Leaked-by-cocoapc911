//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.util.ItemUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.alpha432.oyvey.OyVey;
import java.util.HashMap;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class SurroundRewrite extends Module
{
    private final Setting<Integer> delay;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Boolean> helpingBlocks;
    private final Setting<Boolean> intelligent;
    private final Setting<Boolean> antiPedo;
    private final Setting<Boolean> floor;
    private final Setting<Integer> retryer;
    private final Setting<Integer> retryDelay;
    private final Map<BlockPos, Integer> retries;
    private final Timer timer;
    private final Timer retryTimer;
    private boolean didPlace;
    private int placements;
    private int obbySlot;
    double posY;
    
    public SurroundRewrite() {
        super("SurroundRewrite", "Surrounds you with obsidian", Category.COMBAT, true, false, false);
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", 50, 0, 250));
        this.blocksPerTick = (Setting<Integer>)this.register(new Setting("BPT", 8, 1, 20));
        this.helpingBlocks = (Setting<Boolean>)this.register(new Setting("HelpingBlocks", true));
        this.intelligent = (Setting<Boolean>)this.register(new Setting("Intelligent", false));
        this.antiPedo = (Setting<Boolean>)this.register(new Setting("Always Help", false));
        this.floor = (Setting<Boolean>)this.register(new Setting("Floor", false));
        this.retryer = (Setting<Integer>)this.register(new Setting("Retries", 4, 1, 15));
        this.retryDelay = (Setting<Integer>)this.register(new Setting("Retry Delay", 200, 1, 2500));
        this.retries = new HashMap<BlockPos, Integer>();
        this.timer = new Timer();
        this.retryTimer = new Timer();
        this.didPlace = false;
        this.placements = 0;
        this.obbySlot = -1;
    }
    
    @Override
    public void onEnable() {
        if (SurroundRewrite.mc.player == null || SurroundRewrite.mc.world == null) {
            this.setEnabled(false);
            return;
        }
        this.retries.clear();
        this.retryTimer.reset();
        this.posY = SurroundRewrite.mc.player.posY;
    }
    
    @Override
    public void onToggle() {
        OyVey.INSTANCE.getPlayerManager().setSwitching(false);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.check()) {
            return;
        }
        if (this.posY < SurroundRewrite.mc.player.posY) {
            this.setEnabled(false);
            return;
        }
        boolean onEChest = SurroundRewrite.mc.world.getBlockState(new BlockPos(SurroundRewrite.mc.player.getPositionVector())).getBlock() == Blocks.ENDER_CHEST;
        if (SurroundRewrite.mc.player.posY - (int)SurroundRewrite.mc.player.posY < 0.7) {
            onEChest = false;
        }
        if (!BlockUtil.isSafe((Entity)SurroundRewrite.mc.player, onEChest ? 1 : 0, this.floor.getValue())) {
            this.placeBlocks(SurroundRewrite.mc.player.getPositionVector(), BlockUtil.getUnsafeBlockArray((Entity)SurroundRewrite.mc.player, (int)(onEChest ? 1 : 0), this.floor.getValue()), this.helpingBlocks.getValue(), false);
        }
        else if (!BlockUtil.isSafe((Entity)SurroundRewrite.mc.player, onEChest ? 0 : -1, false) && this.antiPedo.getValue()) {
            this.placeBlocks(SurroundRewrite.mc.player.getPositionVector(), BlockUtil.getUnsafeBlockArray((Entity)SurroundRewrite.mc.player, onEChest ? 0 : -1, false), false, false);
        }
        if (this.didPlace) {
            this.timer.reset();
        }
    }
    
    private boolean placeBlocks(final Vec3d pos, final Vec3d[] vec3ds, final boolean hasHelpingBlocks, final boolean isHelping) {
        int helpings = 0;
        if (this.obbySlot == -1) {
            return false;
        }
        if (SurroundRewrite.mc.player == null) {
            return false;
        }
        boolean switched = false;
        final int lastSlot = SurroundRewrite.mc.player.inventory.currentItem;
        for (final Vec3d vec3d : vec3ds) {
            if (!switched) {
                OyVey.INSTANCE.getPlayerManager().setSwitching(true);
                if (OyVey.INSTANCE.getPlayerManager().getSlot() != this.obbySlot) {
                    SurroundRewrite.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(this.obbySlot));
                }
                switched = true;
            }
            boolean gotHelp = true;
            ++helpings;
            if (isHelping && !this.intelligent.getValue() && helpings > 1) {
                return false;
            }
            final BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            Label_0350: {
                switch (BlockUtil.isPositionPlaceable(position, true)) {
                    case 1: {
                        if (this.retries.get(position) == null || this.retries.get(position) < this.retryer.getValue()) {
                            this.placeBlock(position);
                            this.retries.put(position, (this.retries.get(position) == null) ? 1 : (this.retries.get(position) + 1));
                            this.retryTimer.reset();
                            break;
                        }
                        break;
                    }
                    case 2: {
                        if (hasHelpingBlocks) {
                            gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true);
                            break Label_0350;
                        }
                        break;
                    }
                    case 3: {
                        if (gotHelp) {
                            this.placeBlock(position);
                        }
                        if (isHelping) {
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        if (switched && OyVey.INSTANCE.getPlayerManager().getSlot() != lastSlot) {
            SurroundRewrite.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(lastSlot));
        }
        OyVey.INSTANCE.getPlayerManager().setSwitching(false);
        return false;
    }
    
    private boolean check() {
        if (SurroundRewrite.mc.player == null || SurroundRewrite.mc.world == null) {
            return true;
        }
        this.didPlace = false;
        this.placements = 0;
        this.obbySlot = ItemUtil.getBlockFromHotbar(Blocks.OBSIDIAN);
        if (this.retryTimer.passed(this.retryDelay.getValue())) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (this.obbySlot == -1) {
            this.obbySlot = ItemUtil.getBlockFromHotbar(Blocks.ENDER_CHEST);
            if (this.obbySlot == -1) {
                this.setEnabled(false);
                return true;
            }
        }
        return !this.timer.passed(this.delay.getValue());
    }
    
    private void placeBlock(final BlockPos pos) {
        if (this.placements < this.blocksPerTick.getValue()) {
            BlockUtil.placeBlock(pos);
            this.didPlace = true;
            ++this.placements;
        }
    }
}
