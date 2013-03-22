import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.spec.PSource;

import org.apache.commons.codec.digest.DigestUtils;


public class AutoReplaceValuesCSV {


	static String archiveNS  = "demoArchive";
	static String archiveURL = "http://demoarchive.demo/";
	static String archiveEndpoint = "http://localhost:8090/openrdf-sesame/repositories/koenkerzeileis_v1";

	public static void main(String[] args) {

		
		try {
			String workingDir = "C:\\data\\workspace-juno\\ddocks\\samples\\koenkerzeileis09";
			File genDir = new File(workingDir, "generated");
			genDir.mkdir();
			
//			File fileIn  = new File(workingDir, "rk.raw");
			File fileIn  = new File(workingDir, "data.dj");
			File fileOutTemplate = new File(genDir, fileIn.getName()+".ddocks");
			File fileOutTriples  = new File(genDir, fileIn.getName()+".ddocks.nt");

			
			String tokenOpen  = "<";
			String tokenClose = ">";

			BufferedReader br = new BufferedReader(new FileReader(fileIn));
			PrintStream psDataTemplate = new PrintStream(fileOutTemplate);
			PrintStream psDataTriples  = new PrintStream(fileOutTriples);

			// generate header
			String header = String.format(
					"# ddocks template header\n" +
					"#	for documentation see ...\n" +
					"#\n" +
					"#\n" +
					"#			<open token>	<close token>\n" +
					"#@tokens	%s\t%s\n" +
					"#\n" +
					"#				<short>	<uriprefix>		<sparql endpoint>\n" +
					"#@namespace	%s\t%s\t%s\n" +
					"#"
					, tokenOpen, tokenClose, archiveNS, archiveURL, archiveEndpoint);
			
			psDataTemplate.println(header);
			

			
			
			
			String line;
			
			// skip header row
			psDataTemplate.println(br.readLine());
			
			
			
			
			Pattern regex = Pattern.compile("\\b\\S+\\b");
			
			
			while ((line=br.readLine())!=null) {
				
				Matcher m = regex.matcher(line);
				int index = 0;
				String lineOutTemplate = "";
				
				while (m.find()) {
					
					String value = m.group();
					
					System.out.println("Found value: "+value);
					
					String id = requestIDfor(value);
					
//					line = line.substring(index, m.start()) + tokenOpen + id + tokenClose + line.substring(m.end());
//					m = regex.matcher(line);

					// good for debugging:
//					lineOut += line.substring(index, m.start()) + value+ tokenOpen + id + tokenClose;
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
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

	
	static int counter = 1452134;

	public static String requestIDfor(String value) {
		
//		return value;
		
		
//		String id = DigestUtils.sha512Hex(""+counter+value);
		String id = DigestUtils.sha512Hex(""+counter);
		
		counter++;
		return id;
	}
	
}
