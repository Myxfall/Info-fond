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

import allPiece.Cavalier;
import allPiece.Fou;
import allPiece.Piece;
import allPiece.Tour;
import allPiece.Vide;

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
		this.salle = model.intVarMatrix("salle",this.dimensionY,this.dimensionX,0,5);
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
				if (this.salle[i][j].getValue()==1){
					line+="N";
				}else if (this.salle[i][j].getValue()==2){
					line+="S";
				}else if (this.salle[i][j].getValue()==3){
					line+="E";
				}else if (this.salle[i][j].getValue()==4){
					line+="O";
				}else if (this.salle[i][j].getValue()==5){
					line+="*";
				}else{
					line+=" ";
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
				//System.out.println(l + " : " + k);
				/*if (!this.grid[l][k].equals("*")){
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
					model.arithm(this.salle[l][k], "=", 5).post();
				}*/
				//model.arithm(this.salleMin[l][k],">=",this.salle[l][k]);
				//this.sum=this.sum.add(this.salle[l][k]).intVar();

				//si case vide
				if (!this.grid[l][k].equals("*")){
					ArrayList<Constraint> contrainteTotal_OR = new ArrayList<Constraint>();
					
					//vide and exist une camera
					Constraint contrainte_vide = model.arithm(this.salle[l][k], "=", 0);
					
					ArrayList<Constraint> existCam_OR = new ArrayList<Constraint>();
					//camera S
					int m = l;
					while(m > 0){
					//for(int m=0; m<l; ++m){
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
					while(m < this.dimensionY){
					//for(int m=l; m<this.dimensionY; ++m){
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
					//for(int m=0; m<k; ++m){
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
					while(m < this.dimensionX){
					//for(int m=k; m<this.dimensionX; ++m){
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
				//sinon c'est un mur
				else {
					model.arithm(this.salle[l][k], "=", 5).post();
				}
				
			}
			
			//if(!OR_contraintes.isEmpty()){
				//model.or(OR_contraintes.toArray(new Constraint[]{})).post();
				//OR_contraintes.clear();
			//}
			
			model.count(1, this.salle[l], numN).post();
			model.count(2, this.salle[l],numS).post();
			model.count(3, this.salle[l], numE).post();
			model.count(4, this.salle[l], numO).post();
			this.sum=this.sum.add(numN).intVar();
			this.sum=this.sum.add(numS).intVar();
			this.sum=this.sum.add(numE).intVar();
			this.sum=this.sum.add(numO).intVar();
			//model.sum(new IntVar[]{numN,numS,numE,numO},"+",this.sum);
		}
		
		this.numCamera.eq(this.sum).post();
		this.model.setObjective(Model.MINIMIZE, this.numCamera);
		Solver solver = model.getSolver();
		while(solver.solve()){
			//this.setSolution();
			System.out.print(this.sum+"\n");
			System.out.print(numN+"\n");
			System.out.print(numS+"\n");
			System.out.print(numO+"\n");
			System.out.print(numE+"\n");
			this.printSalle();

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