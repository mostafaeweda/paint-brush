package Core;

import java.util.ArrayList;

import Core.Tools.SelectTool;
import UI.Application;

/**
 * controls the management of taken and loaded snapshots
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class MemoryController {

	private static final int MAX_SNAPSHOTS = 10;
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static MemoryController instance;
	private ArrayList<Momento> snapshots;
	private ArrayList<String> describtions;

	/**
	 * @return
	 * @uml.property  name="instance"
	 */
	public static synchronized MemoryController getInstance() {
		if (instance == null)
			return instance = new MemoryController();
		return instance;
	}

	private MemoryController() {
		snapshots = new ArrayList<Momento>(MAX_SNAPSHOTS);
		describtions = new ArrayList<String>(MAX_SNAPSHOTS);
	}

	public boolean snapshot(String name) {
		if (describtions.size() >= MAX_SNAPSHOTS)
			return false;
		else if (describtions.contains(name))
			return false;
		Application facade = Application.getInstance();
		Momento momento = new Momento(facade.getColor(), facade.getWidth(),
				name, facade.getOrigin(), facade.getShapes(), facade.getTool(),
				facade.getZoom());
		describtions.add(name);
		snapshots.add(momento);
		return true;
	}

	public void restoreSnapshot(String key) {
		Application facade = Application.getInstance();
		Momento momento = snapshots.get((describtions.indexOf(key)));
		facade.getShapes().clear();
		facade.getShapes().addAll(momento.getShapes());
		facade.setColor(momento.getColor());
		facade.setTool(momento.getTool());
		if (momento.getTool() instanceof SelectTool)
			((SelectTool) facade.getTool()).setSelectedShapes(momento
					.getSelectedShapes());
		facade.setWidth(momento.getLineWidth());
		facade.setZoom(momento.getZoom());
		facade.refresh();
	}

	public void remove(String key) {
		int index = describtions.indexOf(key);
		if (index != -1) {
			describtions.remove(index);
			snapshots.remove(index);
		}
	}

	public void remove(String[] keys) {
		for (int i = 0; i < keys.length; i++) {
			remove(keys[i]);
		}
	}

	public boolean contains(String name) {
		return describtions.contains(name);
	}

	public String[] getDesctrbtions() {
		return describtions.toArray(new String[describtions.size()]);
	}
}
