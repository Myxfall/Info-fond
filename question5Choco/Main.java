package question5Choco;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class Main {
	

	public static void main(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("f", true, "fichier");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse( options, args);
		
		String filename=cmd.getOptionValue("f");
		
		if(cmd.hasOption("-f")) {
			Surveillance jeu =new Surveillance(filename);
			jeu.minCamera();
			
		}
		else{
			System.out.print("Erreur inconue");
		}
	}

}
