package com.spot.hero.example;

import com.spot.hero.example.service.RateService;
import com.spot.hero.example.model.Rate;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RateServiceTest {

    private RateService target;
    private Cache memoryOnlyCache;
    private CacheManager cacheManager;

    @Before
    public void setup() {
        cacheManager = CacheManager.newInstance();
        CacheConfiguration cacheConfiguration = new CacheConfiguration()
                .name("rateCache")
                .maxEntriesLocalHeap(5000);
        memoryOnlyCache = new Cache(cacheConfiguration);
        cacheManager.addCache(memoryOnlyCache);
        target = new RateService();
    }

    @After
    public void shutdown() {
        cacheManager.shutdown();
    }

    @Test
    public void addRate_givenRateWithMultipleDays_shouldStoreRateForEachDay() {
        Rate rate = new Rate();
        rate.setTimes("600-1800");
        rate.setPrice(new BigDecimal(1500));
        List<String> days = Arrays.asList("mon", "tues", "wed", "thurs", "fri");
        rate.setDaysList(days);

        target.addRate(rate);

        List<Rate> rates = (List<Rate>)memoryOnlyCache.get("mon").getObjectValue();
        assertEquals(rates.get(0), rate);
        rates = (List<Rate>)memoryOnlyCache.get("tues").getObjectValue();
        assertEquals(rates.get(0), rate);
        rates = (List<Rate>)memoryOnlyCache.get("wed").getObjectValue();
        assertEquals(rates.get(0), rate);
        rates = (List<Rate>)memoryOnlyCache.get("thurs").getObjectValue();
        assertEquals(rates.get(0), rate);
        rates = (List<Rate>)memoryOnlyCache.get("fri").getObjectValue();
        assertEquals(rates.get(0), rate);
    }

    @Test
    public void addRate_givenMultipleRatesWithSameDays_shouldStoreBothRatesForSameDay() {
        List<Rate> testRates = getTestRates();
        for(Rate rate: testRates) {
            target.addRate(rate);
        }
        Rate sameDateRate = new Rate();
        sameDateRate.setTimes("600-1800");
        sameDateRate.setPrice(new BigDecimal(1500));
        List<String> days = Arrays.asList("mon");
        sameDateRate.setDaysList(days);
        target.addRate(sameDateRate);

        List<Rate> rates = (List<Rate>)memoryOnlyCache.get("mon").getObjectValue();
        assertEquals(2, rates.size());
        assertEquals(rates.get(0), testRates.get(0));
        assertEquals(rates.get(1), sameDateRate);
        List<Rate> tuesRates = (List<Rate>)memoryOnlyCache.get("tues").getObjectValue();
        assertEquals(1, tuesRates.size());
    }

    @Test
    public void getRateFromStartEndDateTime_givenValidRange_shouldReturnCorrectPrice() {
        List<Rate> testRates = getTestRates();
        for(Rate rate: testRates) {
            target.addRate(rate);
        }

        LocalDateTime start = LocalDateTime.parse("2015-07-01T07:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse("2015-07-01T12:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
        BigDecimal weekDayPrice = target.getRateFromStartEndDateTime(start, end);
        start = LocalDateTime.parse("2015-07-04T07:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
        end = LocalDateTime.parse("2015-07-04T12:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
        BigDecimal weekEndPrice = target.getRateFromStartEndDateTime(start, end);

        assertEquals(testRates.get(0).getPrice(), weekDayPrice);
        assertEquals(testRates.get(1).getPrice(), weekEndPrice);
    }


    @Test
    public void getRateFromStartEndDateTime_givenInvalidRange_shouldReturnNull() {
        List<Rate> testRates = getTestRates();
        for(Rate rate: testRates) {
            target.addRate(rate);
        }

        LocalDateTime start = LocalDateTime.parse("2015-07-01T07:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse("2015-07-01T20:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
        BigDecimal shouldBeNull = target.getRateFromStartEndDateTime(start, end);

        assertNull(shouldBeNull);
    }


    private List<Rate> getTestRates() {
        List<Rate> rates = new ArrayList<>();
        Rate rate = new Rate();
        rate.setTimes("600-1800");
        rate.setPrice(new BigDecimal(1500));
        List<String> days = Arrays.asList("mon", "tues", "wed", "thurs", "fri");
        rate.setDaysList(days);

        Rate rate2 = new Rate();
        rate2.setTimes("600-2000");
        rate2.setPrice(new BigDecimal(2000));
        days = Arrays.asList("sat", "sun");
        rate2.setDaysList(days);

        rates.add(rate);
        rates.add(rate2);
        return rates;
    }



}
