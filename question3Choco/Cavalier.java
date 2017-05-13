package question3Choco;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class Cavalier extends Piece{
	
	public Cavalier(int n,Model model){
		super(n,"C",model);
	}
	
	public Constraint Menace(Piece pieceCible){
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

}
