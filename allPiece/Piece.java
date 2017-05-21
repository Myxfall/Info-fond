/**
 * Author Rusu George, Romain Maximilien
 */


package allPiece;

import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public abstract class Piece {

	private IntVar coordLigne;
	private IntVar coordColonne;
	private Model model;
	private String type;
	/**
	 * Constructor
	 * @param n taille grille
	 * @param type type de piece "T","C","F","V"
	 * @param model le model choco en question
	 */
	public Piece(int n,String type,Model model){
		this.type=type;
		this.model = model;
		this.coordLigne=this.model.intVar("coordLigne",0,n-1);
		this.coordColonne=this.model.intVar("coordColonne",0,n-1);
	}
	public IntVar getCoordLigne(){
		return this.coordLigne;
	}
	
	public IntVar getCoordColonne(){
		return this.coordColonne;
	}
	
	public Model getModel(){
		return this.model;
	}
	public String getType(){
		return this.type;
	}
	/**
	 * fonction menace . Piece.menace(Piece)
	 * @param pieceCible piece qui sera attaqué
	 */
	public abstract Constraint Menace(Piece pieceCible); //fonction a redefinir pour chaque type de pieces
	
	/**
	 * contrainte unicité :deux pieces doivent être distinctes
	 * @param pieceCible une autre piece
	 * @return contrainte que les pieces doivent être distinctes
	 */
	public Constraint unique(Piece pieceCible){ //contrainte unicité la meme pour toutes les pieces
		Model model=this.getModel();
		Constraint memeLigne=model.arithm(this.getCoordLigne(), "!=", pieceCible.getCoordLigne());
		Constraint memeColonne=model.arithm(this.getCoordColonne(), "!=", pieceCible.getCoordColonne());
		return model.or(memeLigne,memeColonne);
	}
}
