package question3Choco;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;

import allPiece.Cavalier;
import allPiece.Fou;
import allPiece.Piece;
import allPiece.Tour;
import allPiece.Vide;

public class Jeu {
	private int n;
	private int t;
	private int f;
	private int c;
	
	public Jeu(int n,int t, int f, int c){
		this.n=n;
		this.t=t;
		this.f=f;
		this.c=c;
		
		
	}
	public void independance(){
		System.out.println("Probleme d'indepandance");
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
				if (l!=k){
					Piece pieceAttaque=allPiece.get(l);
					Piece pieceSubit=allPiece.get(k);
					
					Constraint unique=pieceAttaque.unique(pieceSubit);
					unique.post();
					
					if ((pieceAttaque.getType()!="*") && (pieceSubit.getType()!="*")){
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
		    System.out.println("Pas de solution trouvée.");
		}
	}
		
	public void domination(){
		System.out.println("Probleme de domination");
		Model model=new Model("domination");
		ArrayList<Piece> allPiece=new ArrayList<Piece>();
		ArrayList<Constraint> OR_contraintes = new ArrayList<Constraint>();

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

					
					Constraint unique=pieceAttaque.unique(pieceSubit);
					unique.post();
					
					if ((pieceAttaque.getType()=="*") && (pieceSubit.getType()!="*")){
	
						Constraint attaque = pieceSubit.Menace(pieceAttaque);
						OR_contraintes.add(attaque);
					}
					//if (((pieceAttaque.getType()=="T") && (pieceSubit.getType()=="F")) || (pieceAttaque.getType()=="T" && pieceSubit.getType()=="C") || (pieceAttaque.getType()=="F" && pieceSubit.getType()=="C")){
					if ((pieceAttaque.getType()=="T") && (pieceSubit.getType()=="F")){
						Constraint attaque = model.not(pieceAttaque.Menace(pieceSubit));
						OR_contraintes.add(attaque);
					}
				}
			}
			if(!OR_contraintes.isEmpty()){
				model.or(OR_contraintes.toArray(new Constraint[]{})).post();
				OR_contraintes.clear();
			}

		}
		
		Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printingBoard(allPiece);
		}else {
		    System.out.println("Pas de solution trouvée.");
		}
	}
	
	public void printingBoard(ArrayList<Piece> allPiece){
		String[][] printingBoard=new String[this.n][this.n];
		for (int i=0;i<allPiece.size();i++){
			printingBoard[allPiece.get(i).getCoordLigne().getValue()][allPiece.get(i).getCoordColonne().getValue()]=allPiece.get(i).getType();
		}
		String line;
		for (int i=0;i<this.n;i++){
			line="";
			for (int j=0;j<this.n;j++){
					line+=printingBoard[i][j]+" ";
			}
			System.out.print(line+"\n");
		}
		
	}
}
