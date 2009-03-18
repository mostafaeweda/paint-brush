package Commands;

import java.util.ArrayList;
import java.util.Iterator;

import Core.plugins.ShapeIF;

public class LocateCommand extends AbstractCommand {

	private ArrayList<ShapeIF> shapesLocated;
	private int shiftX;
	private int shiftY;

	public LocateCommand(ArrayList<ShapeIF> shapesLocated, int shiftX, int shiftY) {
		this.shapesLocated = new ArrayList<ShapeIF>(shapesLocated.size());
		Iterator<ShapeIF> iter = shapesLocated.iterator();
		while (iter.hasNext()) {
			this.shapesLocated.add(iter.next());
		}
		this.shiftX = shiftX;
		this.shiftY = shiftY;
	}

	public void doIt() {
		Iterator<ShapeIF> iter = shapesLocated.iterator();
		while (iter.hasNext()) {
			iter.next().relocate(shiftX, shiftY);
		}
	}

	public void undoIt() {
		Iterator<ShapeIF> iter = shapesLocated.iterator();
		while (iter.hasNext()) {
			iter.next().relocate(-shiftX, -shiftY);
		}
	}
}