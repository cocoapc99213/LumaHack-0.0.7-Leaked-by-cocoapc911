// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;

public class RPC
{
    private static final DiscordRPC discordRPC;
    private static final DiscordRichPresence discordRichPresence;
    
    public static void startRPC() {
        final DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println(String.valueOf(new StringBuilder().append("Discord RPC disconnected, var1: ").append(var1).append(", var2: ").append(var2))));
        final String discordID = "974308651634397284";
        RPC.discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
        RPC.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        RPC.discordRichPresence.details = "A";
        RPC.discordRichPresence.state = "A";
        RPC.discordRichPresence.largeImageKey = "rpc";
        RPC.discordRichPresence.largeImageText = "test";
        RPC.discordRichPresence.smallImageKey = "test";
        RPC.discordRichPresence.smallImageText = "test";
        RPC.discordRPC.Discord_UpdatePresence(RPC.discordRichPresence);
    }
    
    public static void stopRPC() {
        RPC.discordRPC.Discord_Shutdown();
        RPC.discordRPC.Discord_ClearPresence();
    }
    
    static {
        discordRichPresence = new DiscordRichPresence();
        discordRPC = DiscordRPC.INSTANCE;
    }
}
