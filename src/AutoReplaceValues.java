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

import org.apache.commons.codec.digest.DigestUtils;


public class AutoReplaceValues {

	
	public static void main(String[] args) {

		
		try {
			String workingDir = "C:\\data\\workspace-juno\\ddocks\\samples\\koenkerzeileis09";
			
			File fileIn  = new File(workingDir, "rk.raw");
			File fileOut = new File(workingDir, "rk.raw.ddocks");
			
			List<String> replaceEverythingBut = Arrays.asList("id,neq,nreg,nobs,author,journal,year,page,subject,collection".split(","));
			
			BufferedReader br = new BufferedReader(new FileReader(fileIn));
			PrintStream ps = new PrintStream(fileOut);
			
			String line;
			Pattern regex = Pattern.compile("\\b\\S+\\b");
			
			String tokenOpen  = "<";
			String tokenClose = ">";
			
			while ((line=br.readLine())!=null) {
				
				Matcher m = regex.matcher(line);
				int index = 0;
				String lineOut = "";
				
				while (m.find()) {
					
					String value = m.group();
					
					if (replaceEverythingBut.contains(value)) {
						System.out.println("Skipping value: "+value);
						continue;
					}
					
					System.out.println("Found value: "+value);
					
					String id = requestIDfor(value);
					
//					line = line.substring(index, m.start()) + tokenOpen + id + tokenClose + line.substring(m.end());
//					m = regex.matcher(line);

					lineOut += line.substring(index, m.start()) + tokenOpen + id + tokenClose;
					index = m.end();

				}
				
				lineOut += line.substring(index);
				
				ps.println(lineOut);
			}
			
			
			ps.close();
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

	
	static int counter = 0;

	public static String requestIDfor(String value) {
		
//		return value;
		
		
		String id = "valueStore:" + DigestUtils.sha512Hex(""+counter+value);
		
		counter++;
		return id;
	}
	
}
