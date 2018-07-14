package com.spot.hero.example.service;

import com.spot.hero.example.model.Rate;
import com.spot.hero.example.util.Days;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RateService {

    private Cache rateCache;

    public RateService() {
        CacheManager cacheManager = CacheManager.getInstance();
        rateCache =
                cacheManager.getCache("rateCache");
    }

    public void addRate(Rate rate) {
        for(String day : rate.getDaysList()) {
            Element cachedRates = rateCache.get(day);
            if(rateCache.get(day) != null) {
                ArrayList<Rate> rates = (ArrayList<Rate>)cachedRates.getObjectValue();
                rates.add(rate);
            } else {
                ArrayList<Rate> listOfRates = new ArrayList<>();
                listOfRates.add(rate);
                Element element = new Element(day,listOfRates);
                rateCache.put(element);
            }
        }
    }

    public BigDecimal getRateFromStartEndDateTime(LocalDateTime start, LocalDateTime end) {
        DayOfWeek day = start.getDayOfWeek();
        int startRange = (start.getHour() * 100) + start.getMinute();
        int endRange = (end.getHour() * 100) + end.getMinute();
        Element element = rateCache.get(Days.fromDayOfWeek(day));
        if(element != null) {
            List<Rate> foundRates = ((List<Rate>)element.getObjectValue());
            Optional<Rate> rate = foundRates.stream()
                    .filter((a) -> a.getStartTime() <= startRange && a.getEndTime() >= endRange)
                    .findFirst();
            if(rate.isPresent()) {
                return rate.get().getPrice();
            }
        }
        return null;
    }

    public void clearRates() {
        rateCache.removeAll();
    }

    public boolean rateIsValid(Rate rate) {
        return rate != null && !rate.getDaysList().isEmpty();
    }
}
