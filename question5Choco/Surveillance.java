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
	
	/**
	 * Constructor
	 * @param filename fichier a parser
	 */
	public Surveillance(String filename){
		this.filename=filename;
		this.parseFile();
		this.model= new Model("MinimisationCavalierDomination");
		this.salle = model.intVarMatrix("salle",this.nbrLigne,this.nbrColonne,0,5);
		this.solution=new String[this.nbrLigne+2][this.nbrColonne+2];
		
		
	}
 
	/**
	 * methode qui permet d'avoir le nombre de colonnes depuis la grille du fichier
	 * @throws FileNotFoundException
	 */
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
	
	/**
	 * methode qui permet d'avoir le nombre de lignes depuis la grille du fichier
	 * @throws FileNotFoundException
	 */
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
	/**
	 * methode qui va parser le fichier
	 */
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
	
	/**
	 * methode qui permet d'avoir la derniere solution
	 * @param numV le nombre de case Vide
	 */
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
	/**
	 * methode qui permet d'afficher la solution
	 */
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
	
	/**
	 * methode qui permet de minimiser le nombre de camera utilisé
	 */
	public void minCamera(){
		IntVar numV=model.intVar("sumVide", 0, this.nbrColonne*this.nbrLigne);
		ArrayList<Constraint> contrainteTotal_OR = new ArrayList<Constraint>();
		ArrayList<Constraint> existCam_OR = new ArrayList<Constraint>();
		for (int l=0;l<this.nbrLigne;l++){
			for (int k=0;k<this.nbrColonne;k++){
				
			//si case vide
			if (!this.grid[l][k].equals("*")){
				contrainteTotal_OR = new ArrayList<Constraint>();
				
				//vide and exist une camera
				Constraint contrainte_vide = model.arithm(this.salle[l][k], "=", 0);
				
				existCam_OR = new ArrayList<Constraint>();
				//camera S
				int m = l;
				while(m > 0){
					if(!this.grid[m][k].equals("*")){
						Constraint contrainte_S = model.arithm(this.salle[m][k], "=", 2);
						existCam_OR.add(contrainte_S);
					}
					else{
						m = -100;
					}
					--m;
				}
				//camera N
				m = l;
				while(m < this.nbrLigne){
					if(!this.grid[m][k].equals("*")){
						Constraint contrainte_N = model.arithm(this.salle[m][k], "=", 1);
						existCam_OR.add(contrainte_N);
					}
					else{
						m = 100;
					}
					++m;
				}
				//camera E: regarde à droite
				m = k;
				while(m > 0){
				if(!this.grid[l][m].equals("*")){
						Constraint contrainte_E = model.arithm(this.salle[l][m], "=", 3);
						existCam_OR.add(contrainte_E);
					}
					else{
						m = -100;
					}
					
					--m;
				}
				//camera O: regarde à gauche
				m = k;
				while(m < this.nbrColonne){
					if(!this.grid[l][m].equals("*")){
						Constraint contrainte_O = model.arithm(this.salle[l][m], "=", 4);
						existCam_OR.add(contrainte_O);
					}
					else{
						m = 100;
					}
					++m;
				}
				
				//contrainte vide and exists camera dans croix
				Constraint videCamera_AND = model.and(contrainte_vide, model.or(existCam_OR.toArray(new Constraint[]{})));
				
				contrainteTotal_OR.add(videCamera_AND);
				contrainteTotal_OR.add(model.arithm(this.salle[l][k], "=", 1));
				contrainteTotal_OR.add(model.arithm(this.salle[l][k], "=", 2));
				contrainteTotal_OR.add(model.arithm(this.salle[l][k], "=", 3));
				contrainteTotal_OR.add(model.arithm(this.salle[l][k], "=", 4));
				
				model.or(contrainteTotal_OR.toArray(new Constraint[]{})).post();
			}
				else {
					model.arithm(this.salle[l][k], "=", 5).post();
				}
			
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