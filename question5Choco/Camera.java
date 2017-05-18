package question5Choco;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

public class Camera extends Piece{

    public Camera(int n, Model model, String dir){
		super(n, dir, model);
	}

    public Constraint surveilleOneCase(Piece cible){
        Model model=this.getModel();
        Constraint cameraConstraint = null;
        switch (this.getSens()) {
            case "N":
                if (cible.getCoordLigne().getValue() < this.getCoordLigne().getValue()){
                    cameraConstraint = model.arithm(this.getCoordColonne(), "=", cible.getCoordColonne());
                }
                break;
            case "S":
                if (cible.getCoordLigne().getValue() > this.getCoordLigne().getValue()){
                     cameraConstraint = model.arithm(this.getCoordColonne(), "=", cible.getCoordColonne());
                }
                break;
            case "E":
                if (cible.getCoordColonne().getValue() > this.getCoordColonne().getValue()){
                     cameraConstraint = model.arithm(this.getCoordLigne(), "=", cible.getCoordLigne());
                }
                break;
            case "O":
                if (cible.getCoordColonne().getValue() < this.getCoordColonne().getValue()){
                     cameraConstraint = model.arithm(this.getCoordLigne(), "=", cible.getCoordLigne());
                }
                break;
        }
        return cameraConstraint;
    }

	@Override
	public Constraint Menace(Piece pieceCible) {
		// TODO Auto-generated method stub
		return null;
	}
}
