package wdc.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import wdc.model.Pack;
import wdc.model.Task;
import wdc.repository.PackageRepository;
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
	PackageRepository packageRepository;
	@Autowired 
	FileStorageService fileStorageService;	
	
	
	public Set<String> addTask(Task task) {
		Set<String> errors = new HashSet<String>();
		if (!fileStorageService.checkIfFileExists(task.getDistributableDataPath())) {
			errors.add("distributableDataPath");
		}
		if (!fileStorageService.checkIfFileExists(task.getInputDataPath())) {
			errors.add("inputDataPath");
		}
		if (!fileStorageService.checkIfJsFileExists(task.getScriptPath())) {
			errors.add("scriptPath");
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
		if (task!= null) {
			return task;
		}
		return null;
	}
	
	public Integer getTaskProgress(Integer id) {
		return packageRepository.getPacksDone(id);
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
				task.setStartTime(System.currentTimeMillis());
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
	
	public void closeTask(Integer id) {
		Task task = taskRepository.findById(id);
		task.setStopTime(System.currentTimeMillis());
		task.setStatus(2);
		taskRepository.save(task);
		collectResults(task);
		//to do finish colecting results
	} 
	
	public Set<Task> getAvtiveTasks (){
		return taskRepository.findByStatus(1);
	}
	
	public String getInputData(Integer id) {
		Task task = taskRepository.findById(id);
		if (task.getStatus()==1) {
			try {
				return fileStorageService.getJsonFromFileByName(task.getInputDataPath());
			} catch (IOException e) {				
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public List<Task> getAllTasks(){
		return taskRepository.findAll();
	}
	public FileSystemResource getDoneFile(Integer id) {
		Task task = getTaskInfoById(id);
		if (task != null && task.getStatus()==3) {
			return fileStorageService.getFile(task.getDistributableDataPath());
		}
		return null;
	}
	
 	private void collectResults(Task task) {
 		int i =0;
 		while(task.getPackagesTotal()!= packageRepository.getPacksDone(task.getId())) {
 			i++;
				if(i<5) {
					try {
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					break;
				}
 		}
		Set<Pack> packs = packageService.getSetPackages(task);
		JSONArray data = new JSONArray();		
		try {
			for(Pack pack :packs) {
				String str = pack.getData();
				JSONObject obj = new JSONObject(str);
				data.put(obj);
				//data.put(new JSONArray(pack.getData()));
				
			}
			fileStorageService.saveStringToFIle(task.getDistributableDataPath(), data.toString());
			task.setStatus(3);	
			taskRepository.save(task);
		} catch (FileNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
