package wdc.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;




@Service
@Configurable
public class FileStorageService {
		
	@Autowired
	ResourceLoader resourceLoader;
	
	final static String folderPath = "files/";
	final static String jsPath = "bin/static/js/";
	
	public boolean checkIfFileExists(String path) {
		File f = new File(folderPath + path);
		if (f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}
	
	public boolean checkIfJsFileExists(String path) {
		File f = new File(jsPath + path);
		if (f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}
	
	public String getJsonFromFileByName (String path) throws IOException {	
		if (checkIfFileExists(path)){
			File file = new File(folderPath + path);
			Path p = Paths.get(file.toURI());
			StringBuilder data = new StringBuilder();
			Stream<String> lines = Files.lines(p);
			lines.forEach(line -> data.append(line));
		    lines.close();
			return data.toString();
		}
		return null;
	}
	public void saveStringToFIle(String path,String data) throws FileNotFoundException {
		try(  PrintWriter out = new PrintWriter( folderPath + path )  ){
		    out.println( data );
		}
	}
	
	public FileSystemResource getFile(String path) {
		File file = new File(folderPath + path);
		return  new FileSystemResource(file);
	}

}
