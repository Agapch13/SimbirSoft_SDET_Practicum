package api.tests;

import api.client.ApiClient;
import api.models.request.EntityRequest;
import api.models.request.EntityRequest.Addition;
import api.models.response.EntityResponse;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiTests {

    private final ApiClient apiClient = new ApiClient();
    private final EntityRequest entityRequest = new EntityRequest(
            new Addition("Info", 123), List.of(1, 2, 3, 4, 5), "Title", true);
    private String createdEntityId;

    @BeforeEach
    void setUp() {
        createdEntityId = apiClient.create(entityRequest)
                .extract()
                .body()
                .asString();
    }

    @AfterEach
    void tearDown() {
        apiClient.deleteById(createdEntityId);
    }

    @Test
    @DisplayName("Check entity creation via API")
    void createEntityTest() {
        EntityResponse expectedResponse = EntityResponse.create(createdEntityId, entityRequest);
        EntityResponse actualResponse = apiClient.getById(createdEntityId);

        assertNotNull(createdEntityId, "Id of the created entity should not be null");
        assertEquals(expectedResponse, actualResponse, "Expected entity does not match actual");
    }

    @Test
    @DisplayName("Check entity receiving by id via API")
    void getEntityByIdTest() {
        EntityResponse expectedResponse = EntityResponse.create(createdEntityId, entityRequest);
        EntityResponse actualResponse = apiClient.getById(createdEntityId);

        assertEquals(expectedResponse, actualResponse, "Expected entity does not match actual");
    }

    @Test
    @DisplayName("Check receiving all entities via API")
    void getAllEntitiesTest() {
        EntityResponse expectedEntity = EntityResponse.create(createdEntityId, entityRequest);
        List<EntityResponse> actualEntities = apiClient.getAll();

        assertTrue(!actualEntities.isEmpty(), "Response list should not be empty");
        assertEquals(List.of(expectedEntity), actualEntities, "Expected entities does not match actual");
    }

    @Test
    @DisplayName("Check entity removing via API")
    void deleteEntityByIdTest() {
        apiClient.deleteById(createdEntityId, HttpStatus.SC_NO_CONTENT);

        ValidatableResponse errorResponse = apiClient.getById(createdEntityId, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        errorResponse.body("error", Matchers.equalTo("no rows in result set"));
    }

    @Test
    @DisplayName("Check entity updating via API")
    void updateEntityTest() {
        entityRequest.setVerified(true);
        apiClient.patch(createdEntityId, entityRequest);

        EntityResponse actualResponse = apiClient.getById(createdEntityId);
        EntityResponse expectedResponse = EntityResponse.create(createdEntityId, entityRequest);
        assertEquals(expectedResponse, actualResponse, "Updated entityRequest does not match the expectedResponse one");
    }
}
