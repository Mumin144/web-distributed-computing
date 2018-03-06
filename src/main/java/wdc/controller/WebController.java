package wdc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import wdc.model.Task;
import wdc.model.User;
import wdc.service.TaskService;
import wdc.service.UserService;

@Controller
public class WebController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView = whoIsLogged(modelAndView);
		modelAndView.setViewName("login");		
		return modelAndView;
	}
	
	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView = whoIsLogged(modelAndView);
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");
			
		}
		return modelAndView;
	}
	
	@RequestMapping(value={"/counter"}, method = RequestMethod.GET)
	public ModelAndView counter(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("counter");
		modelAndView = whoIsLogged(modelAndView);
		String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.S").format(new Date());
		int tim = time.length();
		modelAndView.addObject("time", tim);
		return modelAndView;
	}
	
	@RequestMapping(value={"/admin/tasks"}, method = RequestMethod.GET)
	public ModelAndView tasks() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/tasks");
		List<Task> tasks = taskService.getAllTasks();
		HashMap<Integer,Integer> done = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> percent = new HashMap<Integer,Integer>();
		for (Task task : tasks) {
			if(task.getStatus()==1) {
				done.put(task.getId(), taskService.getTaskProgress(task.getId()));
				percent.put(task.getId(), ((done.get(task.getId())*100)/task.getPackagesTotal()));
				//modelAndView.addObject("done"+task.getId().toString(), taskService.getTaskProgress(task.getId()));
			}
		}
		modelAndView.addObject("percent", percent);
		modelAndView.addObject("done", done);
		modelAndView.addObject("tasks", tasks);		
		return modelAndView;
	}
	
	private ModelAndView whoIsLogged(ModelAndView mav) {
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString();
		if (role.equalsIgnoreCase("ROLE_ANONYMOUS")){
			return mav;
		}else {
			if(role.equalsIgnoreCase("ADMIN")) {
				mav.addObject("ADMIN", true);
			}
			mav.addObject("USER", true);
			return mav;
		}
	}
	
	
	
	
}
