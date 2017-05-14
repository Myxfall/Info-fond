package allPiece;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;


public class Fou extends Piece {

	public Fou(int n,Model model){
		super(n,"F",model);
	}
	
	public Constraint Menace(Piece pieceCible){
		Model model=this.getModel();
	    Constraint constraint = model.arithm(this.getCoordColonne().sub(pieceCible.getCoordColonne()).abs().intVar(),"=",this.getCoordLigne().sub(pieceCible.getCoordLigne()).abs().intVar());
	    return constraint;
	}
}
