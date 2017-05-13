package question3Choco;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class Echec {
	private int n;
	private int t;
	private int f;
	private int c;
	
	
	
	public Echec(int n,int t, int f, int c){
		this.n=4;
		this.t=2;
		this.f=1;
		this.c=2;
		
		
	}
	
	private void printingBoard(IntVar[][] echequier){
		String line;
		for (int i=0;i<this.n;i++){
			line="";
			for (int j=0;j<this.n;j++){
				//System.out.println(echequier[i][j]);
				if ((1 <= echequier[i][j].getLB()) &&(echequier[i][j].getLB() <=this.t)){ //1 -> k1 Tour
					line+="T ";
				}else if ((this.t+1 <= echequier[i][j].getLB()) &&(echequier[i][j].getLB() <=this.t+this.f)){ //k1+1 ->k1+k2 Fou
					line+="F ";
				}else if ((this.t+this.f+1 <= echequier[i][j].getLB()) &&(echequier[i][j].getLB() <=this.t+this.f+this.c)){ //k1+k2+1 -> k1+k2+k3 cavalier
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
				line+=echequier[i][j].getLB() + " ";
			}
			System.out.println(line);
		}
			
	}
	
	private int max(int i, int j) {
		int res=i;
		if (res<j){
			res=j;
		}
		return res;
	}
	private int min(int i, int j) {
		int res=i;
		if (res>j){
			res=j;
		}
		return res;
	}

	public void independance(){
		Model model = new Model("-independance problem");
		IntVar[][] echequier =model.intVarMatrix("echequier",this.n,this.n,1,this.n*this.n);
		ArrayList<Constraint> AllTour=new ArrayList<Constraint>();
		ArrayList<Constraint> AllFou=new ArrayList<Constraint>();
		ArrayList<Constraint> AllCavalier=new ArrayList<Constraint>();
		ArrayList<Constraint> AllContrainte=new ArrayList<Constraint>();
		ArrayList<Constraint> AllContrainteCase=new ArrayList<Constraint>();
		for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				
				/*
				 * Contrainte unicité de la grille
				 */
				for (int l=0;l<this.n;l++){
					for (int k=0;k<this.n;k++){
						if ((i!=l) || (j!=k)){
							model.arithm(echequier[i][j], "!=", echequier[l][k]).post();
						}
					}
				}
				
				/*
				 * Contrainte pour une tour
				 */
				Constraint Tour1= model.arithm(echequier[i][j], ">=", 1);
				Constraint Tour2= model.arithm(echequier[i][j], "<=", this.t);
				Constraint EstTour=model.and(Tour1,Tour2);
				
				
				AllTour.add(EstTour);
				for (int l=0;l<this.n;l++){
					if (j!=l){
						Constraint contrainte= model.arithm(echequier[i][l], ">", this.t+this.c+this.f); //vide ligne
						AllTour.add(contrainte);
					}
					if(i!=l){
						Constraint contrainte=model.arithm(echequier[l][j], ">", this.t+this.c+this.f); //vide colonne
						AllTour.add(contrainte);
					}
				}
				AllContrainteCase.add(model.and(AllTour.toArray(new Constraint[]{})));
				AllTour.clear();
				
				/*
				 * Contrainte Fou
				 */
				Constraint Fou1= model.arithm(echequier[i][j], ">=", this.t+1);
				Constraint Fou2= model.arithm(echequier[i][j], "<=", this.t+this.f);
				Constraint EstFou=model.and(Fou1,Fou2);
				AllFou.add(EstFou);
				
				int ligne=0;
				int colonne=0;
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j+k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							Constraint contrainte=model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f);
							AllFou.add(contrainte);
						}
					}
				}
				
				ligne=0;
				colonne=0;
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j-k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							Constraint contrainte=model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f);
							AllFou.add(contrainte);
						}
					}
				}
				AllContrainteCase.add(model.and(AllFou.toArray(new Constraint[]{})));
				AllFou.clear();

				/*
				 * Contrainte Cavalier
				 */
				Constraint Cavalier1= model.arithm(echequier[i][j], ">=", this.t+this.f+1);
				Constraint Cavalier2= model.arithm(echequier[i][j], "<=", this.t+this.f+this.c);
				Constraint EstCavalier=model.and(Cavalier1,Cavalier2);
				AllCavalier.add(EstCavalier);
				
				if ((i+1<this.n) && (j+2<this.n)){
					Constraint contrainte=model.arithm(echequier[i+1][j+2], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
					
				}
				if ((i+1<this.n) && (j-2>=0)){
					Constraint contrainte=model.arithm(echequier[i+1][j-2], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i-1>=0) && (j+2<this.n)){
					Constraint contrainte=model.arithm(echequier[i-1][j+2], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i-1>=0) && (j-2>=0)){
					Constraint contrainte=model.arithm(echequier[i-1][j-2], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i+2<this.n) && (j+1<this.n)){
					Constraint contrainte=model.arithm(echequier[i+2][j+1], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
					
				}
				if ((i+2<this.n) && (j-1>=0)){
					Constraint contrainte=model.arithm(echequier[i+2][j-1], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i-2>=0) && (j+1<this.n)){
					Constraint contrainte=model.arithm(echequier[i-2][j+1], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i-2>=0) && (j-1>=0)){
					Constraint contrainte=model.arithm(echequier[i-2][j-1], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}

				if (!AllCavalier.isEmpty()){
					AllContrainteCase.add(model.and(AllCavalier.toArray(new Constraint[]{})));
					AllCavalier.clear();
				}

				AllContrainte.add(model.or(AllContrainteCase.toArray(new Constraint[]{})));
				AllContrainteCase.clear();
			}

			model.or(AllContrainte.toArray(new Constraint[]{})).post();
			AllContrainte.clear();
		}

		Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printingBoard(echequier);
		}else {
		    System.out.println("Pas de solution trouvé");
		}
		
	}
	
	
	public void domination(){
		Model model = new Model("-independance problem");
		IntVar[][] echequier =model.intVarMatrix("echequier",this.n,this.n,1,this.n*this.n);
		ArrayList<Constraint> AllTour=new ArrayList<Constraint>();
		ArrayList<Constraint> AllFou=new ArrayList<Constraint>();
		ArrayList<Constraint> AllCavalier=new ArrayList<Constraint>();
		ArrayList<Constraint> AllContrainte=new ArrayList<Constraint>();
		ArrayList<Constraint> AllContrainteCase=new ArrayList<Constraint>();
		for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				
				/*
				 * Contrainte unicité de la grille
				 */
				for (int l=0;l<this.n;l++){
					for (int k=0;k<this.n;k++){
						if ((i!=l) || (j!=k)){
							model.arithm(echequier[i][j], "!=", echequier[l][k]).post();
						}
					}
				}
				
				/*
				 * Contrainte pour une tour
				 */
				Constraint Tour1= model.arithm(echequier[i][j], ">=", 1);
				Constraint Tour2= model.arithm(echequier[i][j], "<=", this.t);
				Constraint EstTour=model.and(Tour1,Tour2);
				
				
				//AllTour.add(EstTour);
				for (int l=0;l<this.n;l++){
					if (j!=l){
						Constraint contrainte= model.arithm(echequier[i][l], ">", this.t+this.c+this.f); //vide ligne
						AllTour.add(contrainte);
					}
					if(i!=l){
						Constraint contrainte=model.arithm(echequier[l][j], ">", this.t+this.c+this.f); //vide colonne
						AllTour.add(contrainte);
					}
				}
				AllContrainteCase.add(model.and(EstTour,model.or(AllTour.toArray(new Constraint[]{}))));
				AllTour.clear();
				
				/*
				 * Contrainte Fou
				 */
				Constraint Fou1= model.arithm(echequier[i][j], ">=", this.t+1);
				Constraint Fou2= model.arithm(echequier[i][j], "<=", this.t+this.f);
				Constraint EstFou=model.and(Fou1,Fou2);
				//AllFou.add(EstFou);
				
				int ligne=0;
				int colonne=0;
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j+k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							System.out.print("("+ligne+","+colonne+")\n");
							Constraint contrainte=model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f);
							AllFou.add(contrainte);
						}
					}
				}
				
				ligne=0;
				colonne=0;
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j-k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							System.out.print("("+ligne+","+colonne+")\n");
							Constraint contrainte=model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f);
							AllFou.add(contrainte);
						}
					}
				}
				AllContrainteCase.add(model.and(EstFou,model.or(AllFou.toArray(new Constraint[]{}))));
				AllFou.clear();

				/*
				 * Contrainte Cavalier
				 */
				Constraint Cavalier1= model.arithm(echequier[i][j], ">=", this.t+this.f+1);
				Constraint Cavalier2= model.arithm(echequier[i][j], "<=", this.t+this.f+this.c);
				Constraint EstCavalier=model.and(Cavalier1,Cavalier2);
				//AllCavalier.add(EstCavalier);
				
				if ((i+1<this.n) && (j+2<this.n)){
					Constraint contrainte=model.arithm(echequier[i+1][j+2], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
					
				}
				if ((i+1<this.n) && (j-2>=0)){
					Constraint contrainte=model.arithm(echequier[i+1][j-2], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i-1>=0) && (j+2<this.n)){
					Constraint contrainte=model.arithm(echequier[i-1][j+2], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i-1>=0) && (j-2>=0)){
					Constraint contrainte=model.arithm(echequier[i-1][j-2], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i+2<this.n) && (j+1<this.n)){
					Constraint contrainte=model.arithm(echequier[i+2][j+1], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
					
				}
				if ((i+2<this.n) && (j-1>=0)){
					Constraint contrainte=model.arithm(echequier[i+2][j-1], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i-2>=0) && (j+1<this.n)){
					Constraint contrainte=model.arithm(echequier[i-2][j+1], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}
				if ((i-2>=0) && (j-1>=0)){
					Constraint contrainte=model.arithm(echequier[i-2][j-1], ">", this.t+this.c+this.f);
					AllCavalier.add(contrainte);
				}

				if (!AllCavalier.isEmpty()){
					AllContrainteCase.add(model.and(EstCavalier,model.or(AllCavalier.toArray(new Constraint[]{}))));
					AllCavalier.clear();
				}

				AllContrainte.add(model.or(AllContrainteCase.toArray(new Constraint[]{})));
				AllContrainteCase.clear();
			}

			model.or(AllContrainte.toArray(new Constraint[]{})).post();
			AllContrainte.clear();
		}

		Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printingBoard(echequier);
			this.printNumber(echequier);
		}else {
		    System.out.println("The solver has proved the problem has no solution");
		}
		
	}
	
}
