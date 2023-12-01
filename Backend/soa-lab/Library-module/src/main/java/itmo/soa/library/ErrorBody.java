package itmo.soa.library;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorBody {
    private int errorCode;
    private String message;
    private String details;
    private LocalDateTime timestamp;
}
