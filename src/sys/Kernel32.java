package sys;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Kernel32 extends StdCallLibrary {  
    boolean WriteProcessMemory(Pointer p, long address, Pointer buffer, int size, IntByReference written);  
    boolean ReadProcessMemory(Pointer hProcess, long inBaseAddress, Pointer outputBuffer, int nSize, IntByReference outNumberOfBytesRead);  
    Pointer OpenProcess(int desired, boolean inherit, int pid);  
}