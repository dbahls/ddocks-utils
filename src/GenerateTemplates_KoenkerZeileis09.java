import java.io.File;
import java.io.IOException;


public class GenerateTemplates_KoenkerZeileis09 {
	

	
	public static void main(String[] args) {
		
		try {
			File baseDir   = new File("../paper-restore-model/");
			File outputDir = new File(baseDir, "data-templates/");

			
			// process raw file
			File inFile = new File(baseDir, "koenker-zeileis-09/rk.raw");
			AutoReplaceValues.autoReplace(inFile, outputDir, false);

			
		
			// process CSV file
			inFile = new File(baseDir, "koenker-zeileis-09/data.dj");
			AutoReplaceValues.autoReplace(inFile, outputDir, true);
			
			
			System.out.println("Done");

		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
