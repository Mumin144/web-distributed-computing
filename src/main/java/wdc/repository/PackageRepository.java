package wdc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import wdc.model.Pack;

@Repository
public interface PackageRepository extends JpaRepository<Pack, Integer> {

	Pack findFirstByStatus(Integer status);
	
	Pack findById (Integer status);
	
	
	
	/*@Query("SELECT p FROM Pack p WHERE p.status = 0")	
	Pack getFreePackage();*/
}