package Commands;

import java.util.ArrayList;
import java.util.Iterator;

import Core.plugins.ShapeIF;

public class FilledCommend  extends AbstractCommand{

	private ArrayList<ShapeIF> shapes;
	private boolean array[];

	public FilledCommend(ArrayList<ShapeIF>shapes){
		this.shapes = new ArrayList<ShapeIF>(shapes.size());
		this.shapes.addAll(shapes);
		array = new boolean[shapes.size()];
		
	}
	
	public void doIt() {
		Iterator<ShapeIF> itr = shapes.iterator();
		int i = 0;
		while(itr.hasNext()){
			ShapeIF shape = itr.next();
			array[i++] = shape.getFilled();
			shape.setFilled(true);
		}
		
		
	}

	@Override
	public void undoIt() {
		Iterator<ShapeIF> itr = shapes.iterator();
		int i = 0;
		while(itr.hasNext()){
			itr.next().setFilled(array[i++]);
		}
		
	}
	

}
