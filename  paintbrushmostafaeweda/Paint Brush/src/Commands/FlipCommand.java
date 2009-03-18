package Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import UI.Application;

import Core.plugins.ShapeIF;

public class FlipCommand extends AbstractCommand {

	private ArrayList<ShapeIF> flipped;
	private int mode;

	public FlipCommand(List<ShapeIF> flipped, int mode) {
		this.flipped = new ArrayList<ShapeIF>(flipped.size());
		this.flipped.addAll(flipped);
		this.mode = mode;
	}

	public void doIt() {
		Iterator<ShapeIF> iter = flipped.iterator();
		while (iter.hasNext())
			iter.next().flip(mode);
		Application.getInstance().refresh();
	}
	
	public void undoIt() {
		doIt();
	}
}
