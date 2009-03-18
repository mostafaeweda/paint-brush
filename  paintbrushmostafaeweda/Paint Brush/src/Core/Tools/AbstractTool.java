package Core.Tools;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import Core.plugins.ShapeIF;

/**
 * Provides default behavior for the tool interface
 * @see ToolIF  implemented interface
 * @see ShapeIF
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public abstract class AbstractTool implements ToolIF {

	/**
	 * @uml.property  name="element"
	 * @uml.associationEnd  
	 */
	protected ShapeIF element;

	protected AbstractTool() {
	}

	/**
	 * @param shape
	 * @uml.property  name="element"
	 */
	@Override
	public void setElement(ShapeIF shape) {
		element = shape;
	}

	/**
	 * @return
	 * @uml.property  name="element"
	 */
	@Override
	public final ShapeIF getElement() {
		return element;
	}

	public void drawShape(GC gc, Point current) {
		if (element.getAddedPoints() > 0)
			element.draw(gc, current);
	}

	public void mouseDown(MouseEvent e) {
		
	}

	public void mouseUp(MouseEvent e) {
	}

	public void mouseMove(MouseEvent e) {
	}

	public final void mouseDoubleClick(MouseEvent e) {
	} // final we malhaash imp??

	public void setData(Color color, int width) {
		if (element.getAddedPoints() == 0) {
			element.setWidth(width);
			element.setColor(color);
		}
	}
}