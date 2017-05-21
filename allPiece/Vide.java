/**
 * Author Rusu George, Romain Maximilien
 */


package allPiece;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class Vide extends Piece{
	
	/**
	 * Constructor
	 * @param n la taille de la grille
	 * @param model le model choco en question
	 */
	public Vide(int n,Model model){
		super(n,"*",model);
	}
	/**
	 * fonction menace . Piece.menace(Piece)
	 * @param pieceCible piece qui sera attaqué -> ici c'est une case Vide donc cela renvoit null
	 */
	public Constraint Menace(Piece pieceCible){
	    Constraint contraint=null;
	    return contraint;
	}
}
