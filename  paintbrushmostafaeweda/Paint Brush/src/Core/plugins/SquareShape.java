package Core.plugins;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import Core.GraphicsUtils;

/**
 * Square shape
 * @see AbstractShape extending from
 * @author Mostafa Eweda & Mohammed Abd El Salam
 * @version 1.0
 * @since JDK 1.6
 */
public class SquareShape extends AbstractShape {

	private static final int POINTS_NUM = 4;
	Point fixedPoint;

	public SquareShape() {
		super();
		points = new ArrayList<Point>(4);
	}

	@Override
	public void draw(GC gc, Point pt) {
		super.draw(gc);
		if (points.size() == 1) {
			fixedPoint = new Point(points.get(0).x, points.get(0).y);
			for (int i = 1; i < POINTS_NUM; i++)
				points.add(new Point(pt.x, pt.y));
			return;
		}
		int fixedX = fixedPoint.x;
		int fixedY = fixedPoint.y;
		int currentX = pt.x;
		int currentY = pt.y;
		int minX = 0;
		int minY = 0;
		if (currentX >= fixedX && currentY >= fixedY) {
			minX = fixedX;
			minY = fixedY;
		}

		else if (currentX >= fixedX && currentY <= fixedY) {
			minX = fixedX;
			minY = currentY;
		}

		else if (currentX <= fixedX && currentY >= fixedY) {
			minX = currentX;
			minY = fixedY;
		}

		else if (currentX <= fixedX && currentY <= fixedY) {
			minX = currentX;
			minY = currentY;
		}
		int width = Math.abs(fixedX - currentX);
		int hight = Math.abs(fixedY - currentY);
		int line = GraphicsUtils.fisaghors(width, hight,true);
		points.get(0).x = minX;
		points.get(0).y = minY;				// 3ayaz ashelhaa we a7otaha fe method al MouseUp
		points.get(1).x = minX + line;
		points.get(1).y = minY;
		points.get(2).x = minX + line;
		points.get(2).y = minY + line;
		points.get(3).x = minX;
		points.get(3).y = minY + line;
		gc.drawRectangle(minX, minY, line, line);
		
		
		
	}

	@Override
	public Point calculateCenter() {
		return GraphicsUtils.intersection(points.get(0), points.get(2), points.get(1), points.get(3));
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

	@Override
	public ShapeIF copy() {
		return clone();
	}


	@Override
	public boolean addable() {
		return points.size() < POINTS_NUM;
	}

	@Override
	public String getToolType() {
		return "drag";
	}

	public static String getShapeName() {
		return "square";
	}

	public static String getIconPath() {
		return "square.png";
	}

	public static String getToolTip() {
		return "Draw Square : 4 vertices";
	}

	protected ShapeIF clone() {
		Color newColor = new Color(color.getDevice(), color.getRed(), color.getGreen(), color.getBlue());
		SquareShape shape = new SquareShape();
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

	public void addPoint(int ptX, int ptY) {
		
		if (addable()) {
			points.add(new Point(ptX, ptY));
			points.add(new Point(ptX, ptY));
			points.add(new Point(ptX, ptY));
			points.add(new Point(ptX, ptY));
			fixedPoint = new Point(points.get(0).x, points.get(0).y);
		}		
	}
}
