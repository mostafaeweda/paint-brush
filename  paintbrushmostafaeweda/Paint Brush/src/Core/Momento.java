package Core;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

import Core.Tools.SelectTool;
import Core.Tools.ToolIF;
import Core.plugins.ShapeIF;

/**
 * keeps the data of the application when the snapshot is taken
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
class Momento {

	/**
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * @uml.property  name="shapes"
	 */
	private ArrayList<ShapeIF> shapes;
	/**
	 * @uml.property  name="zoom"
	 */
	private int zoom;
	/**
	 * @uml.property  name="lineWidth"
	 */
	private int lineWidth;
	/**
	 * @uml.property  name="color"
	 */
	private Color color;
	/**
	 * @uml.property  name="tool"
	 * @uml.associationEnd  
	 */
	private ToolIF tool;
	/**
	 * @uml.property  name="origin"
	 */
	private Point origin;
	/**
	 * @uml.property  name="selectedShapes"
	 */
	private ArrayList<ShapeIF> selectedShapes;

	Momento(Color color, int lineWidth, String name, Point origin,
			ArrayList<ShapeIF> shapes,
			ToolIF tool, int zoom) {
		this.name = name;
		this.origin = new Point(origin.x, origin.y);
		this.shapes = new ArrayList<ShapeIF>(shapes.size());
		Iterator<ShapeIF> iter = shapes.iterator();
		while (iter.hasNext())
			this.shapes.add(iter.next().copy());
		this.color = color;
		this.lineWidth = lineWidth;
		this.zoom = zoom;
		this.tool = tool;
		if (tool instanceof SelectTool) {
			SelectTool toolSelectTool = (SelectTool) tool;
			ArrayList<ShapeIF> toolItems = toolSelectTool.getSelectedShapes();
			this.selectedShapes = new ArrayList<ShapeIF>(toolItems.size());
			this.selectedShapes.addAll(toolItems);
		}
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 * @uml.property  name="shapes"
	 */
	public ArrayList<ShapeIF> getShapes() {
		return shapes;
	}

	/**
	 * @return
	 * @uml.property  name="zoom"
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * @return
	 * @uml.property  name="lineWidth"
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * @return
	 * @uml.property  name="color"
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return
	 * @uml.property  name="tool"
	 */
	public ToolIF getTool() {
		return tool;
	}

	/**
	 * @return
	 * @uml.property  name="selectedShapes"
	 */
	public ArrayList<ShapeIF> getSelectedShapes() {
		return selectedShapes;
	}

	/**
	 * @return
	 * @uml.property  name="origin"
	 */
	public Point getOrigin() {
		return origin;
	}
}
