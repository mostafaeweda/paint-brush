package tagroba;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class garrab {
	private Display display;
	private Shell shell;

	public static void main(String[] args) {
		new garrab().run();
	}

	public void run() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		ImageData data = new ImageData("ShapeFolder\\2.jpg").scaledTo(500, 400);
		upToDownTransparency(data);
		final Image image = new Image(display, data);
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.addPaintListener(new PaintListener(){
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, 0, 0);
			}});
		shell.open();
		while (! shell.isDisposed())
			if (! display.readAndDispatch())
				display.sleep();
		display.dispose();
	}

	public static void upToDownTransparency(ImageData imageData) {
		int width = imageData.width;
		int height = imageData.height;
		byte[] alphaData = new byte[height * width];
		for (int y = 0; y < height; y++) {
			byte[] alphaRow = new byte[width];
			for (int x = 0; x < width; x++) {
				alphaRow[x] = (byte) ((255 * y) / height);
			}
			System.arraycopy(alphaRow, 0, alphaData, y * width, width);
		}
		imageData.alphaData = alphaData;
	}

	public static void leftToRightTransparency(ImageData imageData) {
		int width = imageData.width;
		int height = imageData.height;
		byte[] alphaData = new byte[height * width];
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < height; x++) {
				alphaData[x * width + y] = (byte) ((255 * y) / width);
			}
		}
		imageData.alphaData = alphaData;
	}

	public static void linesEffectX(ImageData imageData) {
		int width = imageData.width;
		int height = imageData.height;
		byte[] alphaData = new byte[height * width];
		for (int y = 0; y < width; y++) {
			byte[] alphaHeight = new byte[height];
			for (int x = 0; x < height; x++) {
				alphaHeight[x] = (byte) ((255 * x) / width);
			}
			System.arraycopy(alphaHeight, 0, alphaData, y * height, height);
		}
		imageData.alphaData = alphaData;
	}
}
