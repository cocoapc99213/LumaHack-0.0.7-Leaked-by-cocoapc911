//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.util.text.ITextComponent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.util.BlockUtilCrepe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.init.Blocks;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.TimerK;
import me.alpha432.oyvey.features.modules.Module;

public class TrapPhase extends Module
{
    private int stage;
    public TimerK delayTimer;
    private Setting<Boolean> isJump;
    private Setting<Double> jumpOffset;
    private Setting<Integer> delay;
    private Setting<Boolean> toggle;
    
    public TrapPhase() {
        super("TrapPhase", "", Category.COMBAT, true, false, false);
        this.stage = 0;
        this.isJump = (Setting<Boolean>)this.register(new Setting("Jump", false));
        this.jumpOffset = (Setting<Double>)this.register(new Setting("JumpOffset", 0.3, 0.0, 10.0, v -> !this.isJump.getValue()));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", 1, 0, 50));
        this.toggle = (Setting<Boolean>)this.register(new Setting("Toggle", true));
    }
    
    @Override
    public void onEnable() {
        this.stage = 0;
        this.delayTimer = new TimerK();
    }
    
    @Override
    public void onTick() {
        if (this.delayTimer == null) {
            this.delayTimer = new TimerK();
        }
        final int oldSlot = TrapPhase.mc.player.inventory.currentItem;
        final int trapSlot = InventoryUtil.findHotbarBlock(Blocks.IRON_TRAPDOOR);
        if (trapSlot != -1) {
            switch (this.stage) {
                case 0: {
                    if (!this.isJump.getValue()) {
                        TrapPhase.mc.player.setPosition(TrapPhase.mc.player.posX, Math.floor(TrapPhase.mc.player.posY) + this.jumpOffset.getValue(), TrapPhase.mc.player.posZ);
                    }
                    else {
                        TrapPhase.mc.player.jump();
                    }
                    this.stage = 1;
                    TrapPhase.mc.playerController.updateController();
                    break;
                }
                case 1: {
                    if (!this.delayTimer.passedD(this.delay.getValue())) {
                        break;
                    }
                    this.delayTimer.reset();
                    TrapPhase.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)TrapPhase.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    TrapPhase.mc.player.setSneaking(false);
                    TrapPhase.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(trapSlot));
                    BlockUtilCrepe.placeTrapdoor(new BlockPos(TrapPhase.mc.player.posX, TrapPhase.mc.player.posY - 1.0, TrapPhase.mc.player.posZ), EnumHand.MAIN_HAND, false, true, false);
                    BlockUtilCrepe.placeTrapdoor(new BlockPos(TrapPhase.mc.player.posX, TrapPhase.mc.player.posY, TrapPhase.mc.player.posZ), EnumHand.MAIN_HAND, false, true, false);
                    TrapPhase.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(oldSlot));
                    TrapPhase.mc.player.setPosition(TrapPhase.mc.player.posX, Math.floor(TrapPhase.mc.player.posY), TrapPhase.mc.player.posZ);
                    TrapPhase.mc.player.motionY = -10.0;
                    this.stage = 2;
                    if (this.toggle.getValue()) {
                        this.disable();
                        this.stage = 0;
                        break;
                    }
                    break;
                }
            }
            if (this.stage == 2 && this.toggle.getValue()) {
                this.disable();
                this.stage = 0;
            }
        }
        else {
            TrapPhase.mc.player.sendMessage((ITextComponent)new Command.ChatMessage("TRAP\u6301\u3063\u3066\u306d\u3048\u3058\u3083\u3093w"));
            this.disable();
        }
    }
}
