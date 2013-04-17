import java.io.File;
import java.io.IOException;


public class GenerateTemplates_KoenkerZeileis09 {
	

	
	public static void main(String[] args) {
		
		try {
			String workingDir = "C:\\data\\workspace-juno\\ddocks\\samples\\koenkerzeileis09";

//			File genDir = new File(workingDir, "generated");
//			genDir.mkdir();
			
			
			// process raw file
			File inFile = new File(workingDir, "rk.raw");
			AutoReplaceValues.autoReplace(inFile, false);

		
			// process CSV file
			inFile = new File(workingDir, "data.dj");
			AutoReplaceValues.autoReplace(inFile, true);
			
			
			System.out.println("Done");

		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
