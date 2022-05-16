// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.DiscordPresence;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class TestRPC extends Module
{
    public static TestRPC INSTANCE;
    public Setting<Boolean> showIP;
    public Setting<String> state;
    
    public TestRPC() {
        super("RPC", "Discord rich presence", Category.MISC, false, false, false);
        this.showIP = (Setting<Boolean>)this.register(new Setting("ShowIP", (T)true, "Shows the server IP in your discord presence."));
        this.state = (Setting<String>)this.register(new Setting("State", (T)"Luma!", "Sets the state of the DiscordRPC."));
        TestRPC.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        DiscordPresence.start();
    }
    
    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}
