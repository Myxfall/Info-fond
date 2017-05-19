package question5Choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class CameraNord extends Camera{
	
	public CameraNord(Model model, int dimensionLigne, int dimensionColonne){
		super(model,"N", dimensionLigne, dimensionColonne);
	}
	
	public Constraint surveille(Camera camera){
		Model model=this.getModel();
		Constraint memeLigne=model.arithm(this.getCoordLigne(), "<", camera.getCoordLigne());
		Constraint memeColonne=model.arithm(this.getCoordColonne(), "=", camera.getCoordColonne());
		return model.and(memeLigne,memeColonne);
	}
}
