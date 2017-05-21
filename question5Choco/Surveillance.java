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
	private IntVar sum;
	private IntVar numCamera;
	
	public Surveillance(String filename){
		this.filename=filename;
		this.parseFile();
		this.model= new Model("MinimisationCavalierDomination");
		this.salle = model.intVarMatrix("salle",this.nbrLigne,this.nbrColonne,0,5);
		this.sum=model.intVar("sum", 0, this.nbrLigne*this.nbrColonne);
		this.numCamera = model.intVar("objective", 1,  this.nbrLigne*this.nbrColonne);
		
		
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
			ArrayList<Constraint> videEntre = new ArrayList<Constraint>();
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
			ArrayList<Constraint> videEntre = new ArrayList<Constraint>();
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
			ArrayList<Constraint> videEntre = new ArrayList<Constraint>();
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
			ArrayList<Constraint> videEntre = new ArrayList<Constraint>();
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
			//model.count(this.VIDE, this.salle[i], numV).post();
		
		}
		ArrayList<BoolVar> tab1dimension=new ArrayList<BoolVar>();
		for (int i=0;i<this.nbrLigne;i++){
			for (int j=0;j<this.nbrColonne;j++){
				tab1dimension.add(this.salle[i][j].eq(this.VIDE).boolVar());
			}
		}
		BoolVar[] tableau= new BoolVar[this.nbrColonne*this.nbrLigne];
		
		model.sum(tab1dimension.toArray(tableau),"=", numV).post();
		//this.numCamera.eq(this.sum).post();
		this.model.setObjective(Model.MAXIMIZE, numV);
		Solver solver = model.getSolver();
		while(solver.solve()){
			//this.setSolution();

			System.out.print(numV+"\n");

			this.printSalle();
			this.printBoard();

		}
		
	}
	public void mineCamera(){
		IntVar numN=model.intVar("sumN", 0, this.nbrColonne*this.nbrLigne);
		IntVar numS=model.intVar("sumS", 0, this.nbrColonne*this.nbrLigne);
		IntVar numE=model.intVar("sumE", 0, this.nbrColonne*this.nbrLigne);
		IntVar numO=model.intVar("sumO", 0, this.nbrColonne*this.nbrLigne);
		ArrayList<Constraint> Nord = new ArrayList<Constraint>();
		ArrayList<Constraint> Sud = new ArrayList<Constraint>();
		ArrayList<Constraint> Est = new ArrayList<Constraint>();
		ArrayList<Constraint> Ouest = new ArrayList<Constraint>();
		ArrayList<Constraint> contrainteTotal_OR = new ArrayList<Constraint>();
		ArrayList<Constraint> existCam_OR = new ArrayList<Constraint>();
		ArrayList<Integer> ligne_avant=new ArrayList<Integer>();
		ArrayList<Integer> colonne_avant=new ArrayList<Integer>();
		for (int l=0;l<this.nbrLigne;l++){
			for (int k=0;k<this.nbrColonne;k++){
				
				if (!this.grid[l][k].equals("*")){
					
					//vide and exist une camera
					Constraint contrainte_vide = model.arithm(this.salle[l][k], "=", 0);
					
					
					//camera S
					System.out.print("Cordone en jeu ("+l+","+k+")\n");
					int m = l;
					while(m >= 0){
					//for(int m=0; m<l; ++m){
						if (l!=m){
							if(!this.grid[m][k].equals("*")){
								if (ligne_avant.isEmpty() && colonne_avant.isEmpty()){
									ligne_avant.add(m);
									colonne_avant.add(k);
									Constraint contrainte_S = model.arithm(this.salle[m][k], "=", 1);
									Sud.add(contrainte_S);
									System.out.print("Cordone en jeu ("+l+","+k+")"+"entrain de traiter SuD SANS("+m+","+k+")\n");
								}
								else{
									Constraint contrainte_S = model.arithm(this.salle[m][k], "=", 1);
									ligne_avant.add(m);
									colonne_avant.add(k);
									for (int i=0;i<ligne_avant.size();i++){
										Constraint rienEntre =model.arithm(this.salle[ligne_avant.get(i)][colonne_avant.get(i)], "=", 0);
										Sud.add(model.and(rienEntre,contrainte_S));
										System.out.print("Cordone en jeu ("+l+","+k+")"+"entrain de traiter SuD AVEC("+m+","+k+") et ("+ligne_avant.get(i)+","+colonne_avant.get(i)+"\n");
									}
								}
								
								//System.out.print("Cordone en jeu ("+l+","+k+")"+"entrain de traiter SuD ("+m+","+k+")\n");
								//Constraint contrainte_S = model.arithm(this.salle[m][k], "=", 1);
								 
							}
							else{
								m = -100;
							}
						}
						--m;
					}
					ligne_avant.clear();
					colonne_avant.clear();
					//camera N
					m = l;
					while(m < this.nbrLigne){
					//for(int m=l; m<this.nbrLigne; ++m){
						if (l!=m){
							if(!this.grid[m][k].equals("*")){
								if (ligne_avant.isEmpty() && colonne_avant.isEmpty()){
									ligne_avant.add(m);
									colonne_avant.add(k);
									System.out.print("Cordone en jeu ("+l+","+k+")"+"entrain de traiter NORD ("+m+","+k+")\n");
									Constraint contrainte_N = model.arithm(this.salle[m][k], "=", 2);
									Nord.add(contrainte_N);
								}else{
									Constraint contrainte_N = model.arithm(this.salle[m][k], "=", 2);
									ligne_avant.add(m);
									colonne_avant.add(k);
									for (int i=0;i<ligne_avant.size();i++){
										Constraint rienEntre =model.arithm(this.salle[ligne_avant.get(i)][colonne_avant.get(i)], "=", 0);
										Nord.add(model.and(rienEntre,contrainte_N));
										System.out.print("Cordone en jeu ("+l+","+k+")"+"entrain de traiter SuD AVEC("+m+","+k+") et ("+ligne_avant.get(i)+","+colonne_avant.get(i)+"\n");
									}
								}
							}
							else{
								m = 100;
							}
						}
						++m;
					}
					ligne_avant.clear();
					colonne_avant.clear();
					//camera E: regarde à droite
					m = k;
					while(m >= 0){
					//for(int m=0; m<k; ++m){
						if (k!=m){
							if(!this.grid[l][m].equals("*")){
								if (ligne_avant.isEmpty() && colonne_avant.isEmpty()){
									ligne_avant.add(l);
									colonne_avant.add(m);
									System.out.print("Cordone en jeu ("+l+","+k+")"+"entrain de traiter Est ("+l+","+m+")\n");
									Constraint contrainte_E = model.arithm(this.salle[l][m], "=", 3);
									Est.add(contrainte_E);
								}
								else{
									Constraint contrainte_E = model.arithm(this.salle[l][m], "=", 3);
									ligne_avant.add(l);
									colonne_avant.add(m);
									for (int i=0;i<ligne_avant.size();i++){
										Constraint rienEntre =model.arithm(this.salle[ligne_avant.get(i)][colonne_avant.get(i)], "=", 0);
										Nord.add(model.and(rienEntre,contrainte_E));
										System.out.print("Cordone en jeu ("+l+","+k+")"+"entrain de traiter SuD AVEC("+l+","+m+") et ("+ligne_avant.get(i)+","+colonne_avant.get(i)+"\n");
									}
								}
							}
							else{
								m = -100;
							}
						}
						--m;
					}
					ligne_avant.clear();
					colonne_avant.clear();
					//camera O: regarde à gauche
					m = k;
					while(m < this.nbrColonne){
					//for(int m=k; m<this.nbrColonne; ++m){
						if (k!=m){
							if(!this.grid[l][m].equals("*")){
								if (ligne_avant.isEmpty() && colonne_avant.isEmpty()){
									ligne_avant.add(l);
									colonne_avant.add(m);
									System.out.print("Cordone en jeu ("+l+","+k+")"+"entrain de traiter Ouest SANS("+l+","+m+")\n");
									Constraint contrainte_O = model.arithm(this.salle[l][m], "=", 4);
									Ouest.add(contrainte_O);
								}else{
									Constraint contrainte_O = model.arithm(this.salle[l][m], "=", 4);
									ligne_avant.add(l);
									colonne_avant.add(m);
									for (int i=0;i<ligne_avant.size();i++){
										Constraint rienEntre =model.arithm(this.salle[ligne_avant.get(i)][colonne_avant.get(i)], "=", 0);
										Nord.add(model.and(rienEntre,contrainte_O));
										System.out.print("Cordone en jeu ("+l+","+k+")"+"entrain de traiter SuD AVEC("+l+","+m+") et ("+ligne_avant.get(i)+","+colonne_avant.get(i)+"\n");
									}
								}
							}
							else{
								m = 100;
							}
						}
						++m;
					}
					ligne_avant.clear();
					colonne_avant.clear();
					//contrainte vide and exists camera dans croix
					 //model.or(existCam_OR.toArray(new Constraint[]{})).post();;
					if (!Nord.isEmpty()){
						existCam_OR.add(model.or(Nord.toArray(new Constraint[]{})));
						Nord.clear();
					}
					if (!Sud.isEmpty()){
						existCam_OR.add(model.or(Sud.toArray(new Constraint[]{})));;
						Sud.clear();
					}
					if (!Est.isEmpty()){
						existCam_OR.add(model.or(Est.toArray(new Constraint[]{})));
						Est.clear();
					}
					if (!Ouest.isEmpty()){
						existCam_OR.add(model.or(Ouest.toArray(new Constraint[]{})));
						Ouest.clear();
					}
					//existCam_OR.clear();
					/*contrainteTotal_OR.add(videCamera_AND);
					contrainteTotal_OR.add(model.arithm(this.salle[l][k], "=", 1));
					contrainteTotal_OR.add(model.arithm(this.salle[l][k], "=", 2));
					contrainteTotal_OR.add(model.arithm(this.salle[l][k], "=", 3));
					contrainteTotal_OR.add(model.arithm(this.salle[l][k], "=", 4));
					/*if (!Nord.isEmpty()){
						contrainteTotal_OR.add(model.or(Nord.toArray(new Constraint[]{})));
					}
					if (!Sud.isEmpty()){
						contrainteTotal_OR.add(model.or(Sud.toArray(new Constraint[]{})));
					}
					if (!Est.isEmpty()){
						contrainteTotal_OR.add(model.or(Est.toArray(new Constraint[]{})));
					}
					if (!Ouest.isEmpty()){
						contrainteTotal_OR.add(model.or(Ouest.toArray(new Constraint[]{})));
					}
					model.and(contrainte_vide,model.or(contrainteTotal_OR.toArray(new Constraint[]{}))).post();*/
				}
				//sinon c'est un mur
				else{
					model.arithm(this.salle[l][k], "=", 5).post();
				}
				
				
			//if(!OR_contraintes.isEmpty()){
				//model.or(OR_contraintes.toArray(new Constraint[]{})).post();
				//OR_contraintes.clear();
			//}
			}
			model.or(existCam_OR.toArray(new Constraint[]{})).post();
			existCam_OR.clear();
			//model.and(contrainteTotal_OR.toArray(new Constraint[]{})).post();
			//contrainteTotal_OR.clear();
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