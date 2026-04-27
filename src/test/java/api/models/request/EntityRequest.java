package api.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityRequest {

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
    }
}
