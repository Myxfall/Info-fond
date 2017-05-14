package allPiece;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class Tour extends Piece{
	
		
	public Tour(int n,Model model){
		super(n,"T",model);
	}
	
	public Constraint Menace(Piece pieceCible){
		Model model=this.getModel();
		Constraint memeLigne=model.arithm(this.getCoordLigne(), "=", pieceCible.getCoordLigne());
		Constraint memeColonne=model.arithm(this.getCoordColonne(), "=", pieceCible.getCoordColonne());
		return model.or(memeLigne,memeColonne);
	}
}
