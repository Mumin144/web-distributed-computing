package wdc.model;

import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "task")
public class Task {
	@Id
	@Column(name = "task_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	
	@Column(name = "input_data_path")
	private String inputDataPath;
	
	@Column(name = "distributable_data_path")
	private String distributableDataPath;
	
	@Column(name = "script_path")
	private String scriptPath;
	
	@Column(name = "script_function")
	private String scriptFunction;	
	
	@Column(name = "status")
	private Integer status;	
	
	@Column(name = "packages_total")
	private Integer packagesTotal;	
	
	@Column(name = "start_time")
	private String startTime;	
	
	@Column(name = "stop_time")
	private String stopTime;
	
	

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInputDataPath() {
		return inputDataPath;
	}

	public void setInputDataPath(String inputDataPath) {
		this.inputDataPath = inputDataPath;
	}

	public String getDistributableDataPath() {
		return distributableDataPath;
	}

	public void setDistributableDataPath(String distributableDataPath) {
		this.distributableDataPath = distributableDataPath;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public String getScriptFunction() {
		return scriptFunction;
	}

	public void setScriptFunction(String scriptFunction) {
		this.scriptFunction = scriptFunction;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getPackagesTotal() {
		return packagesTotal;
	}

	public void setPackagesTotal(int packagesTotal) {
		this.packagesTotal = packagesTotal;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}
}
