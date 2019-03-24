package ru.iteranet;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class RESTTest {

    @Test
    public void testTest(){
        String URL = "http://localhost:8080/city/1";

        given()
                .when()
                .get(URL)
                .then()
                .statusCode(200)
                .body("name", equalTo("Smolensk1"));
//        System.out.println("Test");
    }

    @Test
    public void canCreateCity() throws JSONException {

        JSONObject jsonObject0 = new JSONObject()
                .put( "name", "Russia2");

        JSONObject jsonObject = new JSONObject()
                .put("name", "Smolensk2")
                .put("country", jsonObject0);

        String testURL = "http://localhost:8080/city";

        given()
                .contentType("application/json")  //another way to specify content type
                .body(jsonObject.toString())   // use jsonObj toString method
                .when()
                .post(testURL)
                .then()
                .statusCode(201);
//                .assertThat()
//                .body("message", equalTo("{\"resultMessage\":\"Message accepted\"}"));



//        String URL = "http://localhost:8080/city/{\"name\":\"Smolensk2\", \"country\":{ \"name\":\"Russia\"}}\"";
//
//        given()
//                .when()
//                .post(URL)
//                .then()
//                .statusCode(200)
//                .body("name", equalTo("Smolensk2"));
////        System.out.println("Test");
    }

}
