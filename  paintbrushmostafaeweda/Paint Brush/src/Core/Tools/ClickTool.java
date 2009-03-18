package Core.Tools;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Canvas;

import UI.Application;

/**
 * Click tool
 * @see AbstractTool  extending from
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class ClickTool extends AbstractTool {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static ClickTool instance;

	public static synchronized ClickTool getInstance(Canvas canvas) {
		if (instance != null)
			return instance;
		else
			return instance = new ClickTool();
	}

	private ClickTool() {
	}

	// i want to change this method to Abstract Class and path string to it and by refactor i create the shape
	@Override
	public void mouseDown(MouseEvent e) {
		super.mouseDown(e);
		setData(Application.getInstance().getColor(),Application.getInstance().getWidth());
		element.addPoint(e.x, e.y);
		if (! element.addable()) {
			Application.getInstance().addPaintable(element);
			element = element.copy();// ne3maal wa7da 3'eer copy
			element.getPoints().clear();
		}
		Application.getInstance().refresh();
	}
}
