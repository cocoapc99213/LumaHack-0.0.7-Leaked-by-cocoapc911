//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.manager;

import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.alpha432.oyvey.event.events.PacketEvent;
import net.minecraft.client.Minecraft;

public class PlayerManager
{
    private static final Minecraft mc;
    private boolean shifting;
    private boolean switching;
    private int slot;
    
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketEntityAction) {
            final CPacketEntityAction packet = event.getPacket();
            if (packet.getAction() == CPacketEntityAction.Action.START_SNEAKING) {
                this.shifting = true;
            }
            else if (packet.getAction() == CPacketEntityAction.Action.STOP_SNEAKING) {
                this.shifting = false;
            }
        }
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            this.slot = event.getPacket().getSlotId();
            PlayerManager.mc.player.inventory.currentItem = this.slot;
        }
    }
    
    public void setSwitching(final boolean switching) {
        this.switching = switching;
    }
    
    public boolean isShifting() {
        return this.shifting;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
