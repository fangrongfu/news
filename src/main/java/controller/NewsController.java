package controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import entity.News;
import service.NewsService;

@Controller
@RequestMapping("/news")
public class NewsController {
	
	@Resource
	private NewsService newsService;
	
	@RequestMapping("/add")
	public void addNewsController() {
		System.setProperty("webdriver.chrome.driver", "D:\\software\\noeclipse\\diary-workspace\\news\\src\\main\\resources\\google\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		String url = "http://blog.sina.com.cn/lm/sports/china/";
		driver.get(url);
		List<WebElement> list1 = driver.findElements(By.className("c_tit"));
		List<WebElement> list = driver.findElements(By.className("c_time"));
		News news = new News();
		for(int i = 0; i < list1.size(); i++) {
			news.setTitle(list1.get(i).getText());
			news.setTime(list.get(i).getText());
			newsService.addNewsService(news);
		}
		driver.quit();
	}
	
	@RequestMapping("/search")
	public void selectNewsController() throws UnknownHostException {
		Settings settings = Settings.builder()
		        .put("cluster.name", "elasticsearch").build();
		TransportClient client = new PreBuiltTransportClient(settings)
		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		List<News> list = new ArrayList<News>();
		list = newsService.selectNewsService();
		Map<String, Object> json = new HashMap<String, Object>();
		for(int i = 0; i < list.size(); i++) {
			json.put("id",list.get(i).getId());
			json.put("title",list.get(i).getTitle());
			json.put("time",list.get(i).getTime());
			IndexResponse response = client.prepareIndex("twitter", "tweet",i+"")
			        .setSource(json, XContentType.JSON)
			        .get();
		}
	}
	
	@RequestMapping("/result")
	public String searchNewsController(HttpServletRequest request, Model model, String title) throws UnknownHostException {
		Settings settings = Settings.builder()
		        .put("cluster.name", "elasticsearch").build();
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		/*SearchResponse response = client.prepareSearch("twitter").setTypes("tweet")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.queryStringQuery(title)) // Query
				.setFrom(0).setSize(60).setExplain(true).get();
		SearchHits searchHits = response.getHits();
		List<News> list = new ArrayList<News>();
		for (SearchHit hit : searchHits) {
			News news = new News();
			Integer id = (Integer) hit.getSource().get("id");
			String name = (String) hit.getSource().get("title");
			String time = (String) hit.getSource().get("time"); 
			news.setId(id);
			news.setTitle(name);
			news.setTime(time);
			list.add(news);
		}*/
		QueryBuilder matchQuery = QueryBuilders.matchQuery("title", title);
        HighlightBuilder hiBuilder=new HighlightBuilder();
        hiBuilder.preTags("<span style=\"color:red\">");
        hiBuilder.postTags("</span>");
        hiBuilder.field("title");
        // 搜索数据
        SearchResponse response = client.prepareSearch("twitter")
                .setQuery(matchQuery)
                .setExplain(true)
                .highlighter(hiBuilder)
                .execute().actionGet();
        //获取查询结果集
        SearchHits searchHits = response.getHits();
        List<News> list = new ArrayList<News>();
		for (SearchHit hit : searchHits) {
			News news = new News();
			Integer id = (Integer) hit.getSource().get("id");
			news.setId(id);
			//获取对应的高亮域
            Map<String, HighlightField> result = hit.highlightFields();;    
            //从设定的高亮域中取得指定域
            HighlightField titleField = result.get("title");  
            //取得定义的高亮标签
            Text[] titleTexts =  titleField.fragments();   
            //为title串值增加自定义的高亮标签
            String title1 = "";  
            for(Text text : titleTexts){    
                  title1 += text;  
            }
            //将追加了高亮标签的串值重新填充到对应的对象
            news.setTitle(title1);
			news.setTime((String) hit.getSource().get("time"));
			list.add(news);
		}
		model.addAttribute("list", list);
		return "index";
	}
}
