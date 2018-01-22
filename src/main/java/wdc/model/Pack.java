package wdc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONException;
import org.json.JSONObject;


@Entity
@Table(name = "package")
public class Pack {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "package_id")
	private Integer id;
	
	@Column(name = "data")
	private String data;
	
	@Column(name = "status")
	private Integer status;
	
	
	@Column(name = "time", nullable = true)//time in ms
	private Integer time;
	
	@Column(name = "user_user_id")
	private Integer userId;
	
	@Column(name = "task_task_id")
	private Integer taskId;
		

	public Pack() {
		super();
	}

	public Pack(String data, Integer status, Integer taskId) {
		super();
		this.data = data;
		this.status = status;
		this.taskId = taskId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {		
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		try {
			json.put("id",this.id);
			json.put("data", this.getData());
			json.put("taskId", this.getTaskId());			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json.toString();
	}
	

}
