package com.spot.hero.example.resource.status;

import javax.ws.rs.core.Response;

public class PriceUnavailable implements Response.StatusType {
    private static final int STATUS_CODE = 404;

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
        return "Unavailable";
    }


}
