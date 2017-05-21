/**
 * Author Rusu George, Romain Maximilien
 */

package question5Choco;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;


public class Surveillance {
	
	private int VIDE=0;
	private int NORD=1;
	private int SUD=2;
	private int EST=3;
	private int OUEST=4;
	private int MUR=5;
	private String[][] grid;
	private String filename;
	private int nbrColonne;
	private int nbrLigne;
	private Model model;
	private IntVar[][] salle;
	private int numVide;
	private String[][] solution;
	private ArrayList<Constraint> videEntre;
	
	public Surveillance(String filename){
		this.filename=filename;
		this.parseFile();
		this.model= new Model("MinimisationCavalierDomination");
		this.salle = model.intVarMatrix("salle",this.nbrLigne,this.nbrColonne,0,5);
		this.solution=new String[this.nbrLigne+2][this.nbrColonne+2];
		this.videEntre = new ArrayList<Constraint>();
		
		
	}

	public void numberOfCharPerLine() throws FileNotFoundException{
		BufferedReader bR= new BufferedReader(new FileReader("./question5Choco/"+this.filename));
		String currentLine;
		try {
			currentLine = bR.readLine();
			this.nbrColonne=currentLine.length()-2;
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
			this.nbrLigne=count-2;
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
			this.grid=new String[this.nbrLigne][this.nbrColonne];
			int count=0;
			while (((currentLine = bR.readLine()) != null) && (count<this.nbrLigne)) {
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
		for (int i=0;i<this.nbrLigne;i++){
			for (int j=0;j<this.nbrColonne;j++){
				System.out.print(this.salle[i][j]);
			}
			System.out.print("\n");
		}
	}
	
	public void printSalle(){
		String line="";
		for (int i=0;i<this.nbrColonne+2;i++){
			line+="*";
		}
		System.out.println(line);
		//this.printBoard();
		for (int i=0;i<this.nbrLigne;i++){
			line="*";
			for (int j=0;j<this.nbrColonne;j++){
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
				}
				else{
					line+="*";
				}
			}
			line+="*\n";
			System.out.print(line);
		}
		line="";
		for (int i=0;i<this.nbrColonne+2;i++){
			line+="*";
		}
		System.out.println(line);
	}
	
	
	public Constraint cameraNord(int ligne, int colonne){
		ArrayList<Constraint> cameraNord=new ArrayList<Constraint>();
		for (int l=ligne; l<this.nbrLigne;l++){
			this.videEntre.clear();
			for (int k=ligne; k<l ;k++){
				videEntre.add(model.arithm(this.salle[k][colonne], "=", this.VIDE));
			}
			videEntre.add(model.arithm(this.salle[l][colonne], "=", this.NORD));
			cameraNord.add(model.and(videEntre.toArray(new Constraint[]{})));
		}
		if (!cameraNord.isEmpty()){
			return model.or(cameraNord.toArray(new Constraint[]{}));
		}
		return model.falseConstraint();
	}
	
	public Constraint cameraSud(int ligne, int colonne){
		ArrayList<Constraint> cameraSud=new ArrayList<Constraint>();
		for (int l=ligne; l>0;l--){
			this.videEntre.clear();
			for (int k=ligne; k>l ;k--){
				videEntre.add(model.arithm(this.salle[k][colonne], "=", this.VIDE));
			}
			videEntre.add(model.arithm(this.salle[l][colonne], "=", this.SUD));
			cameraSud.add(model.and(videEntre.toArray(new Constraint[]{})));
		}
		if (!cameraSud.isEmpty()){
			return model.or(cameraSud.toArray(new Constraint[]{}));
		}
		return model.falseConstraint();
	}
	public Constraint cameraEst(int ligne, int colonne){
		ArrayList<Constraint> cameraEst=new ArrayList<Constraint>();
		for (int l=colonne; l>0;l--){
			this.videEntre.clear();
			for (int k=colonne; k>l ;k--){
				videEntre.add(model.arithm(this.salle[ligne][k], "=", this.VIDE));
			}
			videEntre.add(model.arithm(this.salle[ligne][l], "=", this.EST));
			cameraEst.add(model.and(videEntre.toArray(new Constraint[]{})));
		}
		if (!cameraEst.isEmpty()){
			return model.or(cameraEst.toArray(new Constraint[]{}));
		}
		return model.falseConstraint();
	}
	
	public Constraint cameraOuest(int ligne, int colonne){
		ArrayList<Constraint> cameraOuest=new ArrayList<Constraint>();
		for (int l=colonne; l<this.nbrColonne;l++){
			this.videEntre.clear();
			for (int k=colonne; k<l ;k++){
				videEntre.add(model.arithm(this.salle[ligne][k], "=", this.VIDE));
			}
			videEntre.add(model.arithm(this.salle[ligne][l], "=", this.OUEST));
			cameraOuest.add(model.and(videEntre.toArray(new Constraint[]{})));
		}
		if (!cameraOuest.isEmpty()){
			return model.or(cameraOuest.toArray(new Constraint[]{}));
		}
		return model.falseConstraint();
	}
	
	public void setSolution(IntVar numV){
		this.numVide=0;
		for (int i=0;i<this.nbrColonne+2;i++){
			this.solution[0][i]="*";
		}
		for (int i=0;i<this.nbrLigne+2;i++){
			this.solution[i][0]="*";
			this.solution[i][this.nbrColonne+1]="*";
		}
		for (int i=0;i<this.nbrLigne;i++){
			for (int j=0;j<this.nbrColonne;j++){
				if (this.salle[i][j].getValue()==this.NORD){
					this.solution[i+1][j+1]="N";
					this.numVide++;
				}
				else if (this.salle[i][j].getValue()==this.SUD){
					this.solution[i+1][j+1]="S";
					this.numVide++;
				}
				else if  (this.salle[i][j].getValue()==this.EST){
					this.solution[i+1][j+1]="E";
					this.numVide++;
				}
				else if  (this.salle[i][j].getValue()==this.OUEST){
					this.solution[i+1][j+1]="O";
					this.numVide++;
				}
				else if (this.salle[i][j].getValue()==this.MUR){
					this.solution[i+1][j+1]="*";
				}
				else{
					this.solution[i+1][j+1]=" ";
				}
			}
		}
		
		for (int i=0;i<this.nbrColonne+2;i++){
			this.solution[this.nbrLigne+1][i]="*";
		}
		
	}
	
	public void printSolution(){
		System.out.print(this.numVide+"\n");
		String line;
		for (int i=0;i<this.nbrLigne+2;i++){
			line="";
			for (int j=0;j<this.nbrColonne+2;j++){
				line+=this.solution[i][j]+" ";
			}				
			System.out.println(line);
		}
		
	}
	public void minCamera(){
		IntVar numV=model.intVar("sumVide", 0, this.nbrColonne*this.nbrLigne);
		
		for (int l=0;l<this.nbrLigne;l++){
			for (int k=0;k<this.nbrColonne;k++){
				if (!this.grid[l][k].equals("*")){
					this.salle[l][k].ne(this.MUR).post();
					
				
				}else{
					this.salle[l][k].eq(this.MUR).post();
				}
			}
		}
		for (int i=0;i<this.nbrLigne;i++){
			for (int j=0;j<this.nbrColonne;j++){
				ArrayList<Constraint> existCamera=new ArrayList<Constraint>();
				existCamera.add(this.cameraNord(i, j));
				existCamera.add(this.cameraSud(i, j));
				existCamera.add(this.cameraEst(i,j));
				existCamera.add(this.cameraOuest(i, j));
				model.ifThen(model.arithm(this.salle[i][j],"=", this.VIDE), model.or(existCamera.toArray(new Constraint[]{})));
			}
		
		}
		ArrayList<BoolVar> tab1dimension=new ArrayList<BoolVar>();
		for (int i=0;i<this.nbrLigne;i++){
			for (int j=0;j<this.nbrColonne;j++){
				tab1dimension.add(this.salle[i][j].eq(this.VIDE).boolVar());
			}
		}
		BoolVar[] tableau= new BoolVar[this.nbrColonne*this.nbrLigne];
		
		model.sum(tab1dimension.toArray(tableau),"=", numV).post();
		this.model.setObjective(Model.MAXIMIZE, numV);
		Solver solver = model.getSolver();
		while(solver.solve()){
			this.setSolution(numV);
		}
		this.printSolution();
	}
		
}