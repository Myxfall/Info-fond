package question3Choco;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("i", false, "mode independance");
		options.addOption("d", false, "mode domination");
		options.addOption("n", true, "longueure de l'echequier");
		options.addOption("t", true, "nombre de tour");
		options.addOption("f", true, "nombre de fou");
		options.addOption("c", true, "nombre de chevalier");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse( options, args);
		
		int n=Integer.parseInt(cmd.getOptionValue("n"));
		int t=Integer.parseInt(cmd.getOptionValue("t"));
		int f=Integer.parseInt(cmd.getOptionValue("f"));
		int c=Integer.parseInt(cmd.getOptionValue("c"));
		
		Echec jeu =new Echec(n,t,f,c);
		
		if(cmd.hasOption("-i")) {
			jeu.independance();
		}
		else if (cmd.hasOption("-d")){
			jeu.domination();
		}
		else{
			System.out.print("Erreur inconue");
		}
	}

}
