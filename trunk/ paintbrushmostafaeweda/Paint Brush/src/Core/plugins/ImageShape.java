package Core.plugins;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * Circle shape
 * @see ShapeIF  extending from
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class ImageShape implements ShapeIF {

	private Image image;
	/**
	 * @uml.property  name="selected"
	 */
	private boolean selected;
	private String path;
	/**
	 * @uml.property  name="points"
	 */
	private ArrayList<Point> points;

	public ImageShape(Image image, String path) {
		this.image = image;
		this.path = path;
		points = new ArrayList<Point>(4);
		points.add(new Point(0, 0));
		ImageData data = image.getImageData();
		points.add(new Point(0, data.width));
		points.add(new Point(data.height, data.width));
		points.add(new Point(data.height, 0));
	}

	@Override
	public void addPoint(int ptX, int ptY) {}

	@Override
	public boolean addable() { return false; }

	@Override
	public boolean contains(Point pt) {return image.getBounds().contains(pt);}

	@Override
	public ShapeIF copy() {
		return new ImageShape(new Image(image.getDevice(), image, SWT.IMAGE_COPY), new String(path));
	}

	@Override
	public void deselect() {
		selected = false;
	}

	@Override
	public void draw(GC gc, Point currentPoint) {}

	@Override
	public void draw(GC gc) {
		ImageData data = image.getImageData();
		gc.drawImage(image, 0, 0, data.width, data.height, points.get(0).x, points.get(0).y,
				data.width, data.height);
	}

	@Override
	public int getAddedPoints() { return 4; }

	@Override
	public Rectangle getBounds() { return image.getBounds(); }

	@Override
	public Color getColor() { return null; }

	/**
	 * @return
	 * @uml.property  name="points"
	 */
	@Override
	public ArrayList<Point> getPoints() { return points; }

	@Override
	public String getToolType() { return path; }

	/**
	 * @return
	 * @uml.property  name="selected"
	 */
	@Override
	public boolean isSelected() { return selected; }

	@Override
	public void relocate(int ptX, int ptY) {points.get(0).x = ptX; points.get(0).y = ptY;}

	@Override
	public void rotate(double theta) {}

	@Override
	public void select() {
		selected = true;
	}

	@Override
	public void setColor(Color color) {}

	@Override
	public void setWidth(int width) {}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.path = (String) in.readObject();
		image = new Image(Display.getCurrent(), path);
		points = new ArrayList<Point>(4);
		points.add(new Point(0, 0));
		ImageData data = image.getImageData();
		points.add(new Point(0, data.width));
		points.add(new Point(data.height, data.width));
		points.add(new Point(data.height, 0));
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(path);
	}

	@Override
	public void zoom(int zoom) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFilled() {
		return false;
	}

	@Override
	public void setFilled(boolean filled) {
		
	}

	@Override
	public void flip(int flipMode) {
		
	}

	@Override
	public Point calculateCenter() {
		return new Point(points.get(2).x / 2, points.get(2).y / 2);
	}

	@Override
	public int getWidth() {
		return 0;
	}
}
