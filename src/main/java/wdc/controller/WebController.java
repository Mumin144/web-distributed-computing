package wdc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {
	
	@RequestMapping(value={"/counter"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("counter");
		String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.S").format(new Date());
		int tim = time.length();
		modelAndView.addObject("time", tim);
		return modelAndView;
	}
}
