//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import java.util.Iterator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.item.EntityItem;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.block.BlockEnderChest;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Burrow2 extends Module
{
    private final Setting<Boolean> rotate;
    private final Setting<Float> offset;
    private final Setting<Boolean> sneak;
    private final Setting<Boolean> obsidian;
    private final Setting<Boolean> echest;
    private BlockPos originalPos;
    private int oldSlot;
    
    public Burrow2() {
        super("Burrow2", "Rubberbands u in a block", Category.COMBAT, true, false, false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)false));
        this.offset = (Setting<Float>)this.register(new Setting("Offset", (T)1.0f, (T)(-10.0f), (T)10.0f));
        this.sneak = (Setting<Boolean>)this.register(new Setting("Sneak", (T)false));
        this.obsidian = (Setting<Boolean>)this.register(new Setting("Obsidian", (T)true));
        this.echest = (Setting<Boolean>)this.register(new Setting("Ender chest", (T)true));
        this.oldSlot = -1;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.originalPos = new BlockPos(Burrow2.mc.player.posX, Burrow2.mc.player.posY, Burrow2.mc.player.posZ);
        if (Burrow2.mc.world.getBlockState(new BlockPos(Burrow2.mc.player.posX, Burrow2.mc.player.posY, Burrow2.mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = Burrow2.mc.player.inventory.currentItem;
    }
    
    @Override
    public void onUpdate() {
        int blockSlot = -1;
        if (this.obsidian.getValue()) {
            blockSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        }
        if (this.echest.getValue() && blockSlot == -1) {
            blockSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        }
        if (blockSlot == -1) {
            Burrow2.mc.player.sendMessage((ITextComponent)new CPacketChatMessage("[" + ChatFormatting.RED + "Burrow" + ChatFormatting.RESET + "] Can't find block in hotbar!"));
            this.toggle();
            return;
        }
        InventoryUtil.switchToHotbarSlot(blockSlot, false);
        Burrow2.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow2.mc.player.posX, Burrow2.mc.player.posY + 0.41999998688698, Burrow2.mc.player.posZ, true));
        Burrow2.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow2.mc.player.posX, Burrow2.mc.player.posY + 0.7531999805211997, Burrow2.mc.player.posZ, true));
        Burrow2.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow2.mc.player.posX, Burrow2.mc.player.posY + 1.00133597911214, Burrow2.mc.player.posZ, true));
        Burrow2.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow2.mc.player.posX, Burrow2.mc.player.posY + 1.16610926093821, Burrow2.mc.player.posZ, true));
        final boolean sneaking = Burrow2.mc.player.isSneaking();
        if (this.sneak.getValue() && sneaking) {
            Burrow2.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow2.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        BlockUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        Burrow2.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow2.mc.player.posX, Burrow2.mc.player.posY + this.offset.getValue(), Burrow2.mc.player.posZ, false));
        InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
        if (this.sneak.getValue() && sneaking) {
            Burrow2.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow2.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        this.toggle();
    }
    
    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : Burrow2.mc.world.loadedEntityList) {
            if (entity.equals((Object)Burrow2.mc.player)) {
                continue;
            }
            if (entity instanceof EntityItem) {
                continue;
            }
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }
}
