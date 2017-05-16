package question5Choco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import question3Choco.Jeu;

public class Main {
	
	private ArrayList parseFile(String FILENAME){
		BufferedReader br = null;
		FileReader fr = null;
		ArrayList<String> test=

		try {

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILENAME));

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
		}catch (IOException e) {

				e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("f", true, "fichier");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse( options, args);
		
		int n=Integer.parseInt(cmd.getOptionValue("n"));
		int t=Integer.parseInt(cmd.getOptionValue("t"));
		int f=Integer.parseInt(cmd.getOptionValue("f"));
		int c=Integer.parseInt(cmd.getOptionValue("c"));
		
		Jeu jeu =new Jeu(n,t,f,c);
		
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
