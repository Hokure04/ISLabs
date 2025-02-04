package is.fistlab.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationDto {
    private Integer x;
    private Long y;
    private Float z;
}
