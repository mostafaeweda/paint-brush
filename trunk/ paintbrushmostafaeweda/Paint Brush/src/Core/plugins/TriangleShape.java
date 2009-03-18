package Core.plugins;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import Core.GraphicsUtils;

/**
 * Triangle shape
 * @see AbstractShape extending from
 * @author Mostafa Eweda & Mohammed Abd El Salam
 * @version 1.0
 * @since JDK 1.6
 */
public class TriangleShape extends AbstractShape {

	private static final int POINTS_NUM = 3;

	public TriangleShape() {
		super();
		points = new ArrayList<Point>(POINTS_NUM);
	}

	@Override
	public void draw(GC gc, Point currentPt) {
		super.draw(gc);
		int size = points.size();
		int[] drawn = new int[2 * size];
		Iterator<Point> iter = points.iterator();
		Point current;
		for (int i = 0; iter.hasNext(); i++) {
			current = iter.next();
			drawn[2 * i] = current.x;
			drawn[2 * i + 1] = current.y;
		}
		if (size == POINTS_NUM)
			gc.drawPolygon(drawn);
		else if (size > 0) {
			gc.drawPolyline(drawn);
			gc.drawLine(drawn[drawn.length - 2], drawn[drawn.length - 1],
					currentPt.x, currentPt.y);
		}
	}

	@Override
	public void draw(GC gc) {
		super.draw(gc);
		if (! filled) {
			connectPoints(gc);
			connectFirstLast(gc);
		}
		else
			fillPolygon(gc);
	}

	public ShapeIF copy() {
		return clone();
	}

	public boolean addable() {
		return points.size() < POINTS_NUM;
	}

	@Override
	public Point calculateCenter() {
		Point first = points.get(0), second = points.get(1), third = points.get(2);
		Point firstFront = new Point(Math.min(second.x, third.x) + Math.abs(second.x - third.x)/2, Math.min(second.y, third.y) + Math.abs(second.y - third.y)/2);
		Point secondFront = new Point(Math.min(first.x, third.x) + Math.abs(first.x - third.x)/2, Math.min(first.y, third.y) + Math.abs(first.y - third.y)/2);
		return GraphicsUtils.intersection(first, firstFront, second, secondFront);
	}

	public static String getShapeName() {
		return "triangle";
	}

	public static String getIconPath() {
		return "triangle.png";
	}

	public static String getToolTip() {
		return "Draw Triangle : 4 vertices";
	}

	protected ShapeIF clone() {
		Color newColor = new Color(color.getDevice(), color.getRed(), color
				.getGreen(), color.getBlue());
		TriangleShape shape = new TriangleShape();
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
