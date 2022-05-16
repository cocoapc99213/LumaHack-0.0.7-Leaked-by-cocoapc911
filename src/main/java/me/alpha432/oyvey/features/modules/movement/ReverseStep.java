//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ReverseStep extends Module
{
    private static ReverseStep INSTANCE;
    private final Setting<Boolean> twoBlocks;
    
    public ReverseStep() {
        super("ReverseStep", "ReverseStep.", Category.MOVEMENT, true, false, false);
        this.twoBlocks = (Setting<Boolean>)this.register(new Setting("2Blocks", (T)Boolean.FALSE));
        this.setInstance();
    }
    
    public static ReverseStep getInstance() {
        if (ReverseStep.INSTANCE == null) {
            ReverseStep.INSTANCE = new ReverseStep();
        }
        return ReverseStep.INSTANCE;
    }
    
    private void setInstance() {
        ReverseStep.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        final IBlockState touchingState = ReverseStep.mc.world.getBlockState(new BlockPos(ReverseStep.mc.player.posX, ReverseStep.mc.player.posY, ReverseStep.mc.player.posZ).down(2));
        final IBlockState touchingState2 = ReverseStep.mc.world.getBlockState(new BlockPos(ReverseStep.mc.player.posX, ReverseStep.mc.player.posY, ReverseStep.mc.player.posZ).down(3));
        if (ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isInWater()) {
            return;
        }
        if (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN) {
            if (ReverseStep.mc.player.onGround) {
                final EntityPlayerSP player = ReverseStep.mc.player;
                --player.motionY;
            }
        }
        else if (((this.twoBlocks.getValue() && touchingState2.getBlock() == Blocks.BEDROCK) || (this.twoBlocks.getValue() && touchingState2.getBlock() == Blocks.OBSIDIAN)) && ReverseStep.mc.player.onGround) {
            final EntityPlayerSP player2 = ReverseStep.mc.player;
            --player2.motionY;
        }
    }
    
    static {
        ReverseStep.INSTANCE = new ReverseStep();
    }
}
