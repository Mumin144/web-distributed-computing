package wdc.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import wdc.model.Task;
import wdc.model.User;
import wdc.service.FileStorageService;
import wdc.service.TaskService;
import wdc.service.UserService;
import wdc.service.matrixTaskGenerator;

@Controller
@RequestMapping(path="/wdc")
public class TaskController {	
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	FileStorageService fileStorageService;	
	
	//test only
	@Autowired
	matrixTaskGenerator mtg;
	
	@ResponseBody
	@RequestMapping(value= {"/fileExists"}, method = RequestMethod.GET)
	public ResponseEntity<String> fileExists (@RequestParam("path") String path){
		if(fileStorageService.checkIfFileExists(path)) {
			return new ResponseEntity<String>("exists",HttpStatus.OK);
		}
		return new ResponseEntity<String>("not found",HttpStatus.NOT_FOUND);	
	}
	
	@ResponseBody
	@RequestMapping(value= {"/addTask"}, method = RequestMethod.POST)
	public ResponseEntity<Set<String>> addTask (@RequestBody Task task){
		Set<String> taskServiceResonse = taskService.addTask(task);
		if(taskServiceResonse.isEmpty()) {
			return new ResponseEntity<Set<String>>(taskServiceResonse,HttpStatus.OK);
		}
		return new ResponseEntity<Set<String>>(taskServiceResonse,HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/divideTask"}, method = RequestMethod.GET)
	public ResponseEntity<Task> divideTask(@RequestParam("id") Integer id){
		Task task = taskService.divideTask(id);
		if (task!=null) {
			return new ResponseEntity<Task>(task,HttpStatus.OK); 
		}			
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getTaskInfo"}, method = RequestMethod.GET)
	public ResponseEntity<Task> getTaskInfo(@RequestParam("id") Integer id){		
		Task task = taskService.getTaskInfoById(id);
		if (task!=null) {
			return new ResponseEntity<Task>(task,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getTaskData"}, method = RequestMethod.GET)
	public ResponseEntity<String> getTaskData(@RequestParam("id") Integer id){		
		Task task = taskService.getTaskInfoById(id);
		String taskData;
		if(task!= null) {
			try {
				taskData = fileStorageService.getJsonFromFileByName(task.getInputDataPath());
				return new ResponseEntity<String>(taskData,HttpStatus.OK);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
	}
	
	@ResponseBody
	@RequestMapping(value = {"/addRandomMatrix"}, method = RequestMethod.POST)
	public ResponseEntity<Set<String>> addRandomMatrix(@RequestBody String reqBody){
		Set<String> errors = new HashSet<>();		
		try {
			JSONObject json = new JSONObject(reqBody);
			if(json.getInt("size")<2 || json.getInt("size")>5000) {
				errors.add("size");
			}
			if(json.getInt("nrOfPacks")<2 || json.getInt("nrOfPacks")>5000) {
				errors.add("nrOfPacks");
			}
			if(json.getInt("range")<2 || json.getInt("range")>5000) {
				errors.add("range");
			}
			if(errors.size()== 0) {
				Task task = mtg.addNewTask(json.getInt("size"), json.getInt("nrOfPacks"),json.getInt("range"));
				taskService.addTask(task);
				return new ResponseEntity<Set<String>>(HttpStatus.OK); 
			}else {
				return new ResponseEntity<Set<String>>(errors ,HttpStatus.BAD_REQUEST); 
			}
			
		} catch (FileNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Set<String>>(HttpStatus.INTERNAL_SERVER_ERROR); 
		
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getDoneFile"}, method = RequestMethod.GET , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<FileSystemResource> getDoneFile(@RequestParam("id") Integer id) {
		HttpHeaders headers = new  HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=task"+id+".txt");
		return new ResponseEntity<FileSystemResource>(taskService.getDoneFile(id), headers, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/test"}, method = RequestMethod.GET)
	public ResponseEntity<String> test(){
		String str = null;
		/*try {
			str = mtg.addNewTask(10, 10, 5);
			return new ResponseEntity<String>(str ,HttpStatus.OK); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.isAuthenticated()){
			User user = userService.findUserByEmail(auth.getName());
			str = auth.getName();
		}*/
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		
		return new ResponseEntity<String>(timeStamp ,HttpStatus.OK); 
	}
	
}
