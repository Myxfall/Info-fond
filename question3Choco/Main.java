package question3Choco;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {

	public static void main(String[] args) {
		//Echec jeu =new Echec(args);
		JCommander.newBuilder().addObject(args).build().parse(args);
	}

}
