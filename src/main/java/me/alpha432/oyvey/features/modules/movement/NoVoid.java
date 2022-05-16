//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import me.alpha432.oyvey.features.modules.Module;

public class NoVoid extends Module
{
    public NoVoid() {
        super("NoVoid", "Glitches you up from void.", Category.MOVEMENT, false, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (!NoVoid.mc.player.noClip && NoVoid.mc.player.posY <= 0.0) {
            final RayTraceResult trace = NoVoid.mc.world.rayTraceBlocks(NoVoid.mc.player.getPositionVector(), new Vec3d(NoVoid.mc.player.posX, 0.0, NoVoid.mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                return;
            }
            NoVoid.mc.player.setVelocity(0.0, 0.0, 0.0);
            if (NoVoid.mc.player.getRidingEntity() != null) {
                NoVoid.mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
            }
        }
    }
}
