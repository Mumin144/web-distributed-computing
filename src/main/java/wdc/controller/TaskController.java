package wdc.controller;


import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import wdc.model.Task;
import wdc.service.FileStorageService;
import wdc.service.PackageService;
import wdc.service.TaskService;

@Controller
@RequestMapping(path="/wdc")
public class TaskController {
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	PackageService packageService;
	
	@Autowired
	FileStorageService fileStorageService;	

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
		//String task = taskService.divideTask(id);
		if (task!=null) {
			return new ResponseEntity<Task>(task,HttpStatus.OK); 
		}			
		return new ResponseEntity<>(HttpStatus.IM_USED); 
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
	@RequestMapping(value = {"/test"}, method = RequestMethod.GET)
	public ResponseEntity<String> test(){
		String file = "";
		
		return new ResponseEntity<String>(file.toString(),HttpStatus.OK); 
	}	
	
}
