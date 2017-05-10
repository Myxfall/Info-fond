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
		IntVar[][] echequier =model.intVarMatrix("echequier",this.n,this.n,1,this.n*this.n); //0 vide 1 fou 2 cavalier 4 tour

		ArrayList<Constraint> ContraintesUnique= new ArrayList<Constraint>();
		ArrayList<Constraint> ContrainteT=new ArrayList<Constraint>();
		ArrayList<Constraint> ContrainteF=new ArrayList<Constraint>();
		ArrayList<Constraint> ContrainteC=new ArrayList<Constraint>();
		ArrayList<Constraint> ALLContraintes= new ArrayList<Constraint>();
		ArrayList<Constraint> ALLColonnes= new ArrayList<Constraint>();
		for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				System.out.print("\nPosition"+ i+ " "+ j+ "\n");
				//verification que chaque case est unique
				for (int l=0;l<this.n;l++){
					for (int k=0;k<this.n;k++){
						if ((i!=l) || (j!=k)){
							 //verification que chaque case est unique
							ContraintesUnique.add(model.arithm(echequier[i][j], "!=", echequier[l][k]));
							//model.arithm(echequier[i][j], "!=", echequier[l][k]).post();
						}
					}
				}
			
				//contrainte Tour
				Constraint c1= model.arithm(echequier[i][j], ">=", 1);
				Constraint c2= model.arithm(echequier[i][j], "<=", this.t);
				Constraint contrainteT=model.and(c1,c2);
				Constraint contrainte=null;
				ContrainteT.clear();
				ContrainteT.add(contrainteT);
				System.out.print("\n Debut contrainte Tour: \n");
				for (int l=0;l<this.n;l++){
					if (j!=l){
						System.out.print("\n Emplacement vide Ligne: ("+i+","+l+") \n");
						contrainte= model.arithm(echequier[i][l], ">", this.t+this.c+this.f); //vide ligne
						ContrainteT.add(contrainte);
						
						
						
					}
					if(i!=l){
						System.out.print("\n Emplacement vide Ligne: ("+l+","+j+") \n");
						contrainte=model.arithm(echequier[l][j], ">", this.t+this.c+this.f); //vide colonne
						ContrainteT.add(contrainte);
					}
				}
				
				ALLContraintes.add(model.and(ContrainteT.toArray(new Constraint[]{})));
				//model.or(model.and(ContrainteT.toArray(new Constraint[]{}))).post();
				//contrainte fou
				Constraint c3= model.arithm(echequier[i][j], ">=", this.t+1);
				Constraint c4= model.arithm(echequier[i][j], "<=", this.t+this.f);
				Constraint contrainteF=model.and(c3,c4);
				ContrainteF.clear();
				ContrainteF.add(contrainteF);
				contrainte=null;

				 //diagonale gauche vers la droite
				int ligne=0;
				int colonne=0;
				System.out.print("\n Debut contrainte Fou: \n");
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j+k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							System.out.print("\n Emplacement vide G-D: ("+ligne+","+colonne+") \n");
							contrainte=model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f);
							ContrainteF.add(contrainte);
						}
					}
				}

				
				//diagonale droite vers la gauche
				ligne=0;
				colonne=0;
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j-k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							System.out.print("\n Emplacement vide D-G: ("+ligne+","+colonne+") \n");
							contrainte=model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f);
							ContrainteF.add(contrainte);
						}
					}
				}
				ALLContraintes.add(model.and(ContrainteF.toArray(new Constraint[]{})));
				//model.or(model.and(ContrainteF.toArray(new Constraint[]{}))).post();
				//Contrainte Cavalier
				
				Constraint c5= model.arithm(echequier[i][j], ">=", this.t+this.f+1);
				Constraint c6= model.arithm(echequier[i][j], "<=", this.t+this.f+this.c);
				Constraint contrainteC=model.and(c5,c6);
				ContrainteC.clear();
				ContrainteC.add(contrainteC);
				System.out.print("\n Debut contrainte Cavalier: \n");
				if ((i+1<this.n) && (j+2<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i+1)+","+(j+2)+") \n");
					contrainte=model.arithm(echequier[i+1][j+2], ">", this.t+this.c+this.f);
					ContrainteC.add(contrainte);
					
				}
				if ((i+1<this.n) && (j-2>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i+1)+","+(j-2)+") \n");
					contrainte=model.arithm(echequier[i+1][j-2], ">", this.t+this.c+this.f);
					ContrainteC.add(contrainte);
				}
				if ((i-1>=0) && (j+2<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i-1)+","+(j+2)+") \n");
					contrainte=model.arithm(echequier[i-1][j+2], ">", this.t+this.c+this.f);
					ContrainteC.add(contrainte);
				}
				if ((i-1>=0) && (j-2>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i-1)+","+(j-2)+") \n");
					contrainte=model.arithm(echequier[i-1][j-2], ">", this.t+this.c+this.f);
					ContrainteC.add(contrainte);
				}
				if ((i+2<this.n) && (j+1<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i+2)+","+(j+1)+") \n");
					contrainte=model.arithm(echequier[i+2][j+1], ">", this.t+this.c+this.f);
					ContrainteC.add(contrainte);
					
				}
				if ((i+2<this.n) && (j-1>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i+2)+","+(j-1)+") \n");
					contrainte=model.arithm(echequier[i+2][j-1], ">", this.t+this.c+this.f);
					ContrainteC.add(contrainte);
				}
				if ((i-2>=0) && (j+1<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i-2)+","+(j+1)+") \n");
					contrainte=model.arithm(echequier[i-2][j+1], ">", this.t+this.c+this.f);
					ContrainteC.add(contrainte);
				}
				if ((i-2>=0) && (j-1>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i-2)+","+(j-1)+") \n");
					contrainte=model.arithm(echequier[i-2][j-1], ">", this.t+this.c+this.f);
					ContrainteC.add(contrainte);
					
				}
				
				ALLContraintes.add(model.and(ContrainteC.toArray(new Constraint[]{})));
				
				ALLColonnes.add(model.or(ALLContraintes.toArray(new Constraint[]{})));
				ALLContraintes.clear();
				
			}
			
			model.or(ALLColonnes.toArray(new Constraint[]{})).post();
			ALLColonnes.clear();
			model.and(ContraintesUnique.toArray(new Constraint[]{})).post();
			ContraintesUnique.clear();
		}
		
		
		Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printingBoard(echequier);
			this.printNumber(echequier);
			System.out.print(ALLContraintes);
		}else {
		    System.out.println("The solver has proved the problem has no solution");
		}
		
	}
	public void domination(){
		Model model = new Model("-domination problem");
		IntVar[][] echequier =model.intVarMatrix("echequier",this.n,this.n,1,this.n*this.n); 

		ArrayList<Constraint> ContraintesUnique= new ArrayList<Constraint>();
		ArrayList<Constraint> ContrainteT=new ArrayList<Constraint>();
		ArrayList<Constraint> ContrainteF=new ArrayList<Constraint>();
		ArrayList<Constraint> ContrainteC=new ArrayList<Constraint>();
		ArrayList<Constraint> ALLContraintes= new ArrayList<Constraint>();
		ArrayList<Constraint> ALLColonnes= new ArrayList<Constraint>();
		for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				System.out.print("\nPosition"+ i+ " "+ j+ "\n");
				//verification que chaque case est unique
				for (int l=0;l<this.n;l++){
					for (int k=0;k<this.n;k++){
						if ((i!=l) || (j!=k)){
							 //verification que chaque case est unique
							ContraintesUnique.add(model.arithm(echequier[i][j], "!=", echequier[l][k]));
							//model.arithm(echequier[i][j], "!=", echequier[l][k]).post();
						}
					}
				}
			
				//contrainte Tour
				Constraint c1= model.arithm(echequier[i][j], ">=", this.t+this.f+this.c+1);
				Constraint c2= model.arithm(echequier[i][j], "<=", this.n*this.n);
				Constraint contrainteV=model.and(c1,c2);
				
				Constraint contrainte=null;
				Constraint contrainte1=null;
				Constraint contrainte2=null;
				ContrainteT.clear();
				//ContrainteT.add(contrainteT);
				System.out.print("\n Debut contrainte Vide: \n");
				for (int l=0;l<this.n;l++){
					if (j!=l){
						System.out.print("\n Emplacement vide Ligne: ("+i+","+l+") \n");
						//contrainte= model.arithm(echequier[i][l], "<=", this.t+this.c+this.f); //vide ligne
						//Correction en this.t
						contrainte1=model.arithm(echequier[i][l], "<=", this.t);
						contrainte2=model.arithm(echequier[i][l], ">=", 1);
						contrainte=model.and(contrainte1, contrainte2);
						ContrainteT.add(contrainte);
					}
					if(i!=l){
						System.out.print("\n Emplacement vide Ligne: ("+l+","+j+") \n");
						//contrainte=model.arithm(echequier[l][j], "<=", this.t+this.c+this.f); //vide colonne
						//Correction en this.t
						contrainte1=model.arithm(echequier[i][l], "<=", this.t);
						contrainte2=model.arithm(echequier[i][l], ">=", 1);
						contrainte=model.and(contrainte1, contrainte2);
						ContrainteT.add(contrainte);
					}
				}
				
				
				ALLContraintes.add(model.or(ContrainteT.toArray(new Constraint[]{})));
				//model.or(model.and(ContrainteT.toArray(new Constraint[]{}))).post();
				//contrainte fou
				//Constraint c3= model.arithm(echequier[i][j], ">=", this.t+this.f+this.c+1);
				//Constraint c4= model.arithm(echequier[i][j], "<=", this.n*this.n);
				//Constraint contrainteF=model.and(c3,c4);
				ContrainteF.clear();
				//ContrainteF.add(contrainteF);
				contrainte=null;
				contrainte1 = null;
				contrainte2 = null;

				 //diagonale gauche vers la droite
				int ligne=0;
				int colonne=0;
				System.out.print("\n Debut contrainte Fou: \n");
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j+k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							System.out.print("\n Emplacement vide G-D: ("+ligne+","+colonne+") \n");
							//contrainte=model.arithm(echequier[ligne][colonne], "<=", this.t+this.c+this.f);
							//Correction en this.t+this.f
							contrainte1=model.arithm(echequier[ligne][colonne], "<=", this.t+this.f);
							contrainte2 = model.arithm(echequier[ligne][colonne], ">=", this.t+1);
							contrainte = model.and(contrainte1, contrainte2);
							ContrainteF.add(contrainte);
						}
					}
				}

				
				//diagonale droite vers la gauche
				ligne=0;
				colonne=0;
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j-k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							System.out.print("\n Emplacement vide D-G: ("+ligne+","+colonne+") \n");
							//contrainte=model.arithm(echequier[ligne][colonne], "<=", this.t+this.c+this.f);
							//Correction en this.t+this.f
							contrainte1=model.arithm(echequier[ligne][colonne], "<=", this.t+this.f);
							contrainte2 = model.arithm(echequier[ligne][colonne], ">=", this.t+1);
							contrainte = model.and(contrainte1, contrainte2);
							ContrainteF.add(contrainte);
						}
					}
				}
				ALLContraintes.add(model.or(ContrainteF.toArray(new Constraint[]{})));
				//model.or(model.and(ContrainteF.toArray(new Constraint[]{}))).post();
				//Contrainte Cavalier
				
				//Constraint c5= model.arithm(echequier[i][j], ">=", this.t+this.f+this.c+1);
				//Constraint c6= model.arithm(echequier[i][j], "<=", this.n*this.n);
				//Constraint contrainteC=model.and(c5,c6);
				ContrainteC.clear();
				//ContrainteC.add(contrainteC);
				System.out.print("\n Debut contrainte Cavalier: \n");
				if ((i+1<this.n) && (j+2<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i+1)+","+(j+2)+") \n");
					contrainte1=model.arithm(echequier[i+1][j+2], "<=", this.t+this.c+this.f);
					contrainte2 = model.arithm(echequier[i+1][j+2], ">=", this.t+this.f+1);
					contrainte = model.and(contrainte1, contrainte2);
					ContrainteC.add(contrainte);
					
				}
				if ((i+1<this.n) && (j-2>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i+1)+","+(j-2)+") \n");
					contrainte1=model.arithm(echequier[i+1][j-2], "<=", this.t+this.c+this.f);
					contrainte2 = model.arithm(echequier[i+1][j-2], ">=", this.t+this.f+1);
					contrainte = model.and(contrainte1, contrainte2);
					ContrainteC.add(contrainte);
				}
				if ((i-1>=0) && (j+2<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i-1)+","+(j+2)+") \n");
					contrainte1=model.arithm(echequier[i-1][j+2], "<=", this.t+this.c+this.f);
					contrainte2 = model.arithm(echequier[i-1][j+2], ">=", this.t+this.f+1);
					contrainte = model.and(contrainte1, contrainte2);
					ContrainteC.add(contrainte);
				}
				if ((i-1>=0) && (j-2>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i-1)+","+(j-2)+") \n");
					contrainte1=model.arithm(echequier[i-1][j-2], "<=", this.t+this.c+this.f);
					contrainte2 = model.arithm(echequier[i-1][j-2], ">=", this.t+this.f+1);
					contrainte = model.and(contrainte1, contrainte2);
					ContrainteC.add(contrainte);
				}
				if ((i+2<this.n) && (j+1<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i+2)+","+(j+1)+") \n");
					contrainte1=model.arithm(echequier[i+2][j+1], "<=", this.t+this.c+this.f);
					contrainte2=model.arithm(echequier[i+2][j+1], ">=", this.t+this.f+1);
					contrainte = model.and(contrainte1, contrainte2);
					ContrainteC.add(contrainte);
					
				}
				if ((i+2<this.n) && (j-1>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i+2)+","+(j-1)+") \n");
					contrainte1=model.arithm(echequier[i+2][j-1], "<=", this.t+this.c+this.f);
					contrainte2=model.arithm(echequier[i+2][j-1], ">=", this.t+this.f+1);
					contrainte = model.and(contrainte1, contrainte2);
					ContrainteC.add(contrainte);
				}
				if ((i-2>=0) && (j+1<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i-2)+","+(j+1)+") \n");
					contrainte1=model.arithm(echequier[i-2][j+1], "<=", this.t+this.c+this.f);
					contrainte2=model.arithm(echequier[i-2][j+1], ">=", this.t+this.f+1);
					contrainte = model.and(contrainte1, contrainte2);
					ContrainteC.add(contrainte);
				}
				if ((i-2>=0) && (j-1>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i-2)+","+(j-1)+") \n");
					contrainte=model.arithm(echequier[i-2][j-1], "<=", this.t+this.c+this.f);
					contrainte=model.arithm(echequier[i-2][j-1], ">=", this.t+this.f+1);
					contrainte = model.and(contrainte1, contrainte2);
					ContrainteC.add(contrainte);
				}
				//if (!ContrainteC.isEmpty()){
					//ALLContraintes.add(model.or(ContrainteC.toArray(new Constraint[]{})));
				//}
				ALLColonnes.add(model.and(contrainteV,model.or(ALLContraintes.toArray(new Constraint[]{}))));
				ALLContraintes.clear();
				
			}
			
			model.or(ALLColonnes.toArray(new Constraint[]{})).post();
			ALLColonnes.clear();
			model.and(ContraintesUnique.toArray(new Constraint[]{})).post();
			ContraintesUnique.clear();
		}
		
		
		Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printingBoard(echequier);
			this.printNumber(echequier);
			System.out.print(ALLContraintes);
		}else {
		    System.out.println("The solver has proved the problem has no solution");
		}
		
	}
}
