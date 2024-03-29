/**
 * Author Rusu George, Romain Maximilien
 */


package question4Choco;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class Main {

	public static void main(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("n", true, "longueure de l'echequier");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse( options, args);
		
		int n=Integer.parseInt(cmd.getOptionValue("n"));
		
		MinCavalier jeu =new MinCavalier(n);
		
		if(cmd.hasOption("-n")) {
			jeu.MinimizationCavalier();
		}
		else{
			System.out.print("Erreur inconue");
		}

	}

}
