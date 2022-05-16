//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.features.modules.Module;

public class PopAnnouncer extends Module
{
    public PopAnnouncer() {
        super("PopAnnouncer", "Announce pop", Category.MISC, true, false, false);
    }
    
    public void doAnnounce(final EntityPlayer entity) {
        PopAnnouncer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("Just keep popping " + entity.getName()));
    }
}
