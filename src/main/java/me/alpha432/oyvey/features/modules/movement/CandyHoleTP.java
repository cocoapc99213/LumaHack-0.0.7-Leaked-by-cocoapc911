//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.command.Command;
import java.util.Comparator;
import me.alpha432.oyvey.OyVey;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class CandyHoleTP extends Module
{
    private final Setting<Float> range;
    
    public CandyHoleTP() {
        super("CandyHoleTP", "like TP", Category.MOVEMENT, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)0.5f, (T)0.1f, (T)3.0f));
    }
    
    @Override
    public void onUpdate() {
        final BlockPos hole = OyVey.holeManager.calcHoles().stream().min(Comparator.comparing(p -> CandyHoleTP.mc.player.getDistance((double)p.getX(), (double)p.getY(), (double)p.getZ()))).orElse(null);
        if (hole != null) {
            if (CandyHoleTP.mc.player.getDistance((double)hole.getX(), (double)hole.getY(), (double)hole.getZ()) < this.range.getValue() + 1.5) {
                CandyHoleTP.mc.player.setPosition(hole.getX() + 0.5, (double)hole.getY(), hole.getZ() + 0.5);
                CandyHoleTP.mc.player.setPosition(hole.getX() + 0.5, (double)hole.getY(), hole.getZ() + 0.5);
                Command.sendMessage("Accepting teleport...");
            }
            else {
                Command.sendMessage("Out of range! disabling...");
            }
        }
        else {
            Command.sendMessage("Not found hole! disabling...");
        }
        this.disable();
    }
}
