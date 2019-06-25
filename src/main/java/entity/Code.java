package entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@Accessors(chain = true)
public class Code {
     private final static String type = "code";
     private String lang;
     private String text;

}




