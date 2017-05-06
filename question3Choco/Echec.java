package question3Choco;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.variables.IntVar;

public class Echec {
	private int n;
	private int t;
	private int f;
	private int c;
	
	
	
	public Echec(int n,int t, int f, int c){
		this.n=n;
		this.t=t;
		this.f=f;
		this.c=c;
		
		
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

	public void independance(){
		Model model = new Model(this.n + "-independance problem");
		IntVar[][] echequier =model.intVarMatrix("echequier",this.n,this.n,1,this.n*this.n); //0 vide 1 fou 2 cavalier 4 tour
		/*for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				for (int l=0;l<this.n;l++){
					for (int k=0;k<this.n;k++){
						//System.out.print(i+" "+j+ " "+l + " "+ k +"\n");
						if ((i!=l) || (j!=k)){
							//System.out.print("Dedans"+i+" "+j+ " "+l + " "+ k +"\n");
						model.arithm(echequier[i][j], "!=", echequier[l][k]).post(); //verification que chaque case est unique
						}
					}
				}
			}
		}*/
		ArrayList<Constraint> allContraintes= new ArrayList<Constraint>();
		for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				
				//verification que chaque case est unique
				for (int l=0;l<this.n;l++){
					for (int k=0;k<this.n;k++){
						if ((i!=l) || (j!=k)){
							model.arithm(echequier[i][j], "!=", echequier[l][k]).post(); //verification que chaque case est unique
						}
					}
				}
					
					
				//contrainte Tour
				Constraint c1= model.arithm(echequier[i][j], ">=", 1);
				Constraint c2= model.arithm(echequier[i][j], "<=", this.t);
				Constraint contrainteT=model.and(c1,c2);
				for (int l=0;l<this.n;l++){
					if (j!=l){
						Constraint tmp=model.and(contrainteT,model.arithm(echequier[i][l], ">", this.t+this.c+this.f)); //vide ligne
						allContraintes.add(tmp);
						
						//.add(tmp);
						//model.arithm(echequier[i][l], ">", this.t+this.c+this.f).post();
						
					}
					if(i!=l){
						Constraint tmp=model.and(contrainteT,model.arithm(echequier[l][j], ">", this.t+this.c+this.f)); //vide colonne
						//model.arithm(echequier[l][j], ">", this.t+this.c+this.f).post();
						allContraintes.add(tmp);
					}
				}
				//contrainte fou
				Constraint c3= model.arithm(echequier[i][j], ">=", this.t+1);
				Constraint c4= model.arithm(echequier[i][j], "<=", this.t+this.f);
				Constraint contrainteF=model.and(c3,c4);
				 //diagonale gauche vers la droite
				int ligne=0;
				int colonne=0;
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j+k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) && (colonne!=j)){
							Constraint tmp=model.and(contrainteF,model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f));
							allContraintes.add(tmp);
							//model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f).post();
						}
						//model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f).post():
						
					}
					
				}
				//diagonale droite vers la gauche
				ligne=0;
				colonne=0;
				for (int k=-this.max(i,n-1-j); k<this.n-this.min(i,n-1-j);k++){
					ligne=i+k;
					colonne=n-1-j-k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) && (colonne!=j)){
							//model.ifThen(model.and(c3,c4),model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f));
							Constraint tmp=model.and(contrainteF,model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f));
							allContraintes.add(tmp);
						}
					}
				}
						
						
				//contrainte cavalier
				Constraint c5= model.arithm(echequier[i][j], ">=", this.t+this.f+1);
				Constraint c6= model.arithm(echequier[i][j], "<=", this.t+this.f+this.c);
				Constraint contrainteC=model.and(c5,c6);
				if ((i+1<this.n) && (j+2<this.n)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i+1][j+2], ">", this.t+this.c+this.f));
					//model.arithm(echequier[i+1][j+2], ">", this.t+this.c+this.f).post();
					Constraint tmp=model.and(contrainteC,model.arithm(echequier[i+1][j+2], ">", this.t+this.c+this.f));
					allContraintes.add(tmp);
				}
				if ((i+1<this.n) && (j-2>=0)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i+1][j-2], ">", this.t+this.c+this.f)); 
					//model.arithm(echequier[i+1][j-2], ">", this.t+this.c+this.f).post();
					Constraint tmp=model.and(contrainteC,model.arithm(echequier[i+1][j-2], ">", this.t+this.c+this.f));
					allContraintes.add(tmp);
				}
				if ((i-1>=0) && (j+2<this.n)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i-1][j+2], ">", this.t+this.c+this.f)); vide
					//model.arithm(echequier[i-1][j+2], ">", this.t+this.c+this.f).post();
					Constraint tmp=model.and(contrainteC,model.arithm(echequier[i-1][j+2], ">", this.t+this.c+this.f));
					allContraintes.add(tmp);
				}
				if ((i-1>=0) && (j-2>=0)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i-1][j-2], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i-1][j-2], ">", this.t+this.c+this.f).post();
					Constraint tmp=model.and(contrainteC,model.arithm(echequier[i-1][j-2], ">", this.t+this.c+this.f));
					allContraintes.add(tmp);
				}
				if ((i+2<this.n) && (j+1<this.n)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i+2][j+1], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i+2][j+1], ">", this.t+this.c+this.f).post();
					Constraint tmp=model.and(contrainteC,model.arithm(echequier[i+2][j+1], ">", this.t+this.c+this.f));
					allContraintes.add(tmp);
				}
				if ((i+2<this.n) && (j-1>=0)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i+2][j-1], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i+2][j-1], ">", this.t+this.c+this.f).post();
					Constraint tmp=model.and(contrainteC,model.arithm(echequier[i+2][j-1], ">", this.t+this.c+this.f));
					allContraintes.add(tmp);
				}
				if ((i-2>=0) && (j+1<this.n)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i-2][j+1], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i-2][j+1], ">", this.t+this.c+this.f).post();
					Constraint tmp=model.and(contrainteC,model.arithm(echequier[i-2][j+1], ">", this.t+this.c+this.f));
					allContraintes.add(tmp);
				}
				if ((i-2>=0) && (j-1>=0)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i-2][j-1], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i-2][j-1], ">", this.t+this.c+this.f).post();
					Constraint tmp=model.and(contrainteC,model.arithm(echequier[i-2][j-1], ">", this.t+this.c+this.f));
					allContraintes.add(tmp);
				}
				
				//model.eva
				//model.or(allContraintes).post();
				
					
			}
		}
		
				
				/*
				//contrainte tour
				for (int l=0; l<this.n;l++){
					System.out.print(i+" "+j+" "+l+"\n");
					if (j!=l){
						System.out.print("A\n");
						model.ifThen(model.arithm(echequier[i][j],"=", 4), model.arithm(echequier[i][l], "=", 1));
					}
					if(i!=l){
						System.out.print("B\n");
						model.ifThen(model.arithm(echequier[i][j],"=", 4), model.arithm(echequier[l][j], "=", 1));
					}
				}*/
					//contraintes pour le cavalier
					
				/*model.ifThen(model.arithm(echequier[i][j],"=", 3), model.arithm(echequier[i+1][j+2], "=", 0));
				model.ifThen(model.arithm(echequier[i][j],"=", 3), model.arithm(echequier[i+1][j-2], "=", 0));
				model.ifThen(model.arithm(echequier[i][j],"=", 3), model.arithm(echequier[i-1][j+2], "=", 0));
				model.ifThen(model.arithm(echequier[i][j],"=", 3), model.arithm(echequier[i-1][j-2], "=", 0));
				model.ifThen(model.arithm(echequier[i][j],"=", 3), model.arithm(echequier[i+2][j+1], "=", 0));
				model.ifThen(model.arithm(echequier[i][j],"=", 3), model.arithm(echequier[i+2][j-1], "=", 0));
				model.ifThen(model.arithm(echequier[i][j],"=", 3), model.arithm(echequier[i-2][j+1], "=", 0));
				model.ifThen(model.arithm(echequier[i][j],"=", 3), model.arithm(echequier[i-2][j-1], "=", 0));*/
		
		
		//Solution solution = model.getSolver().findSolution();
		Solver solver = model.getSolver();
		System.out.print("After Solve");
		if(solver.solve()){
			System.out.print("Not ready ye\n");
		    this.printingBoard(echequier);
		}else {
		    System.out.println("The solver has proved the problem has no solution");
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

	public void domination(){
		
	}
	
}
