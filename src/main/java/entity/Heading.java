package entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Heading {
    private final String type = "heading";
    private int depth;
    private String text;
}
