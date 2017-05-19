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
	private IntVar sum;
	private IntVar numCamera;
	
	public Surveillance(String filename){
		this.filename=filename;
		this.parseFile();
		this.model= new Model("MinimisationCavalierDomination");
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
				System.out.print(this.grid[i][j]);
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
					//System.out.print(i+" "+j+" "+this.grid[i][j]+"\n");
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
	
	public Constraint cameraNordSud(int ligne, int colonne){
		ArrayList<Constraint> cameraNordSud=new ArrayList<Constraint>();
		//System.out.print(ligne +" "+colonne+"\n");
		for (int k=0;k<this.dimensionY;k++){
			if (ligne!=k){
				//System.out.print(k);
				if (ligne<k){
					Constraint voit=this.model.arithm(this.salle[k][colonne], "=", 2); //SUD
					cameraNordSud.add(voit);
				}
				else if (ligne>k){
					Constraint voit=this.model.arithm(this.salle[k][colonne], "=", 1); //NORD
					cameraNordSud.add(voit);
				}
			}
		}
		return model.or(cameraNordSud.toArray(new Constraint[]{}));
	}
	
	public Constraint cameraEstOuest(int ligne, int colonne){
		ArrayList<Constraint> cameraEstOuest=new ArrayList<Constraint>();
		for (int k=0;k<this.dimensionY;k++){
			if (colonne!=k){
				if (colonne<k){
					Constraint voit=this.model.arithm(this.salle[ligne][k], "=", 3); //EST
					cameraEstOuest.add(voit);
				}
				else if(colonne>k){
					Constraint voit=this.model.arithm(this.salle[ligne][k], "=", 4); //Ouest
					cameraEstOuest.add(voit);
				}
			}
		}
		return model.or(cameraEstOuest.toArray(new Constraint[]{}));
	}
	
	public void minCamera(){
		ArrayList<Constraint> OR_contraintes = new ArrayList<Constraint>();
		for (int l=0;l<this.dimensionY;l++){
			for (int k=0;k<this.dimensionX;k++){
				if (this.grid[l][k]!="*"){
					OR_contraintes.add(this.cameraNordSud(l, k));
					OR_contraintes.add(this.cameraEstOuest(l,k));
				}
			}
			if(!OR_contraintes.isEmpty()){
				model.or(OR_contraintes.toArray(new Constraint[]{})).post();
				OR_contraintes.clear();
			}
		}
		System.out.print("\n"+model.getNbIntVar(false)+"\n");
		//this.numCamera.eq(model.getNbIntVar(false)).post();
		this.model.setObjective(Model.MINIMIZE, this.numCamera);
		Solver solver = model.getSolver();
		while(solver.solve()){
			//this.setSolution();
			this.printSalle();
		}
		//System.out.print(this.Solution[0][1]);
		//this.printingBoard();
	}
		
}