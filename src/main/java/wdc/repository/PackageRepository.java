package wdc.repository;

import java.util.Set;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import wdc.model.Pack;

@Repository
public interface PackageRepository extends JpaRepository<Pack, Integer> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Pack findFirstByStatus(Integer status);
	
	Set<Pack> findByTaskId(Integer id);
	
	Pack findById (Integer status);
		
	@Query("SELECT COUNT(p) FROM Pack p WHERE p.taskId = :id AND p.status = 2")
	Integer getPacksDone(@Param("id") Integer id);
}