package com.spot.hero.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spot.hero.example.model.Rate;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.config.Searchable;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * InMemoryCacheInstance class.
 *
 */
public class InMemoryCacheInstance {


    public InMemoryCacheInstance() {
        CacheManager singletonManager = CacheManager.newInstance();
        Searchable searchable = new Searchable();
        CacheConfiguration cacheConfiguration = new CacheConfiguration()
                .name("rateCache")
                .maxEntriesLocalHeap(5000)
                .eternal(true)
                .searchable(searchable);
        searchable.addSearchAttribute(new SearchAttribute()
                .name("daysList")
                .expression("value.getDaysList()"));

        Cache rateCache = new Cache(cacheConfiguration);
        singletonManager.addCacheIfAbsent(rateCache);


        ObjectMapper mapper = new ObjectMapper();

        Logger logger = Logger.getLogger(InMemoryCacheInstance.class.getName());

        try {
            Map ratesMap = mapper.readValue(new File("init.json"), Map.class);
            List<Rate> rates = mapper.convertValue(ratesMap.get("rates"), new TypeReference<List<Rate>>() { });
            for(Rate rate : rates) {
                for(String day : rate.getDaysList()) {
                    Element cachedRates = rateCache.get(day);
                    if(rateCache.get(day) != null) {
                        ArrayList<Rate> existingRates = (ArrayList<Rate>)cachedRates.getObjectValue();
                        existingRates.add(rate);
                    } else {
                        ArrayList<Rate> listOfRates = new ArrayList<>();
                        listOfRates.add(rate);
                        Element element = new Element(day,listOfRates);
                        rateCache.put(element);
                    }
                }
            }
            logger.info("Successfully initialized values from init.json");
        } catch (IOException e) {
            logger.warning("No 'init.json' JSON file found in path: " + new File("init.json").getAbsolutePath());
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

}

