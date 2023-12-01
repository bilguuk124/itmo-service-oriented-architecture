package itmo.soa.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class FlatSearchResponse {
    List<Flat> flats;
    int pageNumber;
}
