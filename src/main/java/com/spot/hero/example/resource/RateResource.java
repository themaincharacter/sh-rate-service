package com.spot.hero.example.resource;

import com.codahale.metrics.annotation.Timed;
import com.spot.hero.example.dto.PriceResponse;
import com.spot.hero.example.resource.status.InvalidDateFormat;
import com.spot.hero.example.resource.status.PriceUnavailable;
import com.spot.hero.example.service.RateService;
import com.spot.hero.example.model.Rate;
import io.swagger.annotations.Api;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


@Api
@Path("rate")
@Produces({"application/json", "application/xml"})
public class RateResource {

    private final RateService rateService;

    @Inject
    public RateResource(RateService rateService) {
        this.rateService = rateService;
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRate(Rate rate) {
        Response response;
        if(rateService.rateIsValid(rate)) {
            rateService.addRate(rate);
            response = Response.status(Response.Status.OK).entity(rate).build();
        } else {
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }
        return response;
    }

    @Path("/new")
    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newRates(List<Rate> rates) {
        if(rates == null || rates.isEmpty()) {
            System.out.println("null or empty");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        rateService.clearRates();
        for(Rate rate: rates) {
            if(rateService.rateIsValid(rate)) {
                rateService.addRate(rate);
            } else {
                System.out.println("invalid rate" + rate.toString());
                rateService.clearRates();
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        return Response.status(Response.Status.OK).entity(rates).build();
    }

    @GET
    @Timed
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response getRate(@QueryParam("start") String start, @QueryParam("end") String end) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        try{
            startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME);
            endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            InvalidDateFormat invalidDateFormat = new InvalidDateFormat();
            return Response.status(invalidDateFormat).entity(invalidDateFormat.getReasonPhrase()).build();
        }

        BigDecimal result = rateService.getRateFromStartEndDateTime(startDate,
                endDate);
        if(result == null) {
            PriceUnavailable unavailable = new PriceUnavailable();
            return Response.status(unavailable).entity(unavailable.getReasonPhrase()).build();
        } else {
            return Response.ok(new PriceResponse(result)).build();

        }

    }
}
