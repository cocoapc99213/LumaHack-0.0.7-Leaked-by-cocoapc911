//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.init.Items;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class SilentXp extends Module
{
    Setting<Float> lookPitch;
    private int delay_count;
    int prvSlot;
    
    public SilentXp() {
        super("Silent XP", "awa", Category.COMBAT, true, false, false);
        this.lookPitch = (Setting<Float>)this.register(new Setting("LookPitch", 90.0f, 0.0f, 100.0f));
    }
    
    @Override
    public void onEnable() {
        this.delay_count = 0;
    }
    
    @Override
    public void onUpdate() {
        if (SilentXp.mc.currentScreen == null) {
            this.usedXp();
        }
    }
    
    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            if (SilentXp.mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    private void usedXp() {
        final int oldPitch = (int)SilentXp.mc.player.rotationPitch;
        this.prvSlot = SilentXp.mc.player.inventory.currentItem;
        SilentXp.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.findExpInHotbar()));
        SilentXp.mc.player.rotationPitch = this.lookPitch.getValue();
        SilentXp.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(SilentXp.mc.player.rotationYaw, (float)this.lookPitch.getValue(), true));
        SilentXp.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        SilentXp.mc.player.rotationPitch = (float)oldPitch;
        SilentXp.mc.player.inventory.currentItem = this.prvSlot;
        SilentXp.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.prvSlot));
    }
}
