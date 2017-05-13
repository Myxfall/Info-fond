package question3Choco;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class Vide extends Piece{
	
	public Vide(int n,Model model){
		super(n,"V",model);
	}
	
	public Constraint indepandance(Piece pieceCible){
		Model model=this.getModel();
	    Constraint contraint=null;
	    return contraint;
	}
}
