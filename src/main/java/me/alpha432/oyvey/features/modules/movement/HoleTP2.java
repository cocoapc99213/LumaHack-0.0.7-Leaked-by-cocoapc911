//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import java.util.Comparator;
import me.alpha432.oyvey.OyVey;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class HoleTP2 extends Module
{
    private final Setting<Float> range;
    
    public HoleTP2() {
        super("HoleTP2", "like TP", Category.MOVEMENT, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)0.5f, (T)0.1f, (T)10.0f));
    }
    
    @Override
    public void onUpdate() {
        final BlockPos hole = OyVey.holeManager.calcHoles().stream().min(Comparator.comparing(p -> HoleTP2.mc.player.getDistance((double)p.getX(), (double)p.getY(), (double)p.getZ()))).orElse(null);
        this.enable();
        if (hole != null && HoleTP2.mc.player.getDistance((double)hole.getX(), (double)hole.getY(), (double)hole.getZ()) < this.range.getValue() + 1.5) {
            HoleTP2.mc.player.setPosition(hole.getX() + 0.5, (double)hole.getY(), hole.getZ() + 0.5);
            HoleTP2.mc.player.setPosition(hole.getX() + 0.5, (double)hole.getY(), hole.getZ() + 0.5);
            this.disable();
        }
    }
}
