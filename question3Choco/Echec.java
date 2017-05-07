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

	public void independance(){
		Model model = new Model(this.n + "-independance problem");
		IntVar[][] echequier =model.intVarMatrix("echequier",this.n,this.n,1,this.n*this.n); //0 vide 1 fou 2 cavalier 4 tour

		ArrayList<Constraint> ContraintesUnique= new ArrayList<Constraint>();
		ArrayList<Constraint> ALLContraintes= new ArrayList<Constraint>();
		ArrayList<Constraint> contrainteCase= new ArrayList<Constraint>();
		//ArrayList<Constraint> ContraintesC= new ArrayList<Constraint>();
		ArrayList<Constraint> tmp= new ArrayList<Constraint>();
		ArrayList<Constraint> tmp1= new ArrayList<Constraint>();
		ArrayList<Constraint> tmp2= new ArrayList<Constraint>();
		for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				System.out.print("\nDebut"+ i+ " "+ j+ "\n");
				//verification que chaque case est unique
				for (int l=0;l<this.n;l++){
					for (int k=0;k<this.n;k++){
						if ((i!=l) || (j!=k)){
							 //verification que chaque case est unique
							//ContraintesUnique.add(model.arithm(echequier[i][j], "!=", echequier[l][k]));
							model.arithm(echequier[i][j], "!=", echequier[l][k]).post();
						}
					}
				}
		
				//contrainte Tour
				Constraint c1= model.arithm(echequier[i][j], ">=", 1);
				Constraint c2= model.arithm(echequier[i][j], "<=", this.t);
				Constraint contrainteT=model.and(c1,c2);
				tmp.clear();
				tmp1.clear();
				tmp2.clear();
				for (int l=0;l<this.n;l++){
					if (j!=l){
						tmp.add(model.and(contrainteT,model.arithm(echequier[i][l], ">=", this.t+this.c+this.f))); //vide ligne
						
						
					}
					if(i!=l){
						tmp.add(model.and(contrainteT,model.arithm(echequier[l][j], ">", this.t+this.c+this.f))); //vide colonne
					}
				}
				if(!tmp.isEmpty()){
					contrainteCase.add(model.and(tmp.toArray(new Constraint[]{})));
				}
				
				
				//contrainte fou
				Constraint c3= model.arithm(echequier[i][j], ">=", this.t+1);
				Constraint c4= model.arithm(echequier[i][j], "<=", this.t+this.f);
				Constraint contrainteF=model.and(c3,c4);
				tmp.clear();
				tmp1.clear();
				tmp2.clear();
				 //diagonale gauche vers la droite
				int ligne=0;
				int colonne=0;
				for (int k=-this.max(i,j); k<this.n-this.min(i,j);k++){
					ligne=i+k;
					colonne=j+k;
					System.out.print(k);
					System.out.print("("+ligne+","+colonne+") \n");
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							System.out.print(" Position : " +"("+ligne+","+colonne+") \n");
							tmp1.add(model.and(contrainteF,model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f)));
							//ContraintesF.add(tmp);
							//model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f).post();
						}
						//model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f).post():	
					}
				}
				if( !tmp1.isEmpty()){
					System.out.print("Size Tmp1 :"+tmp1.size());
					tmp.add(model.and(tmp1.toArray(new Constraint[]{})));
					
				}
				System.out.print("Size Tmp :"+tmp.size());
				
				//diagonale droite vers la gauche
				ligne=0;
				colonne=0;
				for (int k=-this.max(i,n-1-j); k<this.n-this.min(i,n-1-j);k++){
					ligne=i+k;
					colonne=n-1-j-k;
					if (((0<=ligne) && (ligne<this.n)) && ((0<=colonne) && (colonne<this.n))){
						if ((ligne!=i) || (colonne!=j)){
							//System.out.print(" Position : " +"("+ligne+","+colonne+") \n");
							//model.ifThen(model.and(c3,c4),model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f));
							tmp2.add(model.and(contrainteF,model.arithm(echequier[ligne][colonne], ">", this.t+this.c+this.f)));
						}
					}
				}
				if( !tmp2.isEmpty()){
					System.out.print("Size Tmp2 :"+tmp2.size());
					tmp.add(model.and(tmp2.toArray(new Constraint[]{})));
				}
				//System.out.print(i+ " "+ j+ "\n");
				System.out.print("Size Tmp :"+tmp.size());
				//Constraint test=model.and(tmp.toArray(new Constraint[]{}));
				if( !tmp.isEmpty()){
					//model.and(tmp.toArray(new Constraint[]{}));
					contrainteCase.add(model.and(tmp.toArray(new Constraint[]{})));
				}
				//ContraintesF.add(model.and(tmp.toArray(new Constraint[]{})));
				//ContraintesF.add(test);
				
				Constraint c5= model.arithm(echequier[i][j], ">=", this.t+this.f+1);
				Constraint c6= model.arithm(echequier[i][j], "<=", this.t+this.f+this.c);
				Constraint contrainteC=model.and(c5,c6);
				tmp.clear();
				if ((i+1<this.n) && (j+2<this.n)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i+1][j+2], ">", this.t+this.c+this.f));
					//model.arithm(echequier[i+1][j+2], ">", this.t+this.c+this.f).post();
					tmp.add(model.and(contrainteC,model.arithm(echequier[i+1][j+2], ">", this.t+this.c+this.f)));
					//ContraintesC.add(tmp);
				}
				if ((i+1<this.n) && (j-2>=0)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i+1][j-2], ">", this.t+this.c+this.f)); 
					//model.arithm(echequier[i+1][j-2], ">", this.t+this.c+this.f).post();
					tmp.add(model.and(contrainteC,model.arithm(echequier[i+1][j-2], ">", this.t+this.c+this.f)));
					//ContraintesC.add(tmp);
				}
				if ((i-1>=0) && (j+2<this.n)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i-1][j+2], ">", this.t+this.c+this.f)); vide
					//model.arithm(echequier[i-1][j+2], ">", this.t+this.c+this.f).post();
					tmp.add(model.and(contrainteC,model.arithm(echequier[i-1][j+2], ">", this.t+this.c+this.f)));
					//ContraintesC.add(tmp);
				}
				if ((i-1>=0) && (j-2>=0)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i-1][j-2], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i-1][j-2], ">", this.t+this.c+this.f).post();
					tmp.add(model.and(contrainteC,model.arithm(echequier[i-1][j-2], ">", this.t+this.c+this.f)));
					//ContraintesC.add(tmp);
				}
				if ((i+2<this.n) && (j+1<this.n)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i+2][j+1], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i+2][j+1], ">", this.t+this.c+this.f).post();
					tmp.add(model.and(contrainteC,model.arithm(echequier[i+2][j+1], ">", this.t+this.c+this.f)));
					
				}
				if ((i+2<this.n) && (j-1>=0)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i+2][j-1], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i+2][j-1], ">", this.t+this.c+this.f).post();
					tmp.add(model.and(contrainteC,model.arithm(echequier[i+2][j-1], ">", this.t+this.c+this.f)));
				}
				if ((i-2>=0) && (j+1<this.n)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i-2][j+1], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i-2][j+1], ">", this.t+this.c+this.f).post();
					tmp.add(model.and(contrainteC,model.arithm(echequier[i-2][j+1], ">", this.t+this.c+this.f)));
					//ContraintesC.add(tmp);
				}
				if ((i-2>=0) && (j-1>=0)){
					//model.ifThen(model.and(c5,c6),model.arithm(echequier[i-2][j-1], ">", this.t+this.c+this.f)); //vide
					//model.arithm(echequier[i-2][j-1], ">", this.t+this.c+this.f).post();
					tmp.add(model.and(contrainteC,model.arithm(echequier[i-2][j-1], ">", this.t+this.c+this.f)));
					
				}
				
				if (!tmp.isEmpty()){
					contrainteCase.add(model.and(tmp.toArray(new Constraint[]{})));
				}//model.eva
				
				//model.or(contrainteCase.toArray(new Constraint[]{})).post();
				
				contrainteCase.clear();
			}
			
		}
		//Constraint allUnique=model.and(ContraintesUnique.toArray(new Constraint[]{})); 
		/*Constraint allTour=model.or(ContraintesT.toArray(new Constraint[]{}));
		Constraint allFou=model.or(ContraintesF.toArray(new Constraint[]{}));
		Constraint allCavalier=model.or(ContraintesC.toArray(new Constraint[]{}));*/
		//Constraint allTour=model.or(ContraintesT.toArray(new Constraint[]{}));
		//Constraint allFou=model.or(ContraintesF.toArray(new Constraint[]{}));
		//Constraint allCavalier=model.or(ContraintesC.toArray(new Constraint[]{}));
		//model.and(allUnique).post();
		//model.and(allUnique,allTour,allFou,allCavalier).post();
		//Constraint res=model.or(ALLContraintes.toArray(new Constraint[]{}));
		//model.and(res,allUnique).post();
		//model.and(allFou).post();
		
		//model.and(ContraintesUnique.toArray(new Constraint[]{})).post();
		//model.or(ALLContraintes.toArray(new Constraint[]{})).post();;
		//Solution solution = model.getSolver().findSolution();
		Solver solver = model.getSolver();
		System.out.print("After Solve");
		if(solver.solve()){
			System.out.print("Not ready ye\n");
		    this.printingBoard(echequier);
			this.printNumber(echequier);
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
