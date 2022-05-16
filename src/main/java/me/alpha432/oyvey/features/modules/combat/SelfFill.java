//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class SelfFill extends Module
{
    private final Setting<Boolean> packet;
    
    public SelfFill() {
        super("SelfFill", "SelfFills yourself in a hole.", Category.COMBAT, true, false, true);
        this.packet = (Setting<Boolean>)this.register(new Setting("PacketPlace", Boolean.FALSE));
    }
    
    @Override
    public void onEnable() {
        SelfFill.mc.player.jump();
        SelfFill.mc.player.jump();
    }
    
    @Override
    public void onUpdate() {
        final BlockPos pos = new BlockPos(SelfFill.mc.player.posX, SelfFill.mc.player.posY, SelfFill.mc.player.posZ);
        if (SelfFill.mc.world.getBlockState(pos.down()).getBlock() == Blocks.AIR && BlockUtil.isPositionPlaceable(pos.down(), false) == 3) {
            BlockUtil.placeBlock(pos.down(), EnumHand.MAIN_HAND, false, this.packet.getValue(), false);
        }
        if (SelfFill.mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) {
            SelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(SelfFill.mc.player.posX, SelfFill.mc.player.posY - 1.3, SelfFill.mc.player.posZ, false));
            SelfFill.mc.player.setPosition(SelfFill.mc.player.posX, SelfFill.mc.player.posY - 1.3, SelfFill.mc.player.posZ);
            this.toggle();
        }
    }
}
