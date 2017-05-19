package allPiece;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class Vide extends Piece{
	
	public Vide(int n,Model model){
		super(n,"*",model);
	}
	
	public Constraint Menace(Piece pieceCible){
	    Constraint contraint=null;
	    return contraint;
	}
}
