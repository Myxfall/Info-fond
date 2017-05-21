/**
 * Author Rusu George, Romain Maximilien
 */


package allPiece;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class Pion extends Piece{
	
	/**
	 * Constructor
	 * @param n la taille de la grille
	 * @param model le model choco en question
	 */
	public Pion(int n,Model model){
		super(n,"P",model);
	}
	/**
	 * fonction menace . Piece.menace(Piece)
	 * @param pieceCible piece qui sera attaqué
	 */
	public Constraint Menace(Piece pieceCible){
		Model model=this.getModel();
		ArrayList<Constraint> pionAttaque=new ArrayList<Constraint>();
		
		//i+1 j+1
		Constraint memeLigne=model.arithm(this.getCoordLigne().add(1).intVar(), "=", pieceCible.getCoordLigne());
		Constraint memeColonne=model.arithm(this.getCoordColonne().add(1).intVar(), "=", pieceCible.getCoordColonne());
		pionAttaque.add(model.and(memeLigne,memeColonne));
		//i+1 j-1
		memeLigne=model.arithm(this.getCoordLigne().add(1).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().sub(1).intVar(), "=", pieceCible.getCoordColonne());
		pionAttaque.add(model.and(memeLigne,memeColonne));
		
		//i-1 j+1
		memeLigne=model.arithm(this.getCoordLigne().sub(1).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().add(1).intVar(), "=", pieceCible.getCoordColonne());
		pionAttaque.add(model.and(memeLigne,memeColonne));
		
		//i-1 i-1
		memeLigne=model.arithm(this.getCoordLigne().sub(1).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().sub(1).intVar(), "=", pieceCible.getCoordColonne());
		pionAttaque.add(model.and(memeLigne,memeColonne));
		
		return model.or(pionAttaque.toArray(new Constraint[]{}));
	}

}
