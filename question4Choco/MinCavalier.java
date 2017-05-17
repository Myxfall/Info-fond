package question4Choco;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import allPiece.Piece;



public class MinCavalier {
	private int n;
	
	public MinCavalier(int n){
		this.n=n;
	}
	
	public void MinimizationCavalier(){
		Model model=new Model("Minimisation-Cavalier");
		IntVar numCavalier=model.intVar("nombre-Cavalier",0,this.n*this.n);
		ArrayList<Case> allCase=new ArrayList<Case>();
		ArrayList<Constraint> OR_contraintes = new ArrayList<Constraint>();
		
		for (int i=0;i<this.n*this.n;i++){
			Case position=new Case(model,this.n);
			allCase.add(position);
		}
		
		for (int l=0;l<this.n*this.n;l++){
			for (int k=0;k<this.n*this.n;k++){
				//System.out.print(l+" et "+k+"\n");
				if (l!=k){
					//System.out.print(allCase.get(l).getType()+ " et "+allCase.get(k).getType()+"\n");
					Case positionAttaque=allCase.get(l);
					Case positionSubit=allCase.get(k);
					
					Constraint unique=positionAttaque.unique(positionSubit);
					unique.post();
					System.out.print(positionAttaque.getType()+"dehors "+positionSubit.getType().getValue()+"\n");
					if ((positionAttaque.getType().getValue()==1) && (positionSubit.getType().getValue()==0)){
						//System.out.print(allPiece.get(l).getType()+ " et "+allPiece.get(k).getType()+"\n");
						System.out.print(positionAttaque.getType().getValue()+"attaque "+positionSubit.getType().getValue()+"\n");
						//Constraint attaque= pieceAttaque.Menace(pieceSubit);;
						
						Constraint attaque = positionAttaque.Menace(positionSubit);
						OR_contraintes.add(attaque);
					}
						//OR_contraintes.add(attaque);
						//attaque.post();
					
				}
			}
			if(!OR_contraintes.isEmpty()){
				model.or(OR_contraintes.toArray(new Constraint[]{})).post();
				OR_contraintes.clear();
			}
		}
		IntVar tmp=model.intVar("tmp1",0,16);
		for (Case position : allCase){
			tmp.add(position.getType().intVar());
		}
		numCavalier.eq(tmp).post();
		model.setObjective(Model.MINIMIZE, numCavalier);
		Solver solver = model.getSolver();
		while(solver.solve());
		if (solver.solve()){
			this.printingBoard(allCase);
		}
	}
	public void printingBoard(ArrayList<Case> allCase){
		int[][] printingBoard=new int[this.n][this.n];
		for (int i=0;i<allCase.size();i++){
			//System.out.print(allPiece.get(i).getCoordLigne().getValue()+" ,"+allPiece.get(i).getCoordLigne().getValue()+" ,"+allPiece.get(i).getType());
			printingBoard[allCase.get(i).getCoordLigne().getValue()][allCase.get(i).getCoordColonne().getValue()]=allCase.get(i).getType().getValue();
		}
		String line;
		for (int i=0;i<this.n;i++){
			line="";
			for (int j=0;j<this.n;j++){
					if (printingBoard[i][j]==1){
						line+="C ";
					}else{
						line+="V ";
					}
				
				//printingBoard[i][j]="test";
			}
			System.out.print(line+"\n");
		}
		
	}

}
