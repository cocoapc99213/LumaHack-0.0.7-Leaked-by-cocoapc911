//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.OyVey;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ChatModifier extends Module
{
    private static ChatModifier INSTANCE;
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    public boolean check;
    
    public ChatModifier() {
        super("ChatModifier", "Modifies your chat", Category.MISC, true, false, false);
        this.clean = (Setting<Boolean>)this.register(new Setting("NoChatBackground", (T)false, "Cleans your chat"));
        this.infinite = (Setting<Boolean>)this.register(new Setting("InfiniteChat", (T)false, "Makes your chat infinite."));
        this.setInstance();
    }
    
    public static ChatModifier getInstance() {
        if (ChatModifier.INSTANCE == null) {
            ChatModifier.INSTANCE = new ChatModifier();
        }
        return ChatModifier.INSTANCE;
    }
    
    private void setInstance() {
        ChatModifier.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = event.getPacket();
            final String s = packet.getMessage();
            this.check = !s.startsWith(OyVey.commandManager.getPrefix());
        }
    }
    
    static {
        ChatModifier.INSTANCE = new ChatModifier();
    }
}
