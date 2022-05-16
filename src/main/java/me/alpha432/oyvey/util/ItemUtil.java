//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;

public class ItemUtil
{
    private static final Minecraft mc;
    
    public static int getSlotFromInventory(final Item item) {
        if (item == null) {
            return -1;
        }
        int slot = -1;
        for (int i = 44; i >= 0; --i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                if (i < 9) {
                    i += 36;
                }
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    public static int getItemFromHotbar(final Item item) {
        int slot = -1;
        for (int i = 8; i >= 0; --i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    public static int getBlockFromHotbar(final Block block) {
        int slot = -1;
        for (int i = 8; i >= 0; --i) {
            if (Block.getBlockFromItem(ItemUtil.mc.player.inventory.getStackInSlot(i).getItem()) == block) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    public static int getItemCount(final Item item) {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            final ItemStack stack = ItemUtil.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }
    
    public static void replaceOffhand(final int slot) {
        ItemUtil.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)ItemUtil.mc.player);
        ItemUtil.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)ItemUtil.mc.player);
        ItemUtil.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)ItemUtil.mc.player);
    }
    
    public static int getItemDamage(final ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage();
    }
    
    public static float getDamageInPercent(final ItemStack stack) {
        return getItemDamage(stack) / (float)stack.getMaxDamage() * 100.0f;
    }
    
    public static int getRoundedDamage(final ItemStack stack) {
        return (int)getDamageInPercent(stack);
    }
    
    public static boolean hasDurability(final ItemStack stack) {
        final Item item = stack.getItem();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
