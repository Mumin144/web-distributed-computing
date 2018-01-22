package wdc.service;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import wdc.model.Pack;
import wdc.model.Task;
import wdc.repository.TaskRepository;

@Service("taskService")
@Configurable
public class TaskService {
	@Autowired
	PackageService packageService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	TaskRepository taskRepository;
	@Autowired 
	FileStorageService fileStorageService;
	
	public Set<String> addTask(Task task) {
		Set<String> errors = new HashSet<String>();
		if (!fileStorageService.checkIfFileExists(task.getDistributableDataPath())) {
			errors.add("DistributableDataPath  " + task.getDistributableDataPath() );
		}
		if (!fileStorageService.checkIfFileExists(task.getInputDataPath())) {
			errors.add("InputDataPath");
		}
		if (!fileStorageService.checkIfFileExists(task.getScriptPath())) {
			errors.add("ScriptPath");
		}
		if (errors.isEmpty()) {
			task.setStatus(0);
			taskRepository.save(task);
			return errors;
		}
		return errors;		
	}
	
	public Task getTaskInfoById(Integer id) {
		Task task = taskRepository.findById(id);
		if (task!= null && task.getStatus()== 1) {
			Set<Pack> packs ;
			
			return task;
		}
		return null;
	}
	
	public Task divideTask (Integer id) {
		Integer packCount = 0;
		Task task = taskRepository.findById(id);
		if (task!= null && task.getStatus()== 0) {
			try {
				String str = fileStorageService.getJsonFromFileByName(task.getDistributableDataPath());
				JSONArray json = new JSONObject(str).getJSONArray("divisible");						
				//task.setInputDataPath(json.getJSONObject(1).toString());
				Set<Pack> packs = new HashSet<>() ;
				for (int i=0;i <json.length();i++) {
					packCount ++;
					JSONArray data = new JSONArray();
					data.put(0, json.getJSONObject(i));
					data.put(1, task.getScriptPath());
					data.put(2, task.getScriptFunction());
					Pack pack = new Pack(data.toString(),0,task.getId());
					packs.add(pack);					
				}
				task.setStatus(1);
				task.setPackagesTotal(packCount);
				//SimpleDateFormat date = System.currentTimeMillis();				
				task.setStartTime(String.valueOf(System.currentTimeMillis()));
				taskRepository.save(task);
				packageService.saveSetPackages(packs);				
				return task;
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return null;
	}
	
	public Set<Task> getAvtiveTasks (){
		return taskRepository.findByStatus(1);
	}
}
