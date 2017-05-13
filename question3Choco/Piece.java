package question3Choco;

import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public abstract class Piece {

	private IntVar coordLigne;
	private IntVar coordColonne;
	private Model model;
	private String type;
	
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
	public abstract Constraint Menace(Piece pieceCible);
	
	public Constraint unique(Piece pieceCible){
		Model model=this.getModel();
		Constraint memeLigne=model.arithm(this.getCoordLigne(), "!=", pieceCible.getCoordLigne());
		Constraint memeColonne=model.arithm(this.getCoordColonne(), "!=", pieceCible.getCoordColonne());
		return model.or(memeLigne,memeColonne);
	}
}
