package main.core;

public class MemoryReadTimer {

    private long lastReadTime, currentReadTime;

    public MemoryReadTimer() {
        lastReadTime = 0;
    }

    public boolean timeForNextRead() {
        currentReadTime = System.currentTimeMillis();
        if (currentReadTime - lastReadTime >= 1000) {
            lastReadTime = currentReadTime;
            return true;
        }
        return false;
    }
}
