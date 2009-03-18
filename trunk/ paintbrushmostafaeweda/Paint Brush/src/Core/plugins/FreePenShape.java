package Core.plugins;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * Free Pen shape
 * @see AbstractShape extending from
 * @author Mostafa Eweda & Mohammed Abd El Salam
 * @version 1.0
 * @since JDK 1.6
 */
public class FreePenShape extends AbstractShape {

	public FreePenShape() {
		super();
		points = new ArrayList<Point>();
	}

	@Override
	public boolean addable() {
		return true;
	}

	@Override
	public ShapeIF copy() {
		return clone();
	}

	@Override
	public void draw(GC gc, Point currentPoint) {
		addPoint(currentPoint.x, currentPoint.y);
		draw(gc);
	}

	@Override
	public void draw(GC gc) {
		super.draw(gc);
		if (points.size() == 1)
			gc.drawPoint(points.get(0).x, points.get(0).y);
		else {
			connectPoints(gc);
		}
	}

	@Override
	public String getToolType() {
		return "drag";
	}

	public static String getShapeName() {
		return "free hand";
	}

	public static String getIconPath() {
		return "pen.png";
	}

	public static String getToolTip() {
		return "Draw with Free Hand Tool";
	}

	protected ShapeIF clone() {
		Color newColor = new Color(color.getDevice(), color.getRed(), color.getGreen(), color.getBlue());
		FreePenShape shape = new FreePenShape();
		shape.setColor(newColor);
		shape.setWidth(width);
		ArrayList<Point> p = new ArrayList<Point>();
		Iterator<Point> iter = points.iterator();
		Point current;
		while (iter.hasNext()) {
			current = iter.next();
			p.add(new Point(current.x, current.y));
		}
		shape.points = p;
		return shape;
	}
}
