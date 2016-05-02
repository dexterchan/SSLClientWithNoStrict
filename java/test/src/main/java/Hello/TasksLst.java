package Hello;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TasksLst {
	private ArrayList<Task> tasks;

	
	public ArrayList<Task> getTasks() {
		return tasks;
	}


	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}


	//	public Task[] getTasks() {
//		return tasks;
//	}
//
//	public void setTasks(Task[] tasks) {
//		this.tasks = tasks;
//	}
	@Override
    public String toString() {
		String str="";
		
		for(Task t : tasks){
			str+=t.toString()+"\n";
		}
		
		return str;
	}
}


