// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.RPC;
import me.alpha432.oyvey.features.modules.Module;

public class DiscordRPC extends Module
{
    public DiscordRPC() {
        super("DiscordRPC", "RPC", Category.MISC, true, false, false);
    }
    
    public void onEnadble() {
        RPC.startRPC();
    }
    
    @Override
    public void onDisable() {
        RPC.stopRPC();
    }
}
