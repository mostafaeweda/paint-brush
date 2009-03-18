package Core.plugins;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import Core.GraphicsUtils;

/**
 * Circle shape
 * @see AbstractShape extending from
 * @author Mostafa Eweda & Mohammed Abd El Salam
 * @version 1.0
 * @since JDK 1.6
 */
public class CircleShape extends AbstractShape {

	private static final int POINTS_NUM = 2;

	public CircleShape() {
		super();
		points = new ArrayList<Point>(POINTS_NUM);	
	}

	@Override
	public void draw(GC gc, Point pt) {
		super.draw(gc);
		if(points.size() == 1)
			points.add(new Point(pt.x,pt.y));
		int diameter = getDiameter(points.get(0), pt);
		//gc.drawOval(points.get(0).x-diameter,points.get(0).y-diameter , diameter*2, diameter*2);
		 //diameter = getDiameter(points.get(0), points.get(1));
		gc.drawOval(points.get(0).x-diameter,points.get(0).y-diameter , diameter*2, diameter*2);
		points.get(1).x = pt.x;
		points.get(1).y = pt.y;
		
	}

	private int getDiameter(Point center,Point  pt) {
		int width = Math.abs( pt.x - center.x);
		int hight = Math.abs(pt.y - center.y);
		return GraphicsUtils.fisaghors(width, hight, true);
	}

	@Override
	public void draw(GC gc) {
		super.draw(gc);
		int diameter = getDiameter(points.get(0), points.get(1));
		if(!filled)
			gc.drawOval(points.get(0).x-diameter,points.get(0).y-diameter , diameter*2, diameter*2);
		else
			gc.fillOval(points.get(0).x-diameter,points.get(0).y-diameter , diameter*2, diameter*2);
	}

	public Rectangle getBounds() {
		int diameter = getDiameter(points.get(0), points.get(1));
		Point center = points.get(0);
		return new Rectangle(center.x - diameter, center.y - diameter, 2*diameter, 2*diameter);
	}

	public ShapeIF copy() {
		return clone();
	}

	@Override
	public boolean addable() {
		return points.size() < POINTS_NUM;
	}

	@Override
	public Point calculateCenter() {
		return points.get(0);
	}

	@Override
	public String getToolType() {
		return "drag";
	}

	public static String getShapeName() {
		return "circle";
	}

	public static String getIconPath() {
		return "circle.png";
	}

	public static String getToolTip() {
		return "Draw Circle";
	}

	protected ShapeIF clone() {
		Color newColor = new Color(color.getDevice(), color.getRed(), color.getGreen(), color.getBlue());
		CircleShape shape = new CircleShape();
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
	
	@Override
	public void addPoint(int ptX, int ptY) {
		
		if(points.size() == 0){
			points.add(new Point(ptX,ptY));
			points.add(new Point(ptX,ptY));
			
		}
	}
	 public boolean contains(Point pt) {
		int diameter = getDiameter(points.get(0), points.get(1));
		if (getDiameter(points.get(0), pt) <= diameter)
		return true;
		return false;
		}
}
