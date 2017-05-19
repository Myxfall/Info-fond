package question4Choco;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;


public class MinCavalier {
	private int n;
	private Model model;
	private IntVar sum;
	private IntVar numCavalier;
	private IntVar[][] echequier;
	private int numMinCavalier;
	private String[][] Solution;
	
	public MinCavalier(int n){
		this.n=n;
		this.model= new Model("MinimisationCavalierDomination");
		this.sum=model.intVar("sum", 0, this.n*this.n);
		this.numCavalier = model.intVar("objective", 1, this.n*this.n);
		this.echequier = model.intVarMatrix("echequier",this.n,this.n,0,1);
		this.numMinCavalier=0;
		this.Solution=new String[this.n][this.n];
		
	}
	private void printingBoard(){
		String line;
		System.out.print(this.numMinCavalier+"\n");
		for (int i=0;i<this.n;i++){
			line="";
			for (int j=0;j<this.n;j++){
				line+=this.Solution[i][j]+" ";
			}				
			System.out.println(line);
		}
	}
	
	private void setSolution(){
		//String line;
		this.numMinCavalier=numCavalier.getValue();
		for (int i=0;i<this.n;i++){
			//line="";
			for (int j=0;j<this.n;j++){
				if (echequier[i][j].getValue() == 1){ //1 -> k1 Tour
					this.Solution[i][j]="C";
				}else{
					this.Solution[i][j]="*";
				}
			}				
		}
	}
	
	public Constraint menace(int ligne,int colonne){
		ArrayList<Constraint> cavalierAttaque=new ArrayList<Constraint>();
		
		//i+1 j+2
		if ((ligne+1<this.n) && (colonne+2<this.n)){
			Constraint menace=this.model.arithm(this.echequier[ligne+1][colonne+2], "!=", this.echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		//i+1 j-2
		if ((ligne+1<this.n) && (colonne-2>=0)){
			Constraint menace=this.model.arithm(this.echequier[ligne+1][colonne-2], "!=", this.echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		//i-1 j+2
		
		if ((ligne-1>=0) && (colonne+2<this.n)){
			Constraint menace=this.model.arithm(this.echequier[ligne-1][colonne+2], "!=", this.echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		if ((ligne-1>=0) && (colonne-2>=0)){
			//i-1 j-2
			Constraint menace=this.model.arithm(this.echequier[ligne-1][colonne-2], "!=", this.echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		if ((ligne+2<this.n) && (colonne+1<this.n)){
			//i+2 j+1
			Constraint menace=this.model.arithm(this.echequier[ligne+2][colonne+1], "!=", this.echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		if ((ligne+2<this.n) && (colonne-1>=0)){
			//i+2 j-1
			Constraint menace=this.model.arithm(this.echequier[ligne+2][colonne-1], "!=",this.echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		
		if ((ligne-2>=0) && (colonne+1<this.n)){
			//i-2 j+1
			Constraint menace=this.model.arithm(this.echequier[ligne-2][colonne+1], "!=",this.echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		if ((ligne-2>=0) && (colonne-1>=0)){
			//i-2 j-1
			Constraint menace=this.model.arithm(this.echequier[ligne-2][colonne-1], "!=", this.echequier[ligne][colonne]);
			cavalierAttaque.add(menace);
		}
		if (!cavalierAttaque.isEmpty()){
			return this.model.or(cavalierAttaque.toArray(new Constraint[]{}));
		}
		return null;
		
	}
	
	
	public void MinimizationCavalier(){
		for (int l=0;l<this.n;l++){
			for (int k=0;k<this.n;k++){
					if (this.menace(l,k)!=null){
						this.menace(l,k).post();
					}
					else{
						this.model.arithm(this.echequier[l][k], "=",1).post();
					}
					this.sum=this.sum.add(this.echequier[l][k]).intVar();
			}
			
		}
		this.numCavalier.eq(sum).post();
		this.model.setObjective(Model.MINIMIZE, this.numCavalier);
		Solver solver = model.getSolver();
		while(solver.solve()){
			this.setSolution();
		}
		//System.out.print(this.Solution[0][1]);
		this.printingBoard();
	}
}