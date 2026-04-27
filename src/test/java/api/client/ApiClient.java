package api.client;

import api.models.request.EntityRequest;
import api.models.response.EntityResponse;
import api.utils.JsonUtils;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.List;

public class ApiClient {

    private final static String BASE_URI = "http://localhost:8080";
    private final RequestSpecification baseSpecification = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Step("Send POST /api/create request")
    public ValidatableResponse create(EntityRequest entityRequest) {
        String requestBody = JsonUtils.pojoToJson(entityRequest);
        return RestAssured.given()
                .spec(baseSpecification)
                .basePath("/api/create/")
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Step("Send GET /api/getAll request ang get response")
    public List<EntityResponse> getAll() {
        return RestAssured.given()
                .spec(baseSpecification)
                .get("/api/getAll")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList("entity", EntityResponse.class);
    }

    @Step("Send GET api/get/{id} request and get response")
    public EntityResponse getById(String id) {
        ValidatableResponse validatableResponse = getById(id, HttpStatus.SC_OK);
        return validatableResponse.extract()
                .body()
                .as(EntityResponse.class);
    }

    @Step("Send GET api/get/{id} request and check status code {statusCode}")
    public ValidatableResponse getById(String id, int statusCode) {
        return RestAssured.given()
                .spec(baseSpecification)
                .pathParam("id", id)
                .get("/api/get/{id}")
                .then()
                .statusCode(statusCode);
    }

    @Step("Send DELETE /api/delete/{id} request and check status code {statusCode}")
    public void deleteById(String id, int statusCode) {
        deleteById(id)
                .then()
                .statusCode(statusCode);
    }

    @Step("Send DELETE /api/delete/{id} request")
    public Response deleteById(String id) {
        return RestAssured.given()
                .spec(baseSpecification)
                .pathParam("id", id)
                .delete("/api/delete/{id}");
    }

    @Step("Send PATCH /api/patch/{id} request")
    public void patch(String id, EntityRequest updatedEntity) {
        String requestBody = JsonUtils.pojoToJson(updatedEntity);
        RestAssured.given()
                .spec(baseSpecification)
                .body(requestBody)
                .when()
                .pathParam("id", id)
                .patch("/api/patch/{id}")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
