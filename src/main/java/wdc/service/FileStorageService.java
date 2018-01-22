package wdc.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;




@Service
@Configurable
public class FileStorageService {
		
	@Autowired
	ResourceLoader resourceLoader;
	
	final static String folderPath = "files/";
	
	public boolean checkIfFileExists(String path) {
		File f = new File(folderPath + path);
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

}
