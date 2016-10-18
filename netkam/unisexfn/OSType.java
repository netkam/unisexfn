package netkam.unisexfn;

import java.io.File;

/** 
 * @author netkam
 * @version 0.1
 */

public enum OSType {

	WINDOWS("\\", "\r\n"),
	UNIX_LIKE("/", "\n");
	
	OSType(String arg1, String arg2) {
		
		this.fileSeparator = arg1;
		this.lineSeparator = arg2;
	}
	
	
	public static OSType getCurrentOSType() {
		
		return OSTypeDetector.getCurrentOSType();
	}
	
	public static boolean isCurrentOSType(OSType arg1) {
		
		return OSTypeDetector.isCurrentOSType(arg1);
	}
	
	
	public final String lineSeparator;
	
	public final String fileSeparator;
	
	public static final OSType CURRENT_OS_TYPE = OSTypeDetector.getCurrentOSType();
	
}


class OSTypeDetector {
	
	private OSTypeDetector() {}
	
	
	public static OSType getCurrentOSType() {
		
		return OSTypeDetector.CURRENT_OS_TYPE;
	}
	
	public static boolean isCurrentOSType(OSType arg1) {
		
		return OSTypeDetector.CURRENT_OS_TYPE == arg1;
	}
	
	
	private static final OSType detectOSType() {
		
		return File.separator.equals(OSType.UNIX_LIKE.fileSeparator)?
				OSType.UNIX_LIKE
				:
				OSType.WINDOWS;
	}
	
	
	private static final OSType CURRENT_OS_TYPE = OSTypeDetector.detectOSType();
	
}
