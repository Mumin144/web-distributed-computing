package wdc.service;


import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import wdc.model.Task;

@Service("matrixTaskGenerator")
@Configurable
public class matrixTaskGenerator {
	
	@Autowired
	FileStorageService fileStorageService;
	
	
	public Task addNewTask(int size, int nrOfPacks, int range) throws FileNotFoundException, JSONException{
		Task task= new Task();
		task.setDistributableDataPath(saveDivisibeData(size, nrOfPacks, range));
		task.setInputDataPath(saveMatrix(size, range));
		task.setScriptPath("dijkstra.js");
		task.setScriptFunction("dijkstraGraf");
		return task;
	}
	private String saveMatrix(int size, int range) throws FileNotFoundException {
		int[][] matrix = new int[size][size];
		int i=0;
		int j=0;
		
		Random generator = new Random(); 
		for(i=0; i<size; i++) {
			for (j=i; j<size; j++) {
				matrix[i][j] = generator.nextInt(range) + 1;
				matrix[j][i] = matrix[i][j];				
			}
		}
		StringBuilder strMatrix = new StringBuilder();
		strMatrix.append('[');
		for(i=0; i<size; i++) {
			strMatrix.append('[');
			for(j=0; j<size; j++) {
				strMatrix.append(matrix[i][j]);
				if(j<size-1) {
					strMatrix.append(',');
				}
			}
			strMatrix.append(']');
			if(i<size-1) {
				strMatrix.append(',');
			}
		}
		strMatrix.append(']');
		return saveFile(strMatrix.toString());
	}
	
	private String saveDivisibeData(int size, int nrOfPacks, int range) throws FileNotFoundException, JSONException {
		Random generator = new Random(); 
		JSONObject json = new JSONObject();
		JSONArray divisible = new JSONArray();
		for (int i=0; i<nrOfPacks; i++) {
			int start = generator.nextInt(size);
			int finish = 0;
			do {
				finish = generator.nextInt(size);
			}while(start == finish);			
			JSONObject obj = new JSONObject();
			obj.put("s", start);
			obj.put("f", finish);
			divisible.put(obj);
		}
		json = new JSONObject();
		json.put("divisible", divisible);		
		return saveFile(json.toString());
	}
	
	private String saveFile(String fileStr) throws FileNotFoundException {
		String path = null;
		int i = 0;
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());		
		do {
			if(!fileStorageService.checkIfFileExists(timeStamp+i)) {
				path=timeStamp+i;
			}else {
				i++;
			}
		}while(path == null);
		fileStorageService.saveStringToFIle(path, fileStr);
		return path;
	}
}
