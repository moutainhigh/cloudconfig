import com.wjh.utils.http.HttpRequestUtil;

import java.util.Date;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Date());
        for (int i = 0; i <1000 ; i++) {
            String id=   HttpRequestUtil.sendGet("http://localhost:9999/id/generateId",new HashMap<String, String>(),new HashMap<String, String>());

        }
        System.out.println(new Date());

    }
}
