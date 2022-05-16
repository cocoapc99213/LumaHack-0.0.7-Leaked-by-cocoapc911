//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import me.alpha432.oyvey.wrapper.Wrapper;

public enum MessageUtil implements Wrapper
{
    instance;
    
    public void addMessage(final String message, final boolean silent) {
        final String prefix = TextFormatting.AQUA + "<Luma +> " + TextFormatting.RESET;
        if (silent) {
            MessageUtil.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)new TextComponentString(prefix + message), 1);
        }
        else {
            MessageUtil.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)new TextComponentString(prefix + message));
        }
    }
}
