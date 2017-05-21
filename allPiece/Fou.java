/**
 * Author Rusu George, Romain Maximilien
 */


package allPiece;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;


public class Fou extends Piece {
	
	/**
	 * Constructor
	 * @param n la taille de la grille
	 * @param model le model choco en question
	 */
	public Fou(int n,Model model){
		super(n,"F",model);
	}
	
	/**
	 * fonction menace . Piece.menace(Piece)
	 * @param pieceCible piece qui sera attaqué
	 */
	public Constraint Menace(Piece pieceCible){
		Model model=this.getModel();
	    Constraint constraint = model.arithm(this.getCoordColonne().sub(pieceCible.getCoordColonne()).abs().intVar(),"=",this.getCoordLigne().sub(pieceCible.getCoordLigne()).abs().intVar());
	    return constraint;
	}
}
