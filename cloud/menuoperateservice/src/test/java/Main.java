import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Long> list=new ArrayList();
        list.add(1L);
        list.add(2L);
        System.out.println(JSON.toJSON(list));
    }
}
