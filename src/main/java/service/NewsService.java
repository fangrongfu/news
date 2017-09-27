package service;

import java.util.List;

import entity.News;

public interface NewsService {
	public void addNewsService(News news);
	public List<News> selectNewsService();
}
