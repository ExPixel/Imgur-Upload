package expixel.imgur.natives;

import java.awt.Rectangle;

import com.sun.jna.Platform;

/**
 * 
 * Used to interact with the operating system
 * in ways that Java can't on its own.
 * 
 * @author Adolph Celestin
 *
 */
public abstract class OSInterface {
	public abstract Rectangle getForegroundWindowRectangle();

	public static OSInterface getOSInterface() {
		int OSType = Platform.getOSType();
		if(OSType == Platform.WINDOWS) {
			return new WindowsOSInterface();
		}
		return null;
	}
}
