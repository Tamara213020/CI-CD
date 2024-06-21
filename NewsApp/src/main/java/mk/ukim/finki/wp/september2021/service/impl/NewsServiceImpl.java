package mk.ukim.finki.wp.september2021.service.impl;

import mk.ukim.finki.wp.september2021.model.News;
import mk.ukim.finki.wp.september2021.model.NewsCategory;
import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsCategoryIdException;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsIdException;
import mk.ukim.finki.wp.september2021.repository.NewsCategoryRepository;
import mk.ukim.finki.wp.september2021.repository.NewsRepository;
import mk.ukim.finki.wp.september2021.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Override
    public List<News> listAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
    }

    @Override
    public News create(String name, String description, Double price, NewsType type, Long category) {
        NewsCategory newsCategory = newsCategoryRepository.findById(category).orElseThrow(InvalidNewsCategoryIdException::new);
        News news = new News(name,description,price,type,newsCategory);

        return newsRepository.save(news);
    }

    @Override
    public News update(Long id, String name, String description, Double price, NewsType type, Long category) {
        NewsCategory newsCategory = newsCategoryRepository.findById(category).orElseThrow(InvalidNewsCategoryIdException::new);
        News news = this.findById(id);

        news.setName(name);
        news.setDescription(description);
        news.setPrice(price);
        news.setType(type);
        news.setCategory(newsCategory);

        return newsRepository.save(news);
    }

    @Override
    public News delete(Long id) {
        News n1 = this.findById(id);
        newsRepository.delete(n1);
        return n1;
    }

    @Override
    public News like(Long id) {
        News n1 = this.findById(id);
        n1.setLikes(n1.getLikes()+1);
        return newsRepository.save(n1);
    }

    @Override
    public List<News> listNewsWithPriceLessThanAndType(Double price, NewsType type) {
        if(price==null && type==null)
        {
            return listAllNews();
        }
        else if(price==null)
        {
            return newsRepository.findAllByTypeLike(type);
        }
        else if(type==null)
        {
            return newsRepository.findAllByPriceLessThan(price);
        }
        else
        {
            return newsRepository.findAllByPriceLessThanAndTypeLike(price,type);
        }
    }
}
