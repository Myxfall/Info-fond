package question4Choco;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import allPiece.Cavalier;
import allPiece.Piece;
import allPiece.Vide;

public class MinCavalier {
	private int n;
	private int nombreCavalier;
	private Model model;
	
	public MinCavalier(int n){
		this.n=n;
		//this.nombreCavalier=this.model.intVar("nombreCavalier",1,this.n*this.n);
		this.nombreCavalier=this.n-1;  
		/** 
		 *4*4= 16 on a 16 cases a toucher alors qu'on sait que un cavalier touche dans le meilleur des cas.
		 * Pour atteindre ce meilleur des cas on doit avoir plus de 3 cavalier (plus de 12 cases) -> donc n-1
		 */
		
	}
	
	public void printingeBoard(ArrayList<Piece> allPiece){
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
	public void findMin(){
		ArrayList<Piece> allPiece=new ArrayList<Piece>();
		ArrayList<Constraint> OR_contraintes = new ArrayList<Constraint>();
		Boolean findSolution=false;
		do{
			this.model=new Model("minimisation-cavalier");
			allPiece.clear();
			OR_contraintes.clear();
			this.nombreCavalier++;
			
			System.out.print("INCREMENTATION"+ this.nombreCavalier+'\n');
			for (int i=0;i<this.nombreCavalier;i++){
				Cavalier cavalier=new Cavalier(this.n,model);
				allPiece.add(cavalier);
			}
			int caseVide=(this.n*this.n)-(this.nombreCavalier);
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
						
						if ((pieceAttaque.getType()=="*") && (pieceSubit.getType()!="*")){
							//System.out.print(allPiece.get(l).getType()+ " et "+allPiece.get(k).getType()+"\n");
							System.out.print(pieceAttaque.getType()+"attaque "+pieceSubit.getType()+"\n");
							//Constraint attaque= pieceAttaque.Menace(pieceSubit);;
							
							Constraint attaque = pieceSubit.Menace(pieceAttaque);
							OR_contraintes.add(attaque);
						}
						
					}
				}
				if(!OR_contraintes.isEmpty()){
					model.or(OR_contraintes.toArray(new Constraint[]{})).post();
					OR_contraintes.clear();
				}
			}
			if(model.getSolver().solve()){
				findSolution=true;
			}
			}while((!findSolution) && (this.nombreCavalier<this.n*this.n));
			//Solver solver=model.getSolver();
			if(findSolution){
				this.printingeBoard(allPiece);
			}else {
				//this.nombreCavalier.add(1);
				
				//System.out.print(this.nombreCavalier);
				System.out.println("The solver has proved the problem has no solution");
			}

	}
	private void printingBoard(IntVar[][] echequier,IntVar numCavalier){
		String line;
		for (int i=0;i<this.n;i++){
			line="";
			for (int j=0;j<this.n;j++){
				if (echequier[i][j].getValue() == 1){ //1 -> k1 Tour
					line+="C ";
				}else{
					line+="* ";
				}
			}				
			System.out.println(line);
		}
	}
	private void printNumber(IntVar[][] echequier){
		String line;
		for (int i=0;i<this.n;i++){
			line="";
			for (int j=0;j<this.n;j++){
				line+=echequier[i][j].getValue() + " ";
			}
			System.out.println(line);
		}
			
	}
	public Constraint menace(Model model,int ligne,int colonne, IntVar[][] echequier){
		ArrayList<Constraint> cavalierAttaque=new ArrayList<Constraint>();
		
		//i+1 j+2
		if ((ligne+1<this.n) && (colonne+2<this.n)){
			Constraint menace=model.arithm(echequier[ligne+1][colonne+2], "!=", echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		//i+1 j-2
		if ((ligne+1<this.n) && (colonne-2>=0)){
			Constraint menace=model.arithm(echequier[ligne+1][colonne-2], "!=", echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		//i-1 j+2
		
		if ((ligne-1>=0) && (colonne+2<this.n)){
			Constraint menace=model.arithm(echequier[ligne-1][colonne+2], "!=", echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		if ((ligne-1>=0) && (colonne-2>=0)){
			//i-1 j-2
			Constraint menace=model.arithm(echequier[ligne-1][colonne-2], "!=", echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		if ((ligne+2<this.n) && (colonne+1<this.n)){
			//i+2 j+1
			Constraint menace=model.arithm(echequier[ligne+2][colonne+1], "!=", echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		if ((ligne+2<this.n) && (colonne-1>=0)){
			//i+2 j-1
			Constraint menace=model.arithm(echequier[ligne+2][colonne-1], "!=",echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		if ((ligne-2>=0) && (colonne+1<this.n)){
			//i-2 j+1
			Constraint menace=model.arithm(echequier[ligne-2][colonne+1], "!=",echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		if ((ligne-2>=0) && (colonne-1>=0)){
			//i-2 j-1
			Constraint menace=model.arithm(echequier[ligne-2][colonne-1], "!=", echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		if (!cavalierAttaque.isEmpty()){
			return model.or(cavalierAttaque.toArray(new Constraint[]{}));
		}
		return null;
		
	}
	
	
	public void MinimizationCavalier(){
		Model model=new Model("tester");
		IntVar sum=model.intVar("sum", 0, this.n*this.n);
		IntVar numCavalier = model.intVar("objective", 1, this.n*this.n);
		IntVar[][] echequier =model.intVarMatrix("echequier",this.n,this.n,0,1);
		for (int l=0;l<this.n;l++){
			for (int k=0;k<this.n;k++){
				//System.out.print("dehors");
				 //si Cavalier
					if (this.menace(model,l,k,echequier)!=null){
						this.menace(model,l,k,echequier).post();
					}
					else{
						model.arithm(echequier[l][k], "=",1).post();;
					}
					//System.out.print("Cavalier");
					sum=sum.add(echequier[l][k]).intVar();
			}
			
		}
		//model.among(numCavalier, echequier[0],1);
		numCavalier.eq(sum).post();
		model.setObjective(Model.MINIMIZE, numCavalier);
		Solver solver = model.getSolver();
		while(solver.solve()){
			System.out.print("\n");
			
			this.printingBoard(echequier,numCavalier);
			//this.printNumber(echequier);
			//solver.findOptimalSolution(numCavalier, , stop)
		}//System.out.print(Caval.getValue());
		/*if (solver.solve()){
			System.out.print("Une SOLUTION");
		}*/
		//this.printingBoard(echequier,numCavalier);
	}
	
	public void test(){
		ArrayList<Piece> allPiece=new ArrayList<Piece>();
		ArrayList<Constraint> OR_contraintes = new ArrayList<Constraint>();
		IntVar numCavalier = model.intVar("objective", 1, this.n*this.n);
		this.model=new Model("minimisation-cavalier");
			
		for (int i=0;i<numCavalier.getValue();i++){
			Cavalier cavalier=new Cavalier(this.n,model);
			allPiece.add(cavalier);
		}
		int caseVide=(this.n*this.n)-(this.nombreCavalier);
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
						
						if ((pieceAttaque.getType()=="*") && (pieceSubit.getType()!="*")){
							//System.out.print(allPiece.get(l).getType()+ " et "+allPiece.get(k).getType()+"\n");
							System.out.print(pieceAttaque.getType()+"attaque "+pieceSubit.getType()+"\n");
							//Constraint attaque= pieceAttaque.Menace(pieceSubit);;
							
							Constraint attaque = pieceSubit.Menace(pieceAttaque);
							OR_contraintes.add(attaque);
						}
						
					}
				}
				if(!OR_contraintes.isEmpty()){
					model.or(OR_contraintes.toArray(new Constraint[]{})).post();
					OR_contraintes.clear();
				}
			}
			
			/*if(findSolution){
				this.printingeBoard(allPiece);
			}else {
				//this.nombreCavalier.add(1);
				
				//System.out.print(this.nombreCavalier);
				System.out.println("The solver has proved the problem has no solution");
			}*/
	}
}
