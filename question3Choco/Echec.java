package question3Choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
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
	private int calcT(IntVar[][] echequier){
		int count=0;
		for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				if (echequier[i][j].contains(1)){
					count+=1;
				}
			}
		}
		return count;
	}
	
	private void printingBoard(IntVar[][] echequier){
		String line;
		for (int i=0;i<this.n;i++){
			line="";
			for (int j=0;j<this.n;j++){
				if (echequier[i][j].contains(0)){
					line+="*";
				}else if (echequier[i][j].contains(1)){
					line+="T";
				}else if (echequier[i][j].contains(2)){
					line+="F";
				}else if (echequier[i][j].contains(2)){
					line+="C";
				}
				line+="  ";
			}
			System.out.println(line);
		}
	}

	public void independance(){
		Model model = new Model(this.n + "-independance problem");
		IntVar[][] echequier =model.intVarMatrix("echequier",this.n,this.n,0,3); //0=Vide 1=tour 2=fou 3=cavalier
		for (int i=0;i<this.n;i++){
			for (int j=0;j<this.n;j++){
				for (int l=i+1; l<this.n;l++){
					//contraintes pour la tour
					model.ifThen(model.arithm(echequier[i][j],"=", 1), model.arithm(echequier[i][l], "=", 0));
					model.ifThen(model.arithm(echequier[i][j],"=", 1), model.arithm(echequier[l][i], "=", 0));
					model.arithm(model.intVar(this.calcT(echequier)), "<=", this.t);
					
					//contraintes pour le cavalier
					
				}
			}
			
		}
		
		
		//Solution solution = model.getSolver().findSolution();
		Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printingBoard(echequier);
		}else {
		    System.out.println("The solver has proved the problem has no solution");
		}
		
	}
	public void domination(){
		
	}
	
}
