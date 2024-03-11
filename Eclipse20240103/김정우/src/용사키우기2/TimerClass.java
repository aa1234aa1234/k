package 용사키우기2;

import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.Timer;

class TimerClass extends Timer implements Serializable{
	private boolean flag = false;
	public TimerClass(int delay, ActionListener listener) {
		super(delay, listener);
	}
	public void setFlag() {
		flag = true;
	}
	public boolean getFlag() {
		return flag;
	}
}
