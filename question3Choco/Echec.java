package question3Choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class Echec {
	String mode;
	
	
	public Echec(String[] args){
		this.mode=args[0];
		System.out.print(this.mode);
		
		
	}

	public void impedence(){
		int n = 4;
		Model model = new Model(n + "-independance problem");
		IntVar[] vars = new IntVar[n];
		for(int q = 0; q < n; q++){
		    vars[q] = model.intVar("T_"+q, 1, n); //contrainte tour, une tour par ligne
		}
		for(int i  = 0; i < n-1; i++){
		    for(int j = i + 1; j < n; j++){
		    	model.arithm(vars[i], "!=", vars[j]).post();
		    }
		}
		Solution solution = model.getSolver().findSolution();
		if(solution != null){
		    System.out.println(solution.toString());
		}
	}
	
}
