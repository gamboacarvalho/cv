package gpad.ui;

public interface ToolSubject {
	public void notifyToolHandlers();
	public void addToolStateListener(ToolStateObserver o);
	public void removeToolStateListener(ToolStateObserver o);
}
