// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

public class TimerK
{
    private long time;
    
    public TimerK() {
        this.time = -1L;
        this.time = System.nanoTime();
    }
    
    public boolean passedS(final double s) {
        return this.getMs(System.nanoTime() - this.time) >= (long)(s * 1000.0);
    }
    
    public boolean passedM(final double m) {
        return this.getMs(System.nanoTime() - this.time) >= (long)(m * 1000.0 * 60.0);
    }
    
    public boolean passedD(final double dms) {
        return this.getMs(System.nanoTime() - this.time) >= (long)(dms * 3.0);
    }
    
    public boolean passedDms(final double dms) {
        return this.getMs(System.nanoTime() - this.time) >= (long)(dms * 10.0);
    }
    
    public boolean passedDs(final double ds) {
        return this.getMs(System.nanoTime() - this.time) >= (long)(ds * 100.0);
    }
    
    public boolean passedMs(final long ms) {
        return this.getMs(System.nanoTime() - this.time) >= ms;
    }
    
    public boolean passedNS(final long ns) {
        return System.nanoTime() - this.time >= ns;
    }
    
    public void setMs(final long ms) {
        this.time = System.nanoTime() - ms * 1000000L;
    }
    
    public long getPassedTimeMs() {
        return this.getMs(System.nanoTime() - this.time);
    }
    
    public void reset() {
        this.time = System.nanoTime();
    }
    
    public long getMs(final long time) {
        return time / 1000000L;
    }
}
