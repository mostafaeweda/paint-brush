package UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import Core.Tools.SelectTool;
import Core.Tools.ToolIF;

/**
 * Color palette that the uses chooses the color from
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class Palette {
	private Shell shell;
	private Image image;
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static Palette instance;

	public static synchronized Palette getInstance(Shell shell, Image image) {
		if (instance == null)
			return (instance = new Palette(shell, image));
		return instance;

	}

	private Palette(Shell shell, Image image) {
		this.shell = shell;
		this.image = image;
		createPalette();
	}

	public void createPalette() {
		Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		shell.setData("palette",canvas);
		FormData data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, -35);
		Canvas scanvas = (Canvas) shell.getData("canvas");
		data.top = new FormAttachment(scanvas, 0);
		data.left = new FormAttachment(0, 70);
		canvas.setLayoutData(data);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				gc.setBackground(e.display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				gc.drawImage(image, 0, 0);
			}
		});
		choseColor();
/*		final Canvas aslia = (Canvas) shell.getData("canvas");
		aslia.addControlListener(new ControlAdapter(){
			@Override
			public void controlResized(ControlEvent e) {
				ImageData oldData = image.getImageData();
				ImageData newImageData = oldData.scaledTo(aslia.getSize().x, oldData.height);
				image = new Image(shell.getDisplay(), newImageData);
			}});
*/
	}

	public void choseColor() {
		Canvas canvas = (Canvas) shell.getData("palette");
		canvas.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				Rectangle imageBound = image.getBounds();
				if (imageBound.contains(e.x, e.y)) {
					ImageData imageData = image.getImageData();
					RGB rgb = imageData.palette.getRGB(imageData.getPixel(e.x,
							e.y));
					Color color = new Color(shell.getDisplay(),rgb);
					Application.getInstance().setColor(color);
					ToolIF tool = Application.getInstance().getTool();
					if (! (tool instanceof SelectTool)) {
						MenuBarUnit.getInstance().errorDialog("Fill Error", "No Items Selected");
					}
					else {
						SelectTool selectTool = (SelectTool) tool;
						selectTool.changeColor(color);
					
						}
					Application.getInstance().refresh();
				}
			}

		});

	}
}
