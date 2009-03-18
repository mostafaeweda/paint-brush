package Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import UI.Application;

import Core.plugins.ShapeIF;


public class DeleteCommand extends AbstractCommand {

	private ArrayList<ShapeIF> removed;

	public DeleteCommand(List<ShapeIF> removed) {
		this.removed = new ArrayList<ShapeIF>(removed.size());
		Iterator<ShapeIF> iter = removed.iterator();
		while (iter.hasNext())
			this.removed.add(iter.next());
	}
	
	public void doIt() {
		List<ShapeIF> allShapes = Application.getInstance().getShapes();
		Iterator<ShapeIF> iter = removed.iterator();
		while (iter.hasNext())
			allShapes.remove(iter.next());
	}
	
	public void undoIt() {
		List<ShapeIF> allShapes = Application.getInstance().getShapes();
		Iterator<ShapeIF> iter = removed.iterator();
		while (iter.hasNext())
			allShapes.add(iter.next());
	}
}
