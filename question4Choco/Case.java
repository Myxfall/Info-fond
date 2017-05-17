package question4Choco;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import allPiece.Piece;

public class Case {
	private IntVar coordLigne;
	private IntVar coordColonne;
	private IntVar type;
	private Model model;
	
	public Case(Model model,int n){
		this.model=model;
		this.coordLigne=this.model.intVar("coordLigne",0,n-1);
		this.coordColonne=this.model.intVar("coordColonne",0,n-1);
		this.type=this.model.intVar("type",new int[]{0, 1});
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
	public IntVar getType(){
		return this.type;
	}
	public Constraint Menace(Case pieceCible){
		Model model=this.getModel();
		ArrayList<Constraint> cavalierAttaque=new ArrayList<Constraint>();
		
		//i+1 j+2
		Constraint memeLigne=model.arithm(this.getCoordLigne().add(1).intVar(), "=", pieceCible.getCoordLigne());
		Constraint memeColonne=model.arithm(this.getCoordColonne().add(2).intVar(), "=", pieceCible.getCoordColonne());
		cavalierAttaque.add(model.and(memeLigne,memeColonne));
		//i+1 j-2
		memeLigne=model.arithm(this.getCoordLigne().add(1).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().sub(2).intVar(), "=", pieceCible.getCoordColonne());
		cavalierAttaque.add(model.and(memeLigne,memeColonne));
		
		//i-1 j+2
		memeLigne=model.arithm(this.getCoordLigne().sub(1).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().add(2).intVar(), "=", pieceCible.getCoordColonne());
		cavalierAttaque.add(model.and(memeLigne,memeColonne));
		
		//i-1 i-2
		memeLigne=model.arithm(this.getCoordLigne().sub(1).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().sub(2).intVar(), "=", pieceCible.getCoordColonne());
		cavalierAttaque.add(model.and(memeLigne,memeColonne));
		
		//i+2 j+1
		memeLigne=model.arithm(this.getCoordLigne().add(2).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().add(1).intVar(), "=", pieceCible.getCoordColonne());
		cavalierAttaque.add(model.and(memeLigne,memeColonne));
		
		//i+2 j-1
		memeLigne=model.arithm(this.getCoordLigne().add(2).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().sub(1).intVar(), "=", pieceCible.getCoordColonne());
		cavalierAttaque.add(model.and(memeLigne,memeColonne));
		
		//i-2 j+1
		memeLigne=model.arithm(this.getCoordLigne().sub(2).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().add(1).intVar(), "=", pieceCible.getCoordColonne());
		cavalierAttaque.add(model.and(memeLigne,memeColonne));
		//i-2 j-1
		memeLigne=model.arithm(this.getCoordLigne().sub(2).intVar(), "=", pieceCible.getCoordLigne());
		memeColonne=model.arithm(this.getCoordColonne().sub(1).intVar(), "=", pieceCible.getCoordColonne());
		cavalierAttaque.add(model.and(memeLigne,memeColonne));
		
		return model.or(cavalierAttaque.toArray(new Constraint[]{}));
	}
	public Constraint unique(Case pieceCible){
		Model model=this.getModel();
		Constraint memeLigne=model.arithm(this.getCoordLigne(), "!=", pieceCible.getCoordLigne());
		Constraint memeColonne=model.arithm(this.getCoordColonne(), "!=", pieceCible.getCoordColonne());
		return model.or(memeLigne,memeColonne);
	}
}
