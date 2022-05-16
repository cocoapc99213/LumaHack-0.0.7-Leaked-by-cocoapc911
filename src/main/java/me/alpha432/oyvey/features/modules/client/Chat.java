//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.client;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.Vec3i;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.features.modules.Module;

public class Chat extends Module
{
    private static Chat INSTANCE;
    private final Timer timer;
    public Setting<Suffix> suffix;
    public Setting<Prefix> prefix;
    public Setting<String> customsuffix;
    public Setting<String> customprefix;
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    public Setting<Boolean> autoQMain;
    public Setting<Boolean> qNotification;
    public Setting<Integer> qDelay;
    public Setting<TextUtil.Color> timeStamps;
    public Setting<Boolean> rainbowTimeStamps;
    public Setting<TextUtil.Color> bracket;
    public Setting<Boolean> space;
    public Setting<Boolean> all;
    public Setting<Boolean> shrug;
    
    public Chat() {
        super("ChatSuffix", "Modifies your chat", Category.CLIENT, true, false, false);
        this.timer = new Timer();
        this.suffix = (Setting<Suffix>)this.register(new Setting("Suffix", (T)Suffix.NONE, "Your Suffix."));
        this.prefix = (Setting<Prefix>)this.register(new Setting("Prefix", (T)Prefix.CUSTOM, "Your Prefix."));
        this.customsuffix = (Setting<String>)this.register(new Setting("customsuffix", (T)"Luma"));
        this.customprefix = (Setting<String>)this.register(new Setting("customprefix", (T)"->"));
        this.clean = (Setting<Boolean>)this.register(new Setting("CleanChat", (T)false, "Cleans your chat"));
        this.infinite = (Setting<Boolean>)this.register(new Setting("Infinite", (T)false, "Makes your chat infinite."));
        this.autoQMain = (Setting<Boolean>)this.register(new Setting("AutoQMain", (T)false, "Spams AutoQMain"));
        this.qNotification = (Setting<Boolean>)this.register(new Setting("QNotification", (T)false, v -> this.autoQMain.getValue()));
        this.qDelay = (Setting<Integer>)this.register(new Setting("QDelay", (T)9, (T)1, (T)90, v -> this.autoQMain.getValue()));
        this.timeStamps = (Setting<TextUtil.Color>)this.register(new Setting("Time", (T)TextUtil.Color.NONE));
        this.rainbowTimeStamps = (Setting<Boolean>)this.register(new Setting("RainbowTimeStamps", (T)false, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.bracket = (Setting<TextUtil.Color>)this.register(new Setting("Bracket", (T)TextUtil.Color.WHITE, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.space = (Setting<Boolean>)this.register(new Setting("Space", (T)true, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.all = (Setting<Boolean>)this.register(new Setting("All", (T)false, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.shrug = (Setting<Boolean>)this.register(new Setting("Shrug", (T)false));
        this.setInstance();
    }
    
    public static Chat getInstance() {
        if (Chat.INSTANCE == null) {
            Chat.INSTANCE = new Chat();
        }
        return Chat.INSTANCE;
    }
    
    private void setInstance() {
        Chat.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.autoQMain.getValue()) {
            if (!this.shouldSendMessage((EntityPlayer)Chat.mc.player)) {
                return;
            }
            if (this.qNotification.getValue()) {
                Command.sendMessage("<AutoQueueMain> Sending message: /queue main");
            }
            Chat.mc.player.sendChatMessage("/queue main");
            this.timer.reset();
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = event.getPacket();
            String s = packet.getMessage();
            if (s.startsWith("/") || s.startsWith("!") || s.startsWith("+") || s.startsWith("-") || s.startsWith(".")) {
                return;
            }
            switch (this.prefix.getValue()) {
                case LUM4: {
                    s = "LUM4 -> " + s;
                    break;
                }
            }
            switch (this.prefix.getValue()) {
                case CUSTOM: {
                    s = this.customprefix.getPlannedValue() + " " + s;
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case LUM4: {
                    s += " -=LUM4=-";
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case Luma: {
                    s += " / Luma";
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case CUSTOM: {
                    s = s + " " + this.customsuffix.getPlannedValue();
                    break;
                }
            }
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }
            packet.message = s;
        }
    }
    
    @SubscribeEvent
    public void onChatPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() != 0 || event.getPacket() instanceof SPacketChat) {}
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.timeStamps.getValue() != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
            if (!event.getPacket().isSystem()) {
                return;
            }
            final String originalMessage = event.getPacket().chatComponent.getFormattedText();
            final String message = this.getTimeString(originalMessage) + originalMessage;
            event.getPacket().chatComponent = (ITextComponent)new TextComponentString(message);
        }
    }
    
    public String getTimeString(final String message) {
        final String date = new SimpleDateFormat("k:mm").format(new Date());
        if (this.rainbowTimeStamps.getValue()) {
            final String timeString = "<" + date + ">" + (this.space.getValue() ? " " : "");
            final StringBuilder builder = new StringBuilder(timeString);
            builder.insert(0, "§+");
            if (!message.contains(Managers.getInstance().getRainbowCommandMessage())) {
                builder.append("§r");
            }
            return builder.toString();
        }
        return ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString("<", this.bracket.getValue())) + TextUtil.coloredString(date, this.timeStamps.getValue()) + ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString(">", this.bracket.getValue())) + (this.space.getValue() ? " " : "") + "§r";
    }
    
    private boolean shouldSendMessage(final EntityPlayer player) {
        return player.dimension == 1 && this.timer.passedS(this.qDelay.getValue()) && player.getPosition().equals((Object)new Vec3i(0, 240, 0));
    }
    
    static {
        Chat.INSTANCE = new Chat();
    }
    
    public enum Suffix
    {
        NONE, 
        LUM4, 
        Luma, 
        CUSTOM;
    }
    
    public enum Prefix
    {
        None, 
        LUM4, 
        CUSTOM;
    }
}
