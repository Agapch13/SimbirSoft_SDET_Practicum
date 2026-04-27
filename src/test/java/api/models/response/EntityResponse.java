package api.models.response;

import api.models.request.EntityRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("addition")
    private Addition addition;

    @JsonProperty("important_numbers")
    private List<Integer> importantNumbers;

    @JsonProperty("title")
    private String title;

    @JsonProperty("verified")
    private Boolean verified;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Addition {

        @JsonProperty("additional_info")
        private String additionalInfo;

        @JsonProperty("additional_number")
        private Integer additionalNumber;

        @JsonProperty("id")
        private Integer id;
    }

    private EntityResponse(int id, EntityRequest entityRequest) {
        EntityRequest.Addition requestAddition = entityRequest.getAddition();
        this.addition = new Addition(
                requestAddition.getAdditionalInfo(),
                requestAddition.getAdditionalNumber(),
                id);
        this.id = id;
        this.importantNumbers = entityRequest.getImportantNumbers();
        this.title = entityRequest.getTitle();
        this.verified = entityRequest.getVerified();
    }

    public static EntityResponse create(String id, EntityRequest entityRequest) {
        return new EntityResponse(Integer.parseInt(id), entityRequest);
    }
}
