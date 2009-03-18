package Commands;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Color;

import Core.plugins.ShapeIF;

public class ColoringCommand extends AbstractCommand {

	private ArrayList<Color> oldColors;
	private Color newColor;
	private ArrayList<ShapeIF> shapes;

	public ColoringCommand(Color newColor, ArrayList<ShapeIF> selectedShapes) {
		this.shapes = new ArrayList<ShapeIF>(selectedShapes.size());
		Iterator<ShapeIF> iter = selectedShapes.iterator();
		while (iter.hasNext())
			this.shapes.add(iter.next());
		this.oldColors = new ArrayList<Color>(selectedShapes.size());
		iter = selectedShapes.iterator();
		while (iter.hasNext())
			this.oldColors.add(iter.next().getColor());
		this.newColor = newColor;
	}

	public void doIt() {
		Iterator<ShapeIF> iter = shapes.iterator();
		while (iter.hasNext()) {
			ShapeIF currentShape = iter.next();
			oldColors.add(currentShape.getColor());
			currentShape.setColor(newColor);
		}

	}

	public void undoIt() {
		Iterator<ShapeIF> iter = shapes.iterator();
		Iterator<Color> colorIter = oldColors.iterator();
		while (iter.hasNext()){
			ShapeIF currentShape= iter.next();
			currentShape.setColor(colorIter.next());
		}
		
	}
}
