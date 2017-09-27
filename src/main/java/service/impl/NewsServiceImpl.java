package service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import dao.NewsDao;
import entity.News;
import service.NewsService;

@Service
public class NewsServiceImpl implements NewsService {
	
	@Resource
    private NewsDao newsDao;
	
	public void addNewsService(News news) {
		// TODO Auto-generated method stub
		newsDao.addNewsDao(news);
	}

	@Override
	public List<News> selectNewsService() {
		// TODO Auto-generated method stub
		return newsDao.selectNewsDao();
	}


}
