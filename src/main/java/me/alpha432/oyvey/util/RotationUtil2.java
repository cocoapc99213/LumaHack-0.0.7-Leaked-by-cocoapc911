//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;
import me.alpha432.oyvey.wrapper.Wrapper;

public class RotationUtil2 implements Wrapper
{
    public static float yaw;
    public static float pitch;
    static Minecraft mc;
    
    public static Vec3d getEyesPos() {
        return new Vec3d(RotationUtil.mc.player.posX, RotationUtil.mc.player.posY + RotationUtil.mc.player.getEyeHeight(), RotationUtil.mc.player.posZ);
    }
    
    public static void rotate(final Vec3d vec, final boolean sendPacket) {
        final float[] rotations = getRotations(vec);
        if (sendPacket) {
            RotationUtil2.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], RotationUtil2.mc.player.onGround));
        }
        RotationUtil2.mc.player.rotationYaw = rotations[0];
        RotationUtil2.mc.player.rotationPitch = rotations[1];
    }
    
    public static void rotate(final BlockPos vec, final boolean sendPacket) {
        final float[] rotations = getRotations(new Vec3d((double)vec.getX(), (double)vec.getY(), (double)vec.getZ()));
        if (sendPacket) {
            RotationUtil2.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], RotationUtil2.mc.player.onGround));
        }
        RotationUtil2.mc.player.rotationYaw = rotations[0];
        RotationUtil2.mc.player.rotationPitch = rotations[1];
    }
    
    public static void rotateSpoof(final Vec3d vec) {
        final float[] rotations = getRotations(vec);
        RotationUtil2.yaw = rotations[0];
        RotationUtil2.pitch = rotations[1];
        RotationUtil2.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(RotationUtil2.yaw, RotationUtil2.pitch, RotationUtil2.mc.player.onGround));
    }
    
    public static void rotateSpoofNoPacket(final Vec3d vec) {
        final float[] rotations = getRotations(vec);
        RotationUtil2.yaw = rotations[0];
        RotationUtil2.pitch = rotations[1];
    }
    
    public static void stopRotating() {
    }
    
    public static float[] getRotations(final Vec3d vec) {
        final Vec3d eyesPos = new Vec3d(RotationUtil2.mc.player.posX, RotationUtil2.mc.player.posY + RotationUtil2.mc.player.getEyeHeight(), RotationUtil2.mc.player.posZ);
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { RotationUtil2.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - RotationUtil2.mc.player.rotationYaw), RotationUtil2.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - RotationUtil2.mc.player.rotationPitch) };
    }
    
    public static double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        dirz /= len;
        double yaw = Math.atan2(dirz, dirx /= len);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        final double[] array = new double[2];
        yaw = (array[0] = yaw + 90.0);
        array[1] = pitch;
        return array;
    }
    
    public static float[] getLegitRotations(final Vec3d vec) {
        final Vec3d eyesPos = RotationUtil.getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { RotationUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - RotationUtil.mc.player.rotationYaw), RotationUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - RotationUtil.mc.player.rotationPitch) };
    }
    
    public static void faceYawAndPitch(final float yaw, final float pitch) {
        RotationUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(yaw, pitch, RotationUtil.mc.player.onGround));
    }
    
    public static void faceVector(final Vec3d vec, final boolean normalizeAngle) {
        final float[] rotations = RotationUtil.getLegitRotations(vec);
        RotationUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? ((float)MathHelper.normalizeAngle((int)rotations[1], 360)) : rotations[1], RotationUtil.mc.player.onGround));
    }
    
    public static void faceEntity(final Entity entity) {
        final float[] angle = MathUtil.calcAngle(RotationUtil.mc.player.getPositionEyes(RotationUtil2.mc.getRenderPartialTicks()), entity.getPositionEyes(RotationUtil2.mc.getRenderPartialTicks()));
        RotationUtil.faceYawAndPitch(angle[0], angle[1]);
    }
    
    public static float[] getAngle(final Entity entity) {
        return MathUtil.calcAngle(RotationUtil.mc.player.getPositionEyes(RotationUtil2.mc.getRenderPartialTicks()), entity.getPositionEyes(RotationUtil2.mc.getRenderPartialTicks()));
    }
    
    public static int getDirection4D() {
        return MathHelper.floor(RotationUtil.mc.player.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
    }
    
    public static boolean isInFov(final BlockPos pos) {
        return pos != null && (RotationUtil.mc.player.getDistanceSq(pos) < 4.0 || isInFov(new Vec3d((Vec3i)pos), RotationUtil.mc.player.getPositionVector()));
    }
    
    public static boolean isInFov(final Entity entity) {
        return entity != null && (RotationUtil.mc.player.getDistanceSq(entity) < 4.0 || isInFov(entity.getPositionVector(), RotationUtil.mc.player.getPositionVector()));
    }
    
    public static boolean isInFov(final Vec3d vec3d, final Vec3d other) {
        if (RotationUtil.mc.player.rotationPitch > 30.0f) {
            if (other.y > RotationUtil.mc.player.posY) {
                return true;
            }
        }
        else if (RotationUtil.mc.player.rotationPitch < -30.0f && other.y < RotationUtil.mc.player.posY) {
            return true;
        }
        final float angle = MathUtil2.calcAngleNoY(vec3d, other)[0] - transformYaw();
        return angle >= -270.0f || true;
    }
    
    public static float transformYaw() {
        float yaw = RotationUtil.mc.player.rotationYaw % 360.0f;
        if (RotationUtil.mc.player.rotationYaw > 0.0f && yaw > 180.0f) {
            yaw = -180.0f + (yaw - 180.0f);
        }
        return yaw;
    }
    
    public static String getDirection4D(final boolean northRed) {
        final int dirnumber = RotationUtil.getDirection4D();
        if (dirnumber == 0) {
            return "South (+Z)";
        }
        if (dirnumber == 1) {
            return "West (-X)";
        }
        if (dirnumber == 2) {
            return (northRed ? "\u00c2§c" : "") + "North (-Z)";
        }
        if (dirnumber == 3) {
            return "East (+X)";
        }
        return "Loading...";
    }
    
    static {
        RotationUtil2.mc = Minecraft.getMinecraft();
    }
}
