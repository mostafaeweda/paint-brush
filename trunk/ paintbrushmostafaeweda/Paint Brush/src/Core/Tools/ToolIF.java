package Core.Tools;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import Core.plugins.ShapeIF;

/**
 * the tool interface that defines the desired to be implemented behavior
 * @see MouseListener
 * @see MouseMoveListener
 * @author   Mostafa Eweda & Mohammed Abd El Salam
 */
public interface ToolIF extends MouseListener, MouseMoveListener {

	/**
	 * draws the current shape with the given GC and point
	 * @param gc the graphics context of the device to be drawn on
	 * @param pt the point of the cursor at this moment
	 */
	public void drawShape(GC gc, Point pt);

	/**
	 * @return   the current shape element tha the tool is manipulating
	 * @uml.property  name="element"
	 * @uml.associationEnd  
	 */
	public ShapeIF getElement();

	/**
	 * sets the current shape of the application to draw
	 * @param  shape
	 * @uml.property  name="element"
	 */
	public void setElement(ShapeIF shape);

	/**
	 * sets the tool data with the updated data from the drawing application
	 * @param color
	 * @param width line width chosen by the user
	 */
	public void setData(Color color, int width);
}
