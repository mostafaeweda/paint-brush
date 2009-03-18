
package Core.plugins;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * acts as Null Object Pattern that provied empty behavior
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class NullShape extends AbstractShape {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static  NullShape instance;

	private NullShape() {
		super();
	}

	/**
	 * @return
	 * @uml.property  name="instance"
	 */
	public synchronized static NullShape getInstance() {
		if (instance == null)
			return instance = new NullShape();
		return instance;
	}

	public int getAddedPoints() {
		return 0;
	}

	@Override
	public boolean addable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ShapeIF copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void draw(GC gc, Point currentPoint) {
		
	}
}
