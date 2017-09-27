package dao;

import java.util.List;

import entity.News;

public interface NewsDao {
	public void addNewsDao(News news);
	public List<News> selectNewsDao();
}
