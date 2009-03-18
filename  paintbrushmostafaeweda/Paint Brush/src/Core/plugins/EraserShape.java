package Core.plugins;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

/**
 * Eraser Shape
 * @author Mostafa Eweda & Mohammed Abd El Salam
 * @see FreePenShape
 * @version 1.0
 * @since JDK 1.6
 */
public class EraserShape extends FreePenShape {

	public EraserShape() {
		super();
	}

	@Override
	public void draw(GC gc, Point currentPoint) {		
		super.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		super.setWidth(20);
		super.draw(gc, currentPoint);
	}

	@Override
	public void draw(GC gc) {
		super.draw(gc);
	}

	public static String getShapeName() {
		return "eraser";
	}

	public static String getIconPath() {
		return "eraser.png";
	}

	public static String getToolTip() {
		return "Eraser Shape";
	}
}
