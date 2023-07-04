package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user=userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        int amount=0;
        if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
            amount=500+(200*subscription.getNoOfScreensSubscribed());
        }if(subscription.getSubscriptionType().equals(SubscriptionType.PRO)){
            amount=800+(250*subscription.getNoOfScreensSubscribed());
        }if(subscription.getSubscriptionType().equals(SubscriptionType.ELITE)){
            amount=1000+(350*subscription.getNoOfScreensSubscribed());
        }
        subscription.setTotalAmountPaid(amount);
        subscription.setUser(user);
        user.setSubscription(subscription);
        subscriptionRepository.save(subscription);
        return amount;

    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        Subscription subscription=user.getSubscription();
        if(subscription.getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }else{
            int prevPaid=subscription.getTotalAmountPaid();
            int newPaid=0;
            if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
                subscription.setSubscriptionType(SubscriptionType.PRO);
                newPaid=800+(250*user.getSubscription().getNoOfScreensSubscribed());
            }else if(subscription.getSubscriptionType().equals(SubscriptionType.PRO)){
                subscription.setSubscriptionType(SubscriptionType.ELITE);
                newPaid=1000+(350*user.getSubscription().getNoOfScreensSubscribed());
            }
            int amount=prevPaid-newPaid;
            user.getSubscription().setTotalAmountPaid(amount);
        }
        subscription.setUser(user);
        user.setSubscription(subscription);
        subscriptionRepository.save(subscription);
        return user.getSubscription().getTotalAmountPaid();
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        int amount = 0;
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        for(Subscription st : subscriptions){
            amount += st.getTotalAmountPaid();
        }
        return amount;
    }
    //totalAmountPaid;
}
