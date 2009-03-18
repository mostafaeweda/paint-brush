package Commands;

import java.util.ArrayList;
import java.util.Iterator;

import UI.Application;

import Core.plugins.ShapeIF;

public class WidthChangedCommend extends AbstractCommand {

	int width;
	ArrayList<ShapeIF> shapes;
	int array[];

	public WidthChangedCommend(ArrayList<ShapeIF> shapes, int width) {
		this.shapes = new ArrayList<ShapeIF>(shapes.size());
		this.shapes.addAll(shapes);
		this.width = width;
		array = new int[shapes.size()];
	}

	@Override
	public void doIt() {

		Iterator<ShapeIF> iter = shapes.iterator();
		int i = 0;
		while (iter.hasNext()) {
			ShapeIF shape = iter.next();
			array[i++] = shape.getWidth();
			shape.setWidth(width);
		}
		Application.getInstance().refresh();
	}

	@Override
	public void undoIt() {
		Iterator<ShapeIF> iter = shapes.iterator();
		int i = 0;
		while (iter.hasNext())
			iter.next().setWidth(array[i++]);
		Application.getInstance().refresh();

	}

}
