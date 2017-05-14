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
		this.n=4;
		this.t=2;
		this.f=1;
		this.c=2;
		
		
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
		for (int l=0;l<this.n*this.n;l++){
			for (int k=0;k<this.n*this.n;k++){
				System.out.print(l+" et "+k+"\n");
				if (l!=k){
					System.out.print(allPiece.get(l).getType()+ " et "+allPiece.get(k).getType()+"\n");
					Piece pieceAttaque=allPiece.get(l);
					Piece pieceSubit=allPiece.get(k);
					
					Constraint unique=pieceAttaque.unique(pieceSubit);
					unique.post();
					
					if ((pieceAttaque.getType()!="*") && (pieceSubit.getType()!="*")){
					//	System.out.print(pieceAttaque.getType()+"attaque "+pieceSubit.getType()+"\n");
						Constraint attaque= model.not(pieceAttaque.Menace(pieceSubit));
						attaque.post();
					}
				}
			}
		}
		
		Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printingBoard(allPiece);
		}else {
			this.printingBoard(allPiece);
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
					//System.out.print(allPiece.get(l).getType()+ " et "+allPiece.get(k).getType()+"\n");
					Piece pieceAttaque=allPiece.get(l);
					Piece pieceSubit=allPiece.get(k);
					
					Constraint unique=pieceAttaque.unique(pieceSubit);
					unique.post();
					
					if ((pieceAttaque.getType()!="*") && (pieceSubit.getType()!="*")){
						System.out.print(allPiece.get(l).getType()+ " et "+allPiece.get(k).getType()+"\n");
					//	System.out.print(pieceAttaque.getType()+"attaque "+pieceSubit.getType()+"\n");
						Constraint attaque= pieceSubit.Menace(pieceAttaque);
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
			//System.out.print(allPiece.get(i).getCoordLigne().getValue()+" ,"+allPiece.get(i).getCoordLigne().getValue()+" ,"+allPiece.get(i).getType());
			printingBoard[allPiece.get(i).getCoordLigne().getValue()][allPiece.get(i).getCoordColonne().getValue()]=allPiece.get(i).getType();
		}
		String line;
		for (int i=0;i<this.n;i++){
			line="";
			for (int j=0;j<this.n;j++){
					line+=printingBoard[i][j]+" ";
				
				//printingBoard[i][j]="test";
			}
			System.out.print(line+"\n");
		}
		
	}
}
