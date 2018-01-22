package wdc.controller;

import javax.xml.ws.RequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import wdc.model.Pack;
import wdc.service.FileStorageService;
import wdc.service.PackageService;

@Controller
@RequestMapping(path="/wdc")
public class PackageController {
		
	@Autowired
	PackageService packageService;
	
	@ResponseBody
	@RequestMapping(value={"/getPackage"}, method = RequestMethod.GET)
	public ResponseEntity<Pack> getPackage() {
		Pack pack = packageService.getPackage();
		if(pack == null) {
			return new ResponseEntity<Pack>(HttpStatus.SERVICE_UNAVAILABLE);
		}		
		return new ResponseEntity<Pack>(pack,HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value={"/cancelPackage"}, method = RequestMethod.POST)
	public ResponseEntity<String> cancelPackage(@RequestBody Pack pack) {
		Boolean bool = packageService.cancelPackage(pack);
		if (bool) {
			return new ResponseEntity<String>("success",HttpStatus.OK);
		}		
		return new ResponseEntity<String>("fail",HttpStatus.OK);		
	}
	
	@ResponseBody
	@RequestMapping(value={"/returnPackage"}, method = RequestMethod.POST)
	public ResponseEntity<String> returnPackage(@RequestBody Pack pack) {
		Boolean bool = packageService.returnPackage(pack);
		if (bool) {
			return new ResponseEntity<String>("success",HttpStatus.OK);
		}		
		return new ResponseEntity<String>("fail",HttpStatus.OK);			
	}
	
	
	
}
