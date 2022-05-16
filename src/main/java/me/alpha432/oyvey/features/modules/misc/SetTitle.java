// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import org.lwjgl.opengl.Display;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;

public class SetTitle extends Module
{
    String set;
    
    public SetTitle() {
        super("SetTitle", "awa", Category.MISC, true, false, false);
        this.set = OyVey.MODNAME;
    }
    
    @Override
    public void onUpdate() {
        Display.setTitle(this.set);
    }
}
