package wdc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import wdc.model.Pack;
import wdc.model.Task;
import wdc.model.User;
import wdc.repository.PackageRepository;
import wdc.repository.TaskRepository;

@Service("packageService")
@Configurable
public class PackageService {
	
	@Autowired
	private PackageRepository packageRepository;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;
	
	
	private HashMap<String, Integer> taskProgress = null;
	
	@Transactional
	public Pack getPackage() {		
		Pack pack = packageRepository.findFirstByStatus(0);
		if(pack!= null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getName()!= "anonymousUser"){
				User user = userService.findUserByEmail(auth.getName());
				pack.setUserId(user.getId());
			}
			pack.setStatus(1);
			pack.setTime(System.currentTimeMillis());
			Pack packTest = packageRepository.save(pack);
			return packTest;
		}
		return null;		
	}
	public Set<Pack> getSetPackages(Task task){		
			return packageRepository.findByTaskId(task.getId());		
	}
	
	public Boolean cancelPackage(Pack pack) {
		Pack packComp = packageRepository.findById(pack.getId());
		if(packComp!=null) {
			packComp.setStatus(0);
			packageRepository.save(packComp);
			return true;
		}
		return false;
	}
	
	public Boolean returnPackage(Pack pack) {
		Pack packComp = packageRepository.findById(pack.getId());
		if(packComp!=null) {
			if (packComp.getStatus()==1 && (pack.getUserId()==packComp.getUserId() || pack.getUserId()==0)) {				
				packComp.setStatus(2);
				packComp.setData(pack.getData());
				packComp.setTime(System.currentTimeMillis()-pack.getTime());
				packageRepository.save(packComp);				
				increaseTaskProgress(packComp.getTaskId());
				return true;
			}
			return false;
		}
		return false;
	}
	public void saveSetPackages( Set<Pack> set) {
		packageRepository.save(set);
	}
	
	private void updateTaskProgres (Integer id){
		Task task = taskService.getTaskInfoById(id);
		if(task.getStatus()==1) {
			taskProgress.put("all"+task.getId(), task.getPackagesTotal());
			taskProgress.put("done"+task.getId(), packageRepository.getPacksDone(task.getId()));
		}
	}
	private void increaseTaskProgress(Integer id) {
		if (taskProgress== null) {
			taskProgress = new HashMap<String,Integer>();
		}
		if (!taskProgress.containsKey("all"+id) & !taskProgress.containsKey("done"+id)){
			updateTaskProgres(id);
		}
		taskProgress.replace("done"+id, taskProgress.get("done"+id)+1);
		if(taskProgress.get("done"+id)==taskProgress.get("all"+id)){
			taskService.closeTask(id);
		}
	}	
}
