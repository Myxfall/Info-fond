package question5Choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class CameraSud extends Camera{
	
	public CameraSud(Model model, int dimensionLigne, int dimensionColonne){
		super(model,"S", dimensionLigne, dimensionColonne);
	}
	
	public Constraint surveille(Camera camera){
		Model model=this.getModel();
		Constraint memeLigne=model.arithm(this.getCoordLigne(), ">", camera.getCoordLigne());
		Constraint memeColonne=model.arithm(this.getCoordColonne(), "=", camera.getCoordColonne());
		return model.and(memeLigne,memeColonne);
	}
}
