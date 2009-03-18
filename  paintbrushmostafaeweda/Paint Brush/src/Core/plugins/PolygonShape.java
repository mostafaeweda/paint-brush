package Core.plugins;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * Polygon shape
 * @see AbstractShape  extending from
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class PolygonShape extends AbstractShape {

	/**
	 * @uml.property  name="triShape"
	 * @uml.associationEnd  
	 */
	private TriangleShape triShape;
	private boolean finished = false;

	public PolygonShape() {
		super();
		points = new ArrayList<Point>();
	}

	@Override
	public boolean addable() {
		return !finished;
	}

	@Override
	public ShapeIF copy() {
		return clone();
	}

	@Override
	public void draw(GC gc, Point currentPoint) {
		draw(gc);
		triShape.setColor(color);
		triShape.draw(gc);
		if (addable()) {
			super.draw(gc);
			Point last = points.get(points.size() - 1);
			gc.drawLine(last.x, last.y, currentPoint.x, currentPoint.y);
		}
	}

	@Override
	public void draw(GC gc) {
		super.draw(gc);
		if (points.size() == 1) {
			Point first = points.get(0);
			gc.drawPoint(first.x, first.y);
		} else {
			if (! filled) {
				connectPoints(gc);
				if (finished)
					connectFirstLast(gc);
			}
			else // filled
				fillPolygon(gc);
		}
	}

	@Override
	public String getToolType() {
		return "click";
	}

	public static String getShapeName() {
		return "polygon";
	}

	public static String getIconPath() {
		return "polygon.png";
	}

	public static String getToolTip() {
		return "Draw apolygon with any number of vertices";
	}

	public void addPoint(int ptX, int ptY) {
		if (points.isEmpty()) {
			triShape = new TriangleShape();
			triShape.addPoint(ptX - 5, ptY + 5);
			triShape.addPoint(ptX + 5, ptY + 5);
			triShape.addPoint(ptX, ptY - 5);
		}
		Point pt = new Point(ptX, ptY);
		if (triShape.contains(pt) && points.size() > 1) {
			finished = true;
		} else
			points.add(new Point(ptX, ptY));
	}

	protected ShapeIF clone() {
		Color newColor = new Color(color.getDevice(), color.getRed(), color
				.getGreen(), color.getBlue());
		PolygonShape shape = new PolygonShape();
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
