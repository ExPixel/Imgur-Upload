package expixel.imgur.natives;

import java.awt.Rectangle;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.win32.StdCallLibrary;

public class WindowsOSInterface extends OSInterface {

	User32 user32 = (User32) Native.loadLibrary("user32", User32.class);

	@Override
	public Rectangle getForegroundWindowRectangle() {
		HWND fore = this.user32.GetForegroundWindow();
		RECT rect = new RECT();
		this.user32.GetWindowRect(fore, rect);
		return this.RECT2Rectangle(rect);
	}

	public Rectangle RECT2Rectangle(RECT rect) {
		Rectangle r = new Rectangle();
		r.x = rect.left;
		r.y = rect.top;
		r.width = rect.right - rect.left;
		r.height = rect.bottom - rect.top;
		return r;
	}

	public interface User32 extends StdCallLibrary {
		HWND GetForegroundWindow();
		void GetWindowRect(HWND hWnd, RECT lpRect);
	}


}