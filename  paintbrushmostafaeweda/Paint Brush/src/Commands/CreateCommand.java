package Commands;

import java.util.List;

import Core.plugins.ShapeIF;

/**
 * @author  Mostafa Eweda Lap
 */
public class CreateCommand extends AbstractCommand {

	private List<ShapeIF> objects;
	/**
	 * @uml.property  name="object"
	 * @uml.associationEnd  
	 */
	private ShapeIF object;

	public CreateCommand(List<ShapeIF> objects, ShapeIF object) {
		this.objects = objects;
		this.object = object;
	}

	public void doIt() {
		objects.add(object);
	}
	
	public void undoIt() {
		objects.remove(objects.lastIndexOf(object));
	}
}
