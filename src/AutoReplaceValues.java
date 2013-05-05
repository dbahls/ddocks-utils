import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


public class AutoReplaceValues {


	static String archiveNS  = "demoArchive";
	static String archiveURL = "http://demoarchive.demo/data/";
	static String archiveEndpoint = "http://localhost:8090/openrdf-sesame/repositories/koenkerzeileis_v1";
	
	

	public static void autoReplace(File inFile, File outputDir, boolean skipHeader) throws IOException {
		
		String tokenOpen  = "<";
		String tokenClose = ">";

		BufferedReader br = new BufferedReader(new FileReader(inFile));
		
		File outFileTemplate = new File(outputDir, inFile.getName() + ".ddocks");
		File outFileTriples  = new File(outputDir, inFile.getName() + ".ddocks.nt");
		
		PrintStream psDataTemplate = new PrintStream(outFileTemplate);
		PrintStream psDataTriples  = new PrintStream(outFileTriples);

		// generate header
		String header = String.format(
				"<?ddocks version=\"0.1\" encoding=\"UTF-8\"?>\n" + 
				"<ddocks-header>\n" + 
				"\n" + 
				"@tokens	%s	%s\n" + 
				"@namespace	%s	%s	%s\n" + 
				"\n" + 
				"</ddocks-header>"
				, tokenOpen, tokenClose, archiveNS, archiveURL, archiveEndpoint);
		
		psDataTemplate.println(header);
		

		String line;

		if (skipHeader) {
			// skip header row
			psDataTemplate.println(br.readLine());
		}
		

		// regex to identify values
		Pattern regex = Pattern.compile("[\\p{Punct}\\S]+");
		
		
		while ((line=br.readLine())!=null) {
			
			Matcher m = regex.matcher(line);
			int index = 0;
			String lineOutTemplate = "";
			
			while (m.find()) {
				
				String value = m.group();
				
				System.out.println("Found value: "+value);
				
				String id = requestIDfor(value);
				
//				line = line.substring(index, m.start()) + tokenOpen + id + tokenClose + line.substring(m.end());
//				m = regex.matcher(line);

				// good for debugging:
//				lineOut += line.substring(index, m.start()) + value+ tokenOpen + id + tokenClose;
				lineOutTemplate += line.substring(index, m.start()) + tokenOpen + archiveNS + ":" + id + tokenClose;
				
				index = m.end();

				
				
				// produce triples, so that every single value is associated with an ID
				String uri = "<" + archiveURL + id + ">";
				psDataTriples.println(uri + "\t<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\t<http://lod.gesis.org/sweavelod/DataReference> .");
				psDataTriples.println(uri + "\t<http://www.w3.org/1999/02/22-rdf-syntax-ns#value>\t\""+value+"\" .");
				
				
				
			}
			
			lineOutTemplate += line.substring(index);
			psDataTemplate.println(lineOutTemplate);
		}
		
		
		psDataTemplate.close();
		br.close();
		
	}
	
	
	static long counter = 0;

	public static String requestIDfor(String value) {
		
		String tmp = ""+counter;
		String id  = Base64.encodeBase64URLSafeString(tmp.getBytes());
		
		counter++;
		return id;
	}
	
}
