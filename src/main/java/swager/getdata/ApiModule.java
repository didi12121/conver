package swager.getdata;
import lombok.Data;

import java.util.List;
import java.util.Random;

@Data
public class ApiModule {

    private String num;

    private String name;

    private String url;

    private String type;

    private String des;

    private String re;

    private List<Parma> Parmas;

    public String getNum() {
        return new Random().nextInt(20)+"";
    }
}
