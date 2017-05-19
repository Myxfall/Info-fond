package question5Choco;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public abstract class Camera{
	
	private IntVar coordLigne;
	private IntVar coordColonne;
	private Model model;
	private String type;

    public Camera(Model model, String type, int dimensionLigne, int dimensionColonne){
    	this.model=model;
    	this.type=type;
    	this.coordLigne=this.model.intVar("coordLigne",0,dimensionLigne-1);
		this.coordColonne=this.model.intVar("coordColonne",0,dimensionColonne-1);
	}
    
    public abstract Constraint surveille(Camera camera);
    
    public IntVar getCoordLigne(){
    	return this.coordLigne;
    }
    
    public IntVar getCoordColonne(){
    	return this.coordColonne;
    }
    
    public Model getModel(){
    	return this.model;
    }
    public String getType(){
    	return this.type;
    }
}
