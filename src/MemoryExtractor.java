import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class MemoryExtractor {

	public static int PROCESS_VM_OPERATION = 0x0008;
	public static int PROCESS_VM_READ = 0x0010;
	public static int PROCESS_VM_WRITE = 0x0020;

	static Kernel32 kernel32 = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
	static User32 user32 = (User32) Native.loadLibrary("user32", User32.class);
	
	public static int pid;
	public static Pointer process;
	
	public static void init() {
		pid = getProcessId("Hockey?");
		process = openProcess(PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_VM_OPERATION, pid);
	}
	
	public static int getProcessId(String window) {
		IntByReference pid = new IntByReference(0);
		user32.GetWindowThreadProcessId(user32.FindWindowA(null, window), pid);
		return pid.getValue();
	}
	
	public static Pointer openProcess(int permissions, int pid) {
		Pointer process = kernel32.OpenProcess(permissions, true, pid);
		return process;
	}
	
	public static Memory readMemory(long address, int bytesToRead) {
		IntByReference read = new IntByReference(0);
		Memory output = new Memory(bytesToRead);
		kernel32.ReadProcessMemory(process, address, output, bytesToRead, read);
		return output;
	}
}
