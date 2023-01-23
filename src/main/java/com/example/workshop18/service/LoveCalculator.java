package com.example.workshop18.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.workshop18.model.Love;

@Service
public class LoveCalculator {

    private static final String LOVE_CALCULATOR_URL = "https://love-calculator.p.rapidapi.com/getPercentage";
    

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    public Optional<Love> getCalculate(String firstName, String secondName) throws IOException {
            System.out.println(firstName);
            System.out.println(secondName);
            String calculatorUrl = UriComponentsBuilder
                                        .fromUriString(LOVE_CALCULATOR_URL)
                                        .queryParam("fname", firstName)
                                        .queryParam("sname", secondName)                                        
                                        .toUriString();
            System.out.println(calculatorUrl);
            RestTemplate template = new RestTemplate();

            //Set the headers you need send
            final HttpHeaders headers = new HttpHeaders();
            String loverApiKey = System.getenv("LOVER_API_KEY");
            String loverApiHost = System.getenv("LOVER_API_HOST");

            headers.set("X-RapidAPI-Key", loverApiKey);
            headers.set("X-RapidAPI-Host", loverApiHost);
    
            //Create a new HttpEntity
            final HttpEntity<String> entity = new HttpEntity<String>(headers);
            
            //Execute the method writing your HttpEntity to the request
            ResponseEntity<String> response = template.exchange(calculatorUrl, HttpMethod.GET, entity, String.class);                 
            Love l = Love.create(response.getBody());
            System.out.println(response.getBody());
    
            if(l != null) {
                redisTemplate.opsForValue().set(l.getId(), response.getBody().toString());
                return Optional.of(l);  
            }
            return Optional.empty();
               
    }

    public Love[] getAllMatchMaking() throws IOException {
        Set<String> allMatchMakingdKeys = redisTemplate.keys("*");
        List<Love> mArr = new LinkedList<Love>();
        for (String matchMakeKey : allMatchMakingdKeys) {
            String result = (String) redisTemplate.opsForValue().get(matchMakeKey);

            mArr.add((Love) Love.create(result));
        }

        return mArr.toArray(new Love[mArr.size()]);
    }
}
