package com.spot.hero.example;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class RateIntegrationTest {

    @BeforeClass
    public static void initTest() throws Exception {
        RestAssured.baseURI = "http://localhost:8080/spothero/rate";
        RestAssured.port = 8080;
        EmbeddedServer.startIfRequired();

    }

    @AfterClass
    public static void stopServer() throws Exception {
        EmbeddedServer.stop();
    }

    @Test
    public void new_post_returns_200_with_expected_days_and_price() {
        given()
                .contentType(ContentType.JSON)
                .body(getRatesJson())
                .post("/new").
                then()
                .statusCode(200)
                .assertThat()
                .body("days", hasItem("mon,tues,wed,thurs,fri"))
                .body("days", hasItem("sat,sun"))
                .body("price", hasItem(1500))
                .body("price", hasItem(2000))
        ;
    }

    @Test
    public void post_returns_200_with_expected_days_and_price() {
        given()
                .contentType(ContentType.JSON)
                .body(getRateJson())
                .post().
                then()
                .statusCode(200)
                .assertThat()
                .body("days", is("sat"))
                .body("price", is(300))
        ;
    }

    @Test
    public void get_returns_200_with_expected_rate_price() {
        given()
                .contentType(ContentType.JSON)
                .body(getRatesJson())
                .post("/new");

        given()
                .contentType(ContentType.JSON)
                .param("start","2015-07-01T07:00:00Z")
                .param("end", "2015-07-01T18:00:00Z")
                .get().
                then()
                .statusCode(200)
                .assertThat()
                .body("price", is(1500))
        ;
    }

    private String getRatesJson() {
        return "[\n" +
                "    {\n" +
                "      \"days\": \"mon,tues,wed,thurs,fri\",\n" +
                "      \"times\": \"0600-1800\",\n" +
                "      \"price\": 1500\n" +
                "    },\n" +
                "    {\n" +
                "      \"days\": \"sat,sun\",\n" +
                "      \"times\": \"0600-2000\",\n" +
                "      \"price\": 2000\n" +
                "    }\n" +
                "  ]";
    }

    private String getRateJson() {
        return "{\n" +
                "      \"days\": \"sat\",\n" +
                "      \"times\": \"0200-0300\",\n" +
                "      \"price\": 300\n" +
                "    }";
    }

}
