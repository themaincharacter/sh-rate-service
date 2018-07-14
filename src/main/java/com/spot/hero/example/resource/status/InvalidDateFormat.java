package com.spot.hero.example.resource.status;

import javax.ws.rs.core.Response;

public class InvalidDateFormat implements Response.StatusType {
    private static final int STATUS_CODE = 400;

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }

    @Override
    public Response.Status.Family getFamily() {
        return Response.Status.Family.familyOf(STATUS_CODE);
    }

    @Override
    public String getReasonPhrase() {
        return "Invalid date format, must be of ISO-8601 date time format";
    }

}
