//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import java.util.List;
import me.alpha432.oyvey.util.RenderUtil;
import java.awt.Color;
import net.minecraft.util.math.Vec3i;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class CityESP extends Module
{
    private Setting<Float> range;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    private Setting<Boolean> renderOL;
    private Setting<Integer> OLRed;
    private Setting<Integer> OLGreen;
    private Setting<Integer> OLBlue;
    private Setting<Integer> OLAlpha;
    private Setting<Float> width;
    
    public CityESP() {
        super("CityESP", "Draw Box", Category.RENDER, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)10.0f, (T)5.0f, (T)30.0f));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)160, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)50, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)120, (T)0, (T)225));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)100, (T)0, (T)225));
        this.renderOL = (Setting<Boolean>)this.register(new Setting("Outline", (T)false));
        this.OLRed = (Setting<Integer>)this.register(new Setting("OL-Red", (T)190, (T)0, (T)255));
        this.OLGreen = (Setting<Integer>)this.register(new Setting("OL-Green", (T)60, (T)0, (T)255));
        this.OLBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", (T)190, (T)0, (T)255));
        this.OLAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", (T)240, (T)0, (T)255));
        this.width = (Setting<Float>)this.register(new Setting("Line Width", (T)1.1f, (T)0.1f, (T)3.0f));
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        final List<EntityPlayer> players = (List<EntityPlayer>)CityESP.mc.world.playerEntities;
        for (final EntityPlayer player : players) {
            if (player.getDistance((Entity)CityESP.mc.player) > this.range.getValue()) {
                continue;
            }
            if (player.entityId == CityESP.mc.player.entityId) {
                continue;
            }
            final BlockPos[] offset = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
            for (int i = 0; i < offset.length; ++i) {
                final BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
                if ((this.getBlock(pos).getBlock() == Blocks.AIR || !(this.getBlock(pos).getBlock() instanceof BlockLiquid)) && this.getBlock(pos.add((Vec3i)offset[i])).getBlock() == Blocks.OBSIDIAN) {
                    RenderUtil.drawBoxESP(pos.add((Vec3i)offset[i]), new Color(this.red.getValue(), this.blue.getValue(), this.green.getValue()), this.renderOL.getValue(), new Color(this.OLRed.getValue(), this.OLBlue.getValue(), this.OLGreen.getValue(), this.OLAlpha.getValue()), this.width.getValue(), this.renderOL.getValue(), true, this.alpha.getValue(), true);
                }
            }
        }
    }
    
    private IBlockState getBlock(final BlockPos b) {
        return CityESP.mc.world.getBlockState(b);
    }
}
