package Commands;

/**
 * @author Mostafa Eweda Lap
 * The parent of all commands associated of doing and undoing and logging actions done
 * on any application
 *
 */
public abstract class AbstractCommand {

	/**
	 * do the current action
	 */
	public abstract void doIt();

	/**
	 * undo the current action
	 */
	public abstract void undoIt();
}
