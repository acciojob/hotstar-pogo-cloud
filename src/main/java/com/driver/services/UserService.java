package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User savedUser=userRepository.save(user);
        return savedUser.getId();

    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){
       int count=0;
       User user=userRepository.findById(userId).get();
       List<WebSeries>webSeriesList=webSeriesRepository.findAll();
       Subscription subscription=user.getSubscription();
       SubscriptionType subscriptionType=subscription.getSubscriptionType();
       for(WebSeries webSeries:webSeriesList){
           if(subscriptionType.equals(SubscriptionType.BASIC)&&webSeries.getSubscriptionType().equals(SubscriptionType.BASIC)){
               count++;
           }else if(webSeries.getSubscriptionType().equals(SubscriptionType.PRO)){
               if(subscriptionType.equals(SubscriptionType.PRO)||subscriptionType.equals(SubscriptionType.BASIC)){
                   count++;
               }
           }
       }

       return count;

    }


}
