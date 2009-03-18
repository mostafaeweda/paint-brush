package Core.Tools;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

import Core.plugins.ShapeIF;
import UI.Application;

/**
 * @author  Mostafa Eweda Lap
 */
public class DragTool extends AbstractTool {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static DragTool instance;
	private boolean mouseDown = false;
	private boolean firstSelected = true;

	public static synchronized DragTool getInstance(Canvas canvas) {
		if (instance != null)
			return instance;
		else
			return instance = new DragTool();
	}

	private DragTool() {
		super();
	}

	@Override
	public void mouseDown(MouseEvent e) { // kont 3ayaz method addPoint teraga3le boolean a7saan
		mouseDown = true; // refresh
		if (! firstSelected) {
			element = element.copy();
			element.getPoints().clear();
		}
		setData(Application.getInstance().getColor(),Application.getInstance().getWidth());
		element.addPoint(e.x, e.y);
	}

	@Override
	public void mouseUp(MouseEvent e) {
		firstSelected = false;
		mouseDown = false;
		Application.getInstance().addPaintable(element);		
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if(mouseDown)
			Application.getInstance().refresh();
	}

	@Override
	public void setElement(ShapeIF shape){
		super.setElement(shape);
		firstSelected = true;
	}

	@Override
	public void drawShape(GC gc, Point current) {
		if(mouseDown)	
			super.drawShape(gc, current);
	
	}
	
}
