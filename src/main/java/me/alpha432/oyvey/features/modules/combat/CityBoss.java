//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.EnumHand;
import java.util.Comparator;
import net.minecraft.util.math.Vec3i;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import me.alpha432.oyvey.util.Util;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.modules.Module;

public class CityBoss extends Module
{
    private BlockPos diggPos;
    private Setting<Float> range;
    private Setting<Boolean> placeCrystal;
    private Setting<Boolean> toggleAutoCrystal;
    int stage;
    
    public CityBoss() {
        super("CityBoss", "Fucking gay yet", Category.COMBAT, true, false, false);
        this.diggPos = null;
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)6.1f, (T)2.0f, (T)13.0f));
        this.placeCrystal = (Setting<Boolean>)this.register(new Setting("Place Crystal", (T)true));
        this.toggleAutoCrystal = (Setting<Boolean>)this.register(new Setting("Toggle AutoCrystal", (T)false));
        this.stage = 0;
    }
    
    @Override
    public void onEnable() {
        this.diggPos = null;
    }
    
    @Override
    public void onTick() {
        if (this.stage == 0) {
            final List<EntityPlayer> entity = (List<EntityPlayer>)Util.mc.world.playerEntities;
            final List<BlockPos> blocks = new ArrayList<BlockPos>();
            for (final EntityPlayer player : entity) {
                if (player.entityId != Util.mc.player.entityId) {
                    final BlockPos[] offset = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
                    for (int i = 0; i < offset.length; ++i) {
                        final BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
                        if ((this.getBlock(pos).getBlock() == Blocks.AIR || !(this.getBlock(pos).getBlock() instanceof BlockLiquid)) && this.getBlock(pos.add((Vec3i)offset[i])).getBlock() == Blocks.OBSIDIAN) {
                            blocks.add(pos.add((Vec3i)offset[i]));
                        }
                    }
                }
            }
            this.diggPos = blocks.stream().min(Comparator.comparing(b -> Util.mc.player.getDistance((double)b.getX(), (double)b.getY(), (double)b.getZ()))).orElse(null);
        }
        if (this.stage == 1) {
            Util.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
    
    private IBlockState getBlock(final BlockPos b) {
        return Util.mc.world.getBlockState(b);
    }
}
