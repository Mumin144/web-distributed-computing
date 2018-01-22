package wdc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

import wdc.model.Pack;
import wdc.repository.PackageRepository;
import wdc.repository.TaskRepository;

@Service("packageService")
@Configurable
public class PackageService {
	
	@Autowired
	private PackageRepository packageRepository;
	
			
	/*@Autowired
	private static PackageService instance = null;
	
	private PackageService() {
		
	}

	public static PackageService getInstance() {
		if (instance == null) {
			instance = new PackageService();
		}
		return instance;
	}*/
	
	public Pack getPackage() {		
		Pack pack = packageRepository.findFirstByStatus(0);
		//Pack pack = packageRepository.getFreePackage();
		if(pack!= null) {
			pack.setStatus(1);
			pack.setTime(this.getCurrentTimeInSeconds());
			packageRepository.save(pack);
			return pack;
		}
		return null;		
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
			packComp.setStatus(2);
			packComp.setData(pack.getData());
			packComp.setTime(pack.getTime()-this.getCurrentTimeInSeconds());
			packageRepository.save(packComp);
			return true;
		}
		return false;
	}
	public void saveSetPackages( Set<Pack> set) {
		packageRepository.save(set);
	}
	
	private Integer getCurrentTimeInSeconds() {
		return (int)(System.currentTimeMillis()/1000l);
	}
	
	
	

}
