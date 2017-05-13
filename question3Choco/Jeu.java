package question3Choco;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class Jeu {
	private int n;
	private int t;
	private int f;
	private int c;
	
	public Jeu(int n,int t, int f, int c){
		this.n=3;
		this.t=3;
		this.f=0;
		this.c=0;
		
		
	}
	public void independance(){
		Model model=new Model("independance");
		ArrayList<Piece> allPiece=new ArrayList<Piece>();
		for (int i=0;i<this.t;i++){
			Tour tour=new Tour(this.n,model);
			allPiece.add(tour);
		}
		for (int i=0;i<this.f;i++){
			Fou fou=new Fou(this.n,model);
			allPiece.add(fou);
		}
		for (int i=0;i<this.c;i++){
			Cavalier cavalier=new Cavalier(this.n,model);
			allPiece.add(cavalier);
		}
		int caseVide=(this.n*this.n)-(this.t+this.c+this.f);
		for (int i=0;i<caseVide;i++){
			Vide vide=new Vide(this.n,model);
			allPiece.add(vide);
		}
		System.out.print(caseVide);
		for (int l=0;l<this.n*this.n;l++){
			for (int k=0;k<this.n*this.n;k++){
				if (l!=k){
					Piece pieceAttaque=allPiece.get(l);
					Piece pieceSubit=allPiece.get(k);
					if (pieceAttaque.getType()!="V"){
						System.out.print(pieceAttaque.getType()+"attaque "+pieceSubit.getType()+"\n");
						Constraint attaque= pieceAttaque.indepandance(pieceSubit);
						attaque.post();
				}}
			}
		}
		
		Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printingBoard(allPiece);
		}else {
		    System.out.println("The solver has proved the problem has no solution");
		}
	}
		
	public void domination(){
		Model model=new Model("domination");
		ArrayList<Piece> allPiece=new ArrayList<Piece>();
		for (int i=0;i<this.t;i++){
			Tour tour=new Tour(this.n,model);
			allPiece.add(tour);
		}
		for (int i=0;i<this.f;i++){
			Fou fou=new Fou(this.n,model);
			allPiece.add(fou);
		}
		for (int i=0;i<this.c;i++){
			Cavalier cavalier=new Cavalier(this.n,model);
			allPiece.add(cavalier);
		}
		int caseVide=(this.n*this.n)-(this.t+this.c+this.f);
		for (int i=0;i<caseVide;i++){
			Vide vide=new Vide(this.n,model);
			allPiece.add(vide);
		}
		for (int l=0;l<this.n*this.n;l++){
			for (int k=0;k<this.n*this.n;k++){
				if (l!=k){
					Piece pieceAttaque=allPiece.get(l);
					Piece pieceSubit=allPiece.get(k);
					if ((pieceAttaque.getType()!="V") && (pieceSubit.getType()!="V")){
						System.out.print(pieceAttaque.getType()+"attaque "+pieceSubit.getType()+"\n");
						Constraint attaque= pieceAttaque.estMenace(pieceSubit);
						attaque.post();
					}
				}
			}
		}
		
		Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printingBoard(allPiece);
		}else {
		    System.out.println("The solver has proved the problem has no solution");
		}
	}
	
	public void printingBoard(ArrayList<Piece> allPiece){
		String[][] printingBoard=new String[this.n][this.n];
		for (int i=0;i<allPiece.size();i++){
			System.out.print(allPiece.get(i).getCoordLigne().getValue()+" ,"+allPiece.get(i).getCoordLigne().getValue()+" ,"+allPiece.get(i).getType());
			printingBoard[allPiece.get(i).getCoordLigne().getValue()][allPiece.get(i).getCoordLigne().getValue()]="test";
		}
		for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				System.out.print(printingBoard[i][j]);
			}
		}
		
	}
}
