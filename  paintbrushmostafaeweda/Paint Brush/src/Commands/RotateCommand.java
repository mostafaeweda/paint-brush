package Commands;

import java.util.ArrayList;
import java.util.Iterator;

import Core.plugins.ShapeIF;

public class RotateCommand extends AbstractCommand {

	private double theta;
	private ArrayList<ShapeIF> rotated;

	public RotateCommand(ArrayList<ShapeIF> rotated, double theta) {
		this.rotated = new ArrayList<ShapeIF>(rotated.size());
		Iterator<ShapeIF> iter = rotated.iterator();
		while (iter.hasNext())
			this.rotated.add(iter.next());
		this.theta = theta;
	}

	@Override
	public void doIt() {
		Iterator<ShapeIF> iter = rotated.iterator();
		while (iter.hasNext())
			iter.next().rotate(theta);
	}

	@Override
	public void undoIt() {
		Iterator<ShapeIF> iter = rotated.iterator();
		while (iter.hasNext())
			iter.next().rotate(Math.PI*2 - theta);
	}

}
