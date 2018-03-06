package wdc.repository;


import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wdc.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	Task findById (Integer id);
	List<Task> findAll();
	Set<Task> findByStatus(Integer status);
}