package UI;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

import Commands.CommandManager;
import Commands.CreateCommand;
import Core.Tools.Constants;
import Core.Tools.NullTool;
import Core.Tools.ToolIF;
import Core.plugins.NullShape;
import Core.plugins.ShapeIF;

/**
 * The main application starter
 * @see ToolIF  implemented interface
 * @see ShapeIF
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class Application {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static Application instance;
	private Display display;
	private Cursor plus;
	private Shell shell;
	private Canvas canvas;
	private Point current;
	/**
	 * @uml.property  name="shapes"
	 */
	private ArrayList<ShapeIF> shapes; //
	/**
	 * @uml.property  name="tool"
	 * @uml.associationEnd  
	 */
	private ToolIF tool;
	/**
	 * @uml.property  name="width"
	 */
	private int width = 1;
	/**
	 * @uml.property  name="color"
	 */
	private Color color;
	private Point realSize;
	/**
	 * @uml.property  name="origin"
	 */
	private Point origin;

	/**
	 * @uml.property  name="toolBarUnit"
	 * @uml.associationEnd  
	 */
	private ToolBarUnit toolBarUnit;
	/**
	 * @uml.property  name="menuBarUnit"
	 * @uml.associationEnd  
	 */
	private MenuBarUnit menuBarUnit;
	/**
	 * @uml.property  name="zoom"
	 */
	private int zoom = 100;
	/**
	 * @uml.property  name="image"
	 */
	private Image image;
	private Image icon;
	private Label colorLabel;
	private CLabel statue;

	private Application() {
		current = new Point(0, 0);
		shapes = new ArrayList<ShapeIF>();
		tool = NullTool.getInstance();
		tool.setElement(NullShape.getInstance());
		realSize = new Point(1000, 1000);
		origin = new Point(0, 0);
	}

	/**
	 * @return
	 * @uml.property  name="instance"
	 */
	public static synchronized Application getInstance() {
		if (instance != null)
			return instance;
		else
			return instance = new Application();
	}

	public static void main(String[] args) {
		getInstance().run();
	}

	public void addPaintable(ShapeIF shape) {
		CreateCommand command = new CreateCommand(shapes, shape);
		shapes.add(shape);
		CommandManager.getInstance().registerCommand(command);
	}

	public void zoomIn() {
		if (zoom < Constants.ZOOM_MAX - Constants.ZOOM_STEP)
			zoom(zoom + 10);
		else
			// zoom > MAX
			zoom(Constants.ZOOM_MAX);
		refresh();
	}

	public void zoomOut() {
		if (zoom >= Constants.ZOOM_MIN + Constants.ZOOM_STEP)
			zoom(zoom - 10);
		else
			// zoom < min
			zoom(Constants.ZOOM_MIN);
		refresh();
	}

	public void zoom(int newZoom) {
		Iterator<ShapeIF> iter = shapes.iterator();
		while (iter.hasNext())
			iter.next().zoom(newZoom - zoom);
		zoom = newZoom;
		resizeScrollBars();
		refresh();
	}

	public void run() {
		display = new Display();
		image = new Image(display, Application.class.getResourceAsStream("palette.png"));
		icon = new Image(display, Application.class.getResourceAsStream("paint.png"));
		plus = new Cursor(display, SWT.CURSOR_CROSS);
		color = display.getSystemColor(SWT.COLOR_BLACK);
		shell = new Shell(display);
		shell.setText("Paint Brush");
		Rectangle displaySize = display.getBounds();
		int sizeX = 1000, sizeY = 800;
		createContents();
		init();
		shell.setLocation(displaySize.width / 2 - sizeX / 2, displaySize.height
				/ 2 - sizeY / 2);
		shell.setSize(sizeX, sizeY);

		shell.setImage(icon);
		shell.setMaximized(true);
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		color.dispose();
		dispose();
		image.dispose();
		icon.dispose();
		display.dispose();
	}

	private void createContents() {
		shell.setLayout(new FormLayout());
		menuBarUnit = new MenuBarUnit(shell);

		canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED | SWT.V_SCROLL
				| SWT.H_SCROLL);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setData("canvas", canvas);
		canvas.setCursor(plus);
		Palette.getInstance(shell, image);
		ToolBar toolBar = new ToolBar(shell, SWT.NONE);
		toolBarUnit = new ToolBarUnit(toolBar, menuBarUnit);
		shell.setData("toolBar", toolBar);

		FormData data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, -100);
		data.top = new FormAttachment(toolBar, 4);
		data.left = new FormAttachment(0, 70);
		canvas.setLayoutData(data);

		listen(canvas);
		shell.setData("toolBox", ToolBoxUnit.getInstance(shell, canvas));

		ScrollBar horizontal = canvas.getHorizontalBar();
		horizontal.setVisible(true);
		horizontal.setMinimum(0);
		horizontal.setEnabled(false);
		horizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollHorizontally((ScrollBar) event.widget);
			}
		});
		ScrollBar vertical = canvas.getVerticalBar();
		vertical.setVisible(true);
		vertical.setMinimum(0);
		vertical.setEnabled(false);
		vertical.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollVertically((ScrollBar) event.widget);
			}
		});
		resizeScrollBars();
		shell.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				resizeScrollBars();
			}
		});

		colorLabel = new Label(shell, SWT.NONE);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(canvas);
		data.top = new FormAttachment((ToolBar) shell.getData("toolBox2"), 2);
		data.bottom = new FormAttachment(100, -38);
		colorLabel.setLayoutData(data); // 3'ayartaaahaa nowwwwwwwwwwwwwwww
		colorLabel.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		shell.setData("colorLabel",colorLabel);
		statue = new CLabel(shell, SWT.LEFT | SWT.SHADOW_IN);
		statue.setText("Left and Shadow In");
		shell.setData("status",statue);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -3);
		data.top = new FormAttachment((Canvas) shell.getData("palette"), 3);
		data.bottom = new FormAttachment(100, -3);
		statue.setLayoutData(data); // 3'ayartaaahaa nowwwwwwwwwwwwwwww
		statue.setText("For Help, click Help topics on the Help Menu \t\t\t"
				+ "" + current.x + "," + "" + current.y);
	}

	private void resizeScrollBars() {
		ScrollBar horizontal = canvas.getHorizontalBar();
		ScrollBar vertical = canvas.getVerticalBar();
		Rectangle canvasBounds = canvas.getClientArea();
		int width = Math.round(realSize.x * zoom / 100);
		if (width > canvasBounds.width) {
			horizontal.setEnabled(true);
			horizontal.setMaximum(width);
			horizontal.setThumb(canvasBounds.width);
			horizontal.setPageIncrement(canvasBounds.width);
		} else {
			horizontal.setEnabled(false);
			refresh();
		}
		int height = Math.round(realSize.y * zoom / 100);
		if (height > canvasBounds.height) {
			// The image is taller than the canvas.
			vertical.setEnabled(true);
			vertical.setMaximum(height);
			vertical.setThumb(canvasBounds.height);
			vertical.setPageIncrement(canvasBounds.height);
		} else {
			vertical.setEnabled(false);
			refresh();
		}
	}

	private void init() {
		shell.setData("shapes", shapes);
	}

	private void listen(final Canvas canvas) {
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				tool.mouseDown(e);
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
				canvas.redraw();
				tool.mouseMove(e);
				statue.setText("For Help, click Help topics on the Help Menu \t\t\t"+ ""+current.x+","+" "+current.y);
			}
		});

		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;

				gc.fillRectangle(canvas.getBounds());
				Iterator<ShapeIF> iter = shapes.iterator();
				ShapeIF shape;
				while (iter.hasNext()) {
					shape = iter.next();
					shape.relocate(origin.x, origin.y);
					shape.draw(gc);
					shape.relocate(-origin.x, -origin.y);
				}
				tool.drawShape(gc, current);
			}
		});

		shell.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == SWT.ESC) {
					tool.getElement().getPoints().clear();
					refresh();
				}
			}
		});
		shell.setSize(500, 500);
	}

	private void scrollHorizontally(ScrollBar scrollBar) {
		Rectangle canvasBounds = canvas.getClientArea();
		int width = Math.round(realSize.x * zoom / 100);
		int height = Math.round(realSize.y * zoom / 100);
		if (width > canvasBounds.width) {
			int x = -scrollBar.getSelection() - origin.x;
			// if (x + width < canvasBounds.width) {
			// x = canvasBounds.width - width;
			// }
			canvas.scroll(x, 0, 0, 0, width, height, false);
			origin.x = -scrollBar.getSelection();
		}
	}

	private void scrollVertically(ScrollBar scrollBar) {
		Rectangle canvasBounds = canvas.getClientArea();
		int width = Math.round(realSize.x * zoom / 100);
		int height = Math.round(realSize.y * zoom / 100);
		if (height > canvasBounds.height) {
			int y = -scrollBar.getSelection() - origin.y;
			// if (y + height < canvasBounds.height) {
			// y = canvasBounds.height - height;
			// }
			canvas.scroll(0, y, 0, 0, width, height, false);
			origin.y = -scrollBar.getSelection();
		}
	}

	public void refresh() {
		canvas.redraw();
	}

	/**
	 * @return
	 * @uml.property  name="image"
	 */
	public Image getImage() {
		Point size = canvas.getSize();
		Image image = new Image(display, size.x, size.y);
		GC gc = new GC(image);
		Iterator<ShapeIF> iter = shapes.iterator();
		while (iter.hasNext())
			iter.next().draw(gc);
		return image;
	}

	/**
	 * @return
	 * @uml.property  name="shapes"
	 */
	public ArrayList<ShapeIF> getShapes() {
		return shapes;
	}

	public void dispose() {
		menuBarUnit.dispose();
		toolBarUnit.dispose();
		ToolBoxUnit.getInstance().dispose();
	}

	/**
	 * @return
	 * @uml.property  name="color"
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 * @uml.property  name="color"
	 */
	public void setColor(Color color) {
		this.color = color;
		colorLabel.setBackground(color);
	}

	public void setCursor(Cursor cursor) {
		canvas.setCursor(cursor);
	}

	/**
	 * @return
	 * @uml.property  name="width"
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 * @uml.property  name="width"
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param zoom
	 * @uml.property  name="zoom"
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	/**
	 * @return
	 * @uml.property  name="zoom"
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * @return
	 * @uml.property  name="tool"
	 */
	public ToolIF getTool() {
		return tool;
	}

	/**
	 * @param tool
	 * @uml.property  name="tool"
	 */
	public void setTool(ToolIF tool) {
		this.tool = tool;
	}

	/**
	 * @return
	 * @uml.property  name="origin"
	 */
	public Point getOrigin() {
		return origin;
	}

}
