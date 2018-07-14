package com.spot.hero.example;

import javax.ws.rs.core.Response;

import com.spot.hero.example.resource.status.InvalidDateFormat;
import com.spot.hero.example.resource.status.PriceUnavailable;
import com.spot.hero.example.service.RateService;
import com.spot.hero.example.model.Rate;
import com.spot.hero.example.resource.RateResource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateResourceTest {

    @Mock
    private RateService mockService;

    private RateResource target;

    @Before
    public void setUp() {
        target = new RateResource(mockService);
    }

    @Test
    public void addRate_givenValidRate_shouldRespondWithStatusOk() {
        Rate rate = new Rate();

        when(mockService.rateIsValid(any())).thenReturn(true);

        Response response = target.addRate(rate);
        assertEquals(response.getEntity(), rate);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

    }

    @Test
    public void addRate_givenInValidRate_shouldRespondWithStatusBadRequest() {
        Rate rate = new Rate();
        when(mockService.rateIsValid(any())).thenReturn(false);

        Response response = target.addRate(rate);
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void newRates_givenInValidRatesProvided_shouldRespondWithStatusBadRequest() {
        ArrayList<Rate> rates = new ArrayList<>();

        Response response = target.newRates(rates);
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void newRates_givenValidRatesProvided_shouldRespondWithStatusOk() {
        ArrayList<Rate> rates = new ArrayList<>();
        Rate rate = new Rate();
        rate.setTimes("600-1800");
        rate.setPrice(new BigDecimal(1500));
        List<String> days = Arrays.asList("mon", "tues", "wed", "thurs", "fri");
        rate.setDaysList(days);
        rates.add(rate);
        when(mockService.rateIsValid(any())).thenReturn(true);

        Response response = target.newRates(rates);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void newRates_givenRatesContainsInvalidRate_shouldRespondWithBadRequest() {
        ArrayList<Rate> rates = new ArrayList<>();
        Rate rate = new Rate();
        rates.add(rate);
        when(mockService.rateIsValid(any())).thenReturn(false);

        Response response = target.newRates(rates);
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void getRate_givenValidRange_shouldRespondWithOkResponse() {
        when(mockService.getRateFromStartEndDateTime(any(), any())).thenReturn(new BigDecimal(1.0));

        Response response = target.getRate("2015-07-01T07:00:00Z", "2015-07-01T18:00:00Z");
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void getRate_givenInvalidDateFormat_shouldRespondWithBadRequestResponse() {
        when(mockService.getRateFromStartEndDateTime(any(), any())).thenReturn(new BigDecimal(1.0));

        Response response = target.getRate("01/02/1932T02:33:99", "fake plastic trees");
        assertThat(response.getStatusInfo(), instanceOf(InvalidDateFormat.class));
    }

    @Test
    public void getRate_givenInvalidRange_shouldRespondWithPriceUnavailableResponse() {
        when(mockService.getRateFromStartEndDateTime(any(), any())).thenReturn(null);

        Response response = target.getRate("2015-07-01T07:00:00Z", "2015-07-01T20:00:00Z");
        assertThat(response.getStatusInfo(), instanceOf(PriceUnavailable.class));
    }


}
