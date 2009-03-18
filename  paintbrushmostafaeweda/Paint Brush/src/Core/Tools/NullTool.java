package Core.Tools;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Point;


/**
 * that is null Object
 * @author  Mostafa Eweda Lap
 */
public class NullTool extends AbstractTool {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static NullTool instance;
	private NullTool() {
		super();
	}

	@Override
	public void mouseDown(MouseEvent e) {
		System.err.println("Null ya abne");
	}

	/**
	 * @return
	 * @uml.property  name="instance"
	 */
	public static synchronized NullTool getInstance(){
		if (instance == null)
			return instance = new NullTool();
		return instance;
	}

	public void drawShape(PaintEvent e, Point current, int zoom) {
	}

}
