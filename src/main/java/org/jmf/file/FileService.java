package org.jmf.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.FileSystems;
import java.util.Properties;


/*
 * in charge of adding new lines to files, and create a new file it it does not exist
 */
public class FileService {
	
	public static void addLine(String fileName, String line) throws IOException {	
		
		Files.write(Paths.get(fileName), (line + System.getProperty("line.separator"))
				.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}	
	
	public static String getProperty(String propertiesFileName, String propertyName)  {

		Path path = FileSystems.getDefault().getPath(".").toAbsolutePath();

		File file = new File(propertiesFileName);
		if (!file.exists()) {
			propertiesFileName = "./src/main/resources/conf/" + propertiesFileName;
		}

		try (InputStream in = new FileInputStream(propertiesFileName)) {
			 Properties prop = new Properties();
			 prop.load(in);			 
			 return prop.getProperty(propertyName); 
		 }	catch (FileNotFoundException fnf) {
			 fnf.printStackTrace();
		 } catch (IOException ioe) {
			 ioe.printStackTrace();
		 }
		 return null;
	}
}
