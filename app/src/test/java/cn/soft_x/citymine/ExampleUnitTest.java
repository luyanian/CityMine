package cn.soft_x.citymine;

import org.junit.Test;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    Map<String, String> map = new HashMap<>();

    @Test
    public void addition_isCorrect() throws Exception {
//        initMap();

//        mapToString();
//        foreachMap();
        //        assertEquals(4, 2 + 2);


        SnCal snCal = new SnCal();

        // 计算sn跟参数对出现顺序有关，get请求请使用LinkedHashMap保存<key,value>，该方法根据key的插入顺序排序；
        // post请使用TreeMap保存<key,value>，该方法会自动将key按照字母a-z顺序排序。
        // 所以get请求可自定义参数顺序（sn参数必须在最后）发送请求，但是post请求必须按照字母a-z顺序填充body（sn参数必须在最后）。
        // 以get请求为例：http://api.map.baidu.com/geocoder/v2/?address=百度大厦&output=json&ak=yourak，paramsMap中先放入address，
        // 再放output，然后放ak，放入顺序必须跟get请求中对应参数的出现顺序保持一致。
        Map paramsMap = new LinkedHashMap<String, String>();
        paramsMap.put("address", "百度大厦");
        paramsMap.put("output", "json");
        paramsMap.put("ak", "yourak");

        // 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，
        // 拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
        String paramsStr = snCal.toQueryString(paramsMap);

        // 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
        String wholeStr = new String("/geocoder/v2/?" + paramsStr + "yoursk");

        // 对上面wholeStr再作utf8编码
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");

        // 调用下面的MD5方法得到最后的sn签名7de5a22212ffaa9e326444c75a58f9a0
        System.out.println(snCal.MD5(tempStr));


    }

    public void mapToString() {
        System.out.print(map.toString()+"\n\n");
    }

    public void foreachMap() {
        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> s :
                set) {
            System.out.println(s.toString());
        }
    }

    private void initMap() {
        map.put("userid", "123");
        map.put("email", "aaa@aaa.com");
        map.put("name","doubi");
    }
}