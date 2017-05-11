package question4Choco;

import java.util.ArrayList;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class MinCavalier {
	private int n;
	
	public MinCavalier(int n){
		this.n=n;
	}
	private void printingBoard(IntVar[][] echequier,IntVar numCavalier){
		String line;
		for (int i=0;i<this.n;i++){
			line="";
			for (int j=0;j<this.n;j++){
				//System.out.println(echequier[i][j]);
				if ((1 <= echequier[i][j].getLB()) && (echequier[i][j].getLB() <= numCavalier.getLB())){ //1 -> k1 Tour
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
	public void findMin(){
		Model model = new Model("-minimisation du nombre de cavalier");
		IntVar numCavalier = model.intVar("objective", 1, this.n*this.n);
		IntVar[][] echequier =model.intVarMatrix("echequier",this.n,this.n,1,this.n*this.n);
		//model.scalar(new IntVar[]{X,Y}, new int[]{3,4}, OBJ).post();
		// Specify objective
		ArrayList<Constraint> ContraintesUnique= new ArrayList<Constraint>();
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
				Constraint c1= model.arithm(echequier[i][j], ">", numCavalier);
				//ALLContraintes.add(c1);
				Constraint contrainte=null;
				System.out.print("\n Debut contrainte Cavalier: \n");
				if ((i+1<this.n) && (j+2<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i+1)+","+(j+2)+") \n");
					contrainte=model.arithm(echequier[i+1][j+2], "<", numCavalier);
					 ALLContraintes.add(contrainte);
					
				}
				if ((i+1<this.n) && (j-2>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i+1)+","+(j-2)+") \n");
					contrainte=model.arithm(echequier[i+1][j-2], "<", numCavalier);
					 ALLContraintes.add(contrainte);
				}
				if ((i-1>=0) && (j+2<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i-1)+","+(j+2)+") \n");
					contrainte=model.arithm(echequier[i-1][j+2], "<", numCavalier);
					 ALLContraintes.add(contrainte);
				}
				if ((i-1>=0) && (j-2>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i-1)+","+(j-2)+") \n");
					contrainte=model.arithm(echequier[i-1][j-2], "<", numCavalier);
					 ALLContraintes.add(contrainte);
				}
				if ((i+2<this.n) && (j+1<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i+2)+","+(j+1)+") \n");
					contrainte=model.arithm(echequier[i+2][j+1], "<", numCavalier);
					 ALLContraintes.add(contrainte);
					
				}
				if ((i+2<this.n) && (j-1>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i+2)+","+(j-1)+") \n");
					contrainte=model.arithm(echequier[i+2][j-1], "<", numCavalier);
					 ALLContraintes.add(contrainte);
				}
				if ((i-2>=0) && (j+1<this.n)){
					System.out.print("\n Emplacement Chevalier: ("+(i-2)+","+(j+1)+") \n");
					contrainte=model.arithm(echequier[i-2][j+1], "<", numCavalier);
					 ALLContraintes.add(contrainte);
				}
				if ((i-2>=0) && (j-1>=0)){
					System.out.print("\n Emplacement Chevalier: ("+(i-2)+","+(j-1)+") \n");
					contrainte=model.arithm(echequier[i-2][j-1], "<", numCavalier);
					 ALLContraintes.add(contrainte);
					
				}
				
				//ALLContraintes.add(model.and(ContrainteC.toArray(new Constraint[]{})));
				
				ALLColonnes.add(model.and(c1,model.or(ALLContraintes.toArray(new Constraint[]{}))));
				//ALLContraintes.clear();
				
				
				
			}		
			model.or(ALLColonnes.toArray(new Constraint[]{})).post();
			ALLColonnes.clear();
			
			
		}
		//model.or(ALLContraintes.toArray(new Constraint[]{})).post();
		model.and(ContraintesUnique.toArray(new Constraint[]{})).post();
		ContraintesUnique.clear();
		model.setObjective(Model.MINIMIZE, numCavalier);
		Solver solver = model.getSolver();
		if(solver.solve()){
			this.printingBoard(echequier,numCavalier);
			this.printNumber(echequier);
		}else {
		    System.out.println("The solver has proved the problem has no solution");
		}
	}
}
