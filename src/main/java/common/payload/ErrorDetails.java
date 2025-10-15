package common.payload;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data // Generates getters, setters, toString, equals, and hashCode
@Builder 
//For objects with multiple optional fields, the Builder pattern is a very clean and popular solution
public class ErrorDetails {

    private String message;
    private String details;

    // This field will only appear in the JSON response if it's not null.
    // Perfect for validation errors.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> validationErrors;

    
}