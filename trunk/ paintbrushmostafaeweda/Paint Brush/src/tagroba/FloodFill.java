package tagroba;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import Core.GraphicsUtils;
import Core.Tools.Constants;
import Core.Tools.DragTool;
import Core.plugins.ShapeFactory;
import Core.plugins.ShapeIF;

public class FloodFill {
	private Display display;
	private Shell shell;
	private Image buffer;
	private Point current;
	private int mode = Constants.ON;
	private static FloodFill instance;
	private ArrayList<ShapeIF> shapes;

	public static void main(String[] args) {
		getInstance().run();
	}

	public synchronized static FloodFill getInstance() {
		if (instance == null)
			instance = new FloodFill();
		return instance;
	}

	public FloodFill() {
		shapes = new ArrayList<ShapeIF>();
		current = new Point(0, 0);
	}

	public void run() {
		display = new Display();
		shell = new Shell(display);
		createContents();
		shell.setText("Sossa Flood fill trial_1");
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}

	private void createContents() {
		shell.setLayout(new FillLayout());
		final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		final DragTool tool = DragTool.getInstance(canvas);
		tool.setElement(ShapeFactory.getInstance().createShape("free hand"));
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				canvas.redraw();
				tool.mouseDoubleClick(e);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				if (mode == Constants.ON)
					tool.mouseDown(e);
				/*
				 * else floodFill(e.x, e.y,
				 * Display.getCurrent().getSystemColor(SWT.COLOR_WHITE),
				 * Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
				 */
			}

			@Override
			public void mouseUp(MouseEvent e) {
				tool.mouseUp(e);
			}
		});
		canvas.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				current.x = e.x;
				current.y = e.y;
				tool.mouseMove(e);
			}
		});
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				buffer = new Image(display, canvas.getBounds());
				GC gc = new GC(buffer);
				/*
				 * Transform transform = new Transform(display);
				 * transform.rotate(rotate); ArrayList<Point> points =
				 * tool.getElement().getPoints(); float[] transformSource = new
				 * float[2 points.size()]; for (int i = 0; i <
				 * transformSource.length; i+=2) { transformSource[i] = (float)
				 * points.get(i/2).x; transformSource[i+1] = (float)
				 * points.get(i/2).y; } transform.transform(transformSource);
				 * for (int i = 0; i < transformSource.length; i+=2) {
				 * points.get(i/2).x = (int) transformSource[i];
				 * points.get(i/2).y = (int) transformSource[i+1]; }
				 * transform.dispose();
				 */
				tool.drawShape(gc, current);
				if (mode != Constants.ON) {
					for (int k = 0; k < shapes.size(); k++) {
						ArrayList<Point> pts = shapes.get(k).getPoints();
						for (int i = 0; i < pts.size(); i++)
							GraphicsUtils.rotatePoint(pts.get(i), current, 180);
					}
					mode = Constants.ON;
				}
				Iterator<ShapeIF> iter = shapes.iterator();
				while (iter.hasNext())
					iter.next().draw(gc);
				e.gc.drawImage(buffer, 0, 0);
			}
		});
		shell.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == SWT.ESC)
					mode = Constants.OFF;
			}
		});
	}

	/*
	 * public void floodFill(int x, int y, Color oldColor, Color newColor) {
	 * ImageData data = buffer.getImageData(); int oldPixel =
	 * data.palette.getPixel(oldColor.getRGB()); int newPixel =
	 * data.palette.getPixel(newColor.getRGB()); if (data.getPixel(x, y) ==
	 * newPixel) return; int width = data.width; int height = data.height; int
	 * move1 = x, move2 = y; int[] pixels = new int[width]; int[] queue1 = new
	 * int[width], queue2 = new int[width]; queue1[0] = x; moveX++; // la2enni
	 * a5adt elli 2ablo int iX = 1, iY = 1; while (iX != 0) {
	 * data.getPixels(moveX, moveY, width, pixels, 0); if (pixels[moveX] ==
	 * oldPixel) queueX[iX++] moveX = x; // fi el a5er } }
	 */

	public void addPaintable(ShapeIF shape) {
		shapes.add(shape);
	}
}
