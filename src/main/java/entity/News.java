package entity;

public class News {
	private int id;//新闻表的ID
	private String title;//新闻表的标题
	private String time;//新闻表的新闻发布时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", time=" + time + "]";
	}
}
