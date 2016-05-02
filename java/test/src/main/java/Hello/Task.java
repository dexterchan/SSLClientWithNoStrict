package Hello;

public class Task {
    String id;
	String title;
	String description;
	boolean done;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	@Override
    public String toString() {
        return "task{" +
                "id='" + id + '\'' +
                ", title=" + title +
                ", description=" + description +
                ", done=" + done +
                '}';
    }
}

