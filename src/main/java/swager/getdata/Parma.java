package swager.getdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parma {
    private String name;
    private String type;
    private String isneed="Y";
    private String description="";
    private String defaultstr="空";
}
