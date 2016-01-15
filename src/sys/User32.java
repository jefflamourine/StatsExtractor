package sys;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;

public interface User32 extends W32APIOptions {  
	Pointer FindWindowA(String winClass, String title);  
	int GetWindowThreadProcessId(Pointer hWnd, IntByReference lpdwProcessId);  
}