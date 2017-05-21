/**
 * Author Rusu George, Romain Maximilien
 */


package allPiece;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class Tour extends Piece{
	
	/**
	 * Constructor
	 * @param n la taille de la grille
	 * @param model le model choco en question
	 */
	public Tour(int n,Model model){
		super(n,"T",model);
	}
	/**
	 * fonction menace . Piece.menace(Piece)
	 * @param pieceCible piece qui sera attaqué
	 */
	public Constraint Menace(Piece pieceCible){
		Model model=this.getModel();
		Constraint memeLigne=model.arithm(this.getCoordLigne(), "=", pieceCible.getCoordLigne());
		Constraint memeColonne=model.arithm(this.getCoordColonne(), "=", pieceCible.getCoordColonne());
		return model.or(memeLigne,memeColonne);
	}
}
