package info.fond.project.choco.echiquier;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;
 
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;
 
public class SurveillanceMuseeV2 {
   
    // Types de cases autorisées dans une sale de musée.
    private static final int NORD  = 0;
    private static final int SUD   = 1;
    private static final int EST   = 2;
    private static final int OUEST = 3;
    private static final int MUR   = 4;
    private static final int VIDE  = 5;
    private static final int SURVEILLEE = 6;
    private static final int[]  valeurs       = {NORD, SUD, EST, OUEST, MUR, VIDE, SURVEILLEE};
   
    private static final char[] valeursString = {'N', 'S', 'E', 'O', '*', ' ', 'x'};
 
    // Toutes les case. x_ij où i représente la ligne, j la colonne.
    private static IntVar[][] x_ij;
    private static IntVar nbSurveillee;
    private static int H, L;
    private static Model model = new Model("Surveillance de musée");
 
    public static void main(String[] args) throws ContradictionException, IOException
    {
        initialiserMusee("test");
        creerContraintes();
        resoudre();
    }
   
    private static void creerContraintes() throws ContradictionException
    {
        // Le plateau ne doit avoir aucune case vide (ni occupée, ni menacée).
        IntVar[] x_ij1D = model.intVarArray(H * L, valeurs);
        int idx = 0;
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < L; j++) {
                x_ij1D[idx++] = x_ij[i][j];
            }
        }
        // Il ne doit pas y avoir de cases vides.
        IntVar nbCasesVide = model.intVar(0, H * L);
        model.count(VIDE, x_ij1D, nbCasesVide).post();
        nbCasesVide.eq(0).post();
       
        // On compte le nombre de cases qui sont surveillées (sera utilisé pour demandé à être maximisé).
        nbSurveillee = model.intVar(0, H * L);
        model.count(SURVEILLEE, x_ij1D, nbSurveillee).post();
       
        // Si une case est surveillée, il faut qu'elle soit observée par une caméra qui pointe vers cette direction.
        for(int i = 1; i < H-1; i++) {
            for(int j = 1; j < L-1; j++) {
                model.ifThen(model.arithm(x_ij[i][j], "=", SURVEILLEE),
                             model.or(getContraintesHaut(i,j),
                                      getContraintesBas(i,j),
                                      getContraintesGauche(i,j),
                                      getContraintesDroite(i,j)));
            }
        }
    }
   
    private static Constraint getContraintesDroite(int i, int j) {
        ArrayList<Constraint> lesOr = new ArrayList<>();
        //lesOr.add(model.arithm(x_ij[i][j], "!=", OUEST));
       
        for(int j2 = j + 1; j2 < (L-1); j2++) {
            ArrayList<Constraint> innerAnd = new ArrayList<>();
            for(int j3 = j + 1; j3 < j2; j3++)
                innerAnd.add(model.arithm(x_ij[i][j3], "=", SURVEILLEE));
            innerAnd.add(model.arithm(x_ij[i][j2], "=", OUEST));
            lesOr.add(model.and(innerAnd.toArray(new Constraint[innerAnd.size()])));
        }
        return lesOr.size() == 0 ? model.falseConstraint() : model.or((lesOr.toArray(new Constraint[lesOr.size()])));
    }
 
    private static Constraint getContraintesGauche(int i, int j) {
        ArrayList<Constraint> lesOr = new ArrayList<>();
        //lesOr.add(model.arithm(x_ij[i][j], "!=", EST));
       
        for(int j2 = j - 1; j2 > 0; j2--) {
            ArrayList<Constraint> innerAnd = new ArrayList<>();
            for(int j3 = j - 1; j3 > j2; j3--)
                innerAnd.add(model.arithm(x_ij[i][j3], "=", SURVEILLEE));
            innerAnd.add(model.arithm(x_ij[i][j2], "=", EST));
            lesOr.add(model.and(innerAnd.toArray(new Constraint[innerAnd.size()])));
        }
        return lesOr.size() == 0 ? model.falseConstraint() : model.or((lesOr.toArray(new Constraint[lesOr.size()])));
    }
 
    private static Constraint getContraintesBas(int i, int j) {
        ArrayList<Constraint> lesOr = new ArrayList<>();
 
        // La case en dessous peut être autre chose qu'une caméra NORD
        //lesOr.add(model.arithm(x_ij[i][j], "!=", NORD));
       
        // Pour chaque case vers le bas
        for(int i2 = i + 1; i2 < (H - 1); i2++) {
            // Il faut qu'on ait une caméra pointant vers le NORD
            // ou que des X puis une vers le NORD en remontant.
            ArrayList<Constraint> innerAnd = new ArrayList<>();
            for(int i3 = i + 1; i3 < i2; i3++) {
                innerAnd.add(model.arithm(x_ij[i3][j], "=", SURVEILLEE));
            }
            innerAnd.add(model.arithm(x_ij[i2][j], "=", NORD));
            lesOr.add(model.and(innerAnd.toArray(new Constraint[innerAnd.size()])));
        }
        return lesOr.size() == 0 ? model.falseConstraint() : model.or((lesOr.toArray(new Constraint[lesOr.size()])));
    }
 
    private static Constraint getContraintesHaut(int i, int j) {
        ArrayList<Constraint> lesOr = new ArrayList<>();
        //lesOr.add(model.arithm(x_ij[i][j], "!=", SUD));
       
        for(int i2 = i - 1; i2 > 0; i2--) {
            ArrayList<Constraint> innerAnd = new ArrayList<>();
            for(int i3 = i - 1; i3 > i2; i3--)
                innerAnd.add(model.arithm(x_ij[i3][j], "=", SURVEILLEE));
            innerAnd.add(model.arithm(x_ij[i2][j], "=", SUD));
            lesOr.add(model.and(innerAnd.toArray(new Constraint[innerAnd.size()])));
        }
        return lesOr.size() == 0 ? model.falseConstraint() : model.or((lesOr.toArray(new Constraint[lesOr.size()])));
    }
 
   
    private static void resoudre()
    {
        Solver solveur = model.getSolver();
        model.setObjective(Model.MAXIMIZE, nbSurveillee);
        while(solveur.solve()) {
            solveur.printStatistics();
            afficherMusee();
        }
    }
   
    /**
     * On pré-suppose que le musée a été initialisé
     */
    private static void afficherMusee()
    {
        System.out.println("Musée : ");
        for(int i = 0; i < H; i++) {
            for(int j = 0; j < L; j++) {
                System.out.print(valeursString[x_ij[i][j].getValue()] + " ");
            }
            System.out.println();
        }
    }
 
    public static void initialiserMusee(String file)
            throws ContradictionException, IOException
    {
        Vector<String> lignes = new Vector<String>();
        String ligne = null;
        BufferedReader br = new BufferedReader(new FileReader(new File(file)));
        System.out.println("Fichier : ");
        while ((ligne = br.readLine()) != null) {
            lignes.add(ligne);
            System.out.println(ligne);
        }
        if (lignes.size() > 0) {
            H = lignes.size();
            L = lignes.get(0).length();
        }
       
        x_ij = model.intVarMatrix(H, L, valeurs);
   
        for(int i = 0; i < H; i++) {
            ligne = lignes.get(i);
            for(int j = 0; j < L; j++) {
                // On met par défaut les murs à true (espaces et murs)
                if(ligne.charAt(j) == '*') {
                    x_ij[i][j].eq(MUR).post();
                } else {
                    // Interdiction de placer des murs
                    x_ij[i][j].ne(MUR).post();
                }
            }
        }
        br.close();
    }
}