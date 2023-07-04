package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        WebSeries webSeries=webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(webSeries.getSeriesName()!=null){
            throw new Exception("Series is already present");
        }
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        ProductionHouse productionHouse=productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
        webSeries.setProductionHouse(productionHouse);
        productionHouse.getWebSeriesList().add(webSeries);
        double oldRating=productionHouse.getRatings();
        double newRating=webSeries.getRating();
        int n=productionHouse.getWebSeriesList().size();
        double updateRating=oldRating+(oldRating-newRating)/n;
        productionHouse.setRatings(updateRating);
        ProductionHouse saveProductionHouse=productionHouseRepository.save(productionHouse);
        return webSeries.getId();
    }

}
