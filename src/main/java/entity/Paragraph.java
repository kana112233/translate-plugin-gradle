package entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Paragraph {
    private final static String type = "paragraph";
    private boolean pre;
    private String text;


}
