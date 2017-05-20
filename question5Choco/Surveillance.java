package question5Choco;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class Surveillance {
	
	private String[][] grid;
	private String filename;
	private int dimensionX;
	private int dimensionY;
	private Model model;
	private IntVar[][] salle;
	private IntVar[][] salleMin;
	private IntVar sum;
	private IntVar numCamera;
	
	public Surveillance(String filename){
		this.filename=filename;
		this.parseFile();
		this.model= new Model("MinimisationCavalierDomination");
		this.salleMin = model.intVarMatrix("salleMin",this.dimensionY,this.dimensionX,0,4);
		this.salle = model.intVarMatrix("salle",this.dimensionY,this.dimensionX,0,4);
		this.sum=model.intVar("sum", 0, this.dimensionX*this.dimensionY);
		this.numCamera = model.intVar("objective", 1, this.dimensionX*this.dimensionY);
		
		
	}

	public void numberOfCharPerLine() throws FileNotFoundException{
		BufferedReader bR= new BufferedReader(new FileReader("./question5Choco/"+this.filename));
		String currentLine;
		try {
			currentLine = bR.readLine();
			this.dimensionX=currentLine.length()-2;
			bR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void numberOfLines() throws FileNotFoundException{
		BufferedReader bR= new BufferedReader(new FileReader("./question5Choco/"+this.filename));
		int count=0;
		String currentLine;
		try {
			while ((currentLine = bR.readLine()) != null) {
				count++;
			}
			this.dimensionY=count-2;
			bR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private void parseFile(){
		System.out.println("Parsing File: "+this.filename);
		String currentLine;
		try {
			this.numberOfCharPerLine();
			this.numberOfLines();
			BufferedReader bR= new BufferedReader(new FileReader("./question5Choco/"+this.filename));
			currentLine=bR.readLine();
			this.grid=new String[this.dimensionY][this.dimensionX];
			int count=0;
			while (((currentLine = bR.readLine()) != null) && (count<this.dimensionY)) {
				for (int i=1;i<currentLine.length()-1;i++){
					this.grid[count][i-1]=Character.toString(currentLine.charAt(i));
				}
				count++;
			}
			bR.close();
		}catch (IOException e) {
			
				e.printStackTrace();
		}
	}
	
	public void printBoard(){
		for (int i=0;i<this.dimensionY;i++){
			for (int j=0;j<this.dimensionX;j++){
				System.out.print(this.salle[i][j]);
			}
			System.out.print("\n");
		}
	}
	
	public void printSalle(){
		String line="";
		for (int i=0;i<this.dimensionX+2;i++){
			line+="*";
		}
		System.out.println(line);
		//this.printBoard();
		for (int i=0;i<this.dimensionY;i++){
			line="*";
			for (int j=0;j<this.dimensionX;j++){
				if (!this.grid[i][j].equals("*")){
					if (this.salle[i][j].getValue()==1){
						line+="N";
					}else if (this.salle[i][j].getValue()==2){
						line+="S";
					}else if (this.salle[i][j].getValue()==3){
						line+="E";
					}else if (this.salle[i][j].getValue()==4){
						line+="O";
					}else{
						line+=" ";
					}
				}else{
					line+="*";
				}
			}
			line+="*\n";
			System.out.print(line);
		}
		line="";
		for (int i=0;i<this.dimensionX+2;i++){
			line+="*";
		}
		System.out.println(line);
	}
	
	public Constraint cameraNord(int ligne, int colonne){
		ArrayList<Constraint> cameraNord=new ArrayList<Constraint>();
		for (int k=0;k<this.dimensionY;k++){
			if (ligne!=k){
				//System.out.print(k);
				if (ligne<k){
					Constraint voit=this.model.arithm(this.salle[k][colonne], "=", 1); //Nord
					cameraNord.add(voit);
				}
			}
		}
		if (!cameraNord.isEmpty()){
			return model.or(cameraNord.toArray(new Constraint[]{}));
		}
		return null;
	}
	public Constraint cameraSud(int ligne, int colonne){
		ArrayList<Constraint> cameraSud=new ArrayList<Constraint>();
		for (int k=0;k<this.dimensionY;k++){
			if (ligne!=k){
				if (ligne>k){
					Constraint voit=this.model.arithm(this.salle[k][colonne], "=", 2); //Sud
					cameraSud.add(voit);
				}
			}
		}
		if (!cameraSud.isEmpty()){
			return model.or(cameraSud.toArray(new Constraint[]{}));
		}
		return null;
	}
	public Constraint cameraEst(int ligne, int colonne){
		ArrayList<Constraint> cameraEst=new ArrayList<Constraint>();
		for (int k=0;k<this.dimensionY;k++){
			if (colonne!=k){
				if(colonne>k){
					Constraint voit=this.model.arithm(this.salle[ligne][k], "=", 3); //Est
					cameraEst.add(voit);
				}
			}
		}
		if (!cameraEst.isEmpty()){
			return model.or(cameraEst.toArray(new Constraint[]{}));
		}
		return null;
	}
	public Constraint cameraOuest(int ligne, int colonne){
		ArrayList<Constraint> cameraOuest=new ArrayList<Constraint>();
		for (int k=0;k<this.dimensionY;k++){
			if (colonne!=k){
				if (colonne<k){
					Constraint voit=this.model.arithm(this.salle[ligne][k], "=", 4); //Ouest
					cameraOuest.add(voit);
				}
			}
		}
		if (!cameraOuest.isEmpty()){
			return model.or(cameraOuest.toArray(new Constraint[]{}));
		}
		return null;
	}
	
	public void minCamera(){
		IntVar numN=model.intVar("sumN", 0, this.dimensionX*this.dimensionY);
		IntVar numS=model.intVar("sumS", 0, this.dimensionX*this.dimensionY);
		IntVar numE=model.intVar("sumE", 0, this.dimensionX*this.dimensionY);
		IntVar numO=model.intVar("sumO", 0, this.dimensionX*this.dimensionY);
		ArrayList<Constraint> OR_contraintes = new ArrayList<Constraint>();
		ArrayList<Constraint> boolSum = new ArrayList<Constraint>();
		for (int l=0;l<this.dimensionY;l++){
			for (int k=0;k<this.dimensionX;k++){
				if (!this.grid[l][k].equals("*")){
					if (this.cameraNord(l, k)!=null){
						OR_contraintes.add(this.cameraNord(l, k));
					}
					if (this.cameraSud(l, k)!=null){
						OR_contraintes.add(this.cameraSud(l, k));
					}
					if (this.cameraEst(l, k)!=null){
						OR_contraintes.add(this.cameraEst(l,k));
					}
					if (this.cameraOuest(l, k)!=null){
						OR_contraintes.add(this.cameraOuest(l,k));
					}
				}else{
					model.arithm(this.salle[l][k], "=", 0).post();
				}
				//model.arithm(this.salleMin[l][k],">=",this.salle[l][k]);
				//this.sum=this.sum.add(this.salle[l][k]).intVar();
			}
			
			if(!OR_contraintes.isEmpty()){
				model.or(OR_contraintes.toArray(new Constraint[]{})).post();
				OR_contraintes.clear();
			}
			
			boolSum.add(model.count(1, this.salle[l], numN));
			boolSum.add(model.count(2, this.salle[l],numS));
			boolSum.add(model.count(3, this.salle[l], numE));
			boolSum.add(model.count(4, this.salle[l], numO));
			model.sum(new IntVar[]{numN,numS,numE,numO},"+",this.sum);
		}
		
		//this.numCamera.eq(sum).post();
		this.model.setObjective(Model.MINIMIZE, this.numCamera);
		Solver solver = model.getSolver();
		while(solver.solve()){
			//this.setSolution();
			System.out.print(numN+"\n");
			System.out.print(numS+"\n");
			System.out.print(numE+"\n");
			System.out.print(numO+"\n");
			System.out.print(this.sum+"\n");
			this.printSalle();
			this.printBoard();
		}
		//System.out.print(this.Solution[0][1]);
		//this.printingBoard();

		/*Solver solver = model.getSolver();
		if(solver.solve()){
		    this.printSalle();
		}else {
		    System.out.println("Pas de solution trouvée.");
		}*/
	}
		
}