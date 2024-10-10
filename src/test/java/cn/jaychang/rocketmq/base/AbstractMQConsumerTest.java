package cn.jaychang.rocketmq.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import cn.jaychang.rocketmq.gson.CustomDateAdapter;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class AbstractMQConsumerTest {
    @Test
    public void parseMessage() {
        CustomDateAdapter customDateAdapter = new CustomDateAdapter();


        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())  // 默认适配器
                .registerTypeAdapter(Date.class, customDateAdapter)
                .create();

        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        System.out.println(dateTimeInstance.format(new Date()));

        String json1 = "{\"username\":\"jaychang\",\"startTime\":\"2022-12-19 16:04:31\",\"endTime\":\"2023-12-19 16:04:31\"}";
        A a1 = gson.fromJson(json1, A.class);
        System.out.println(a1);

        String json2 = "{\"username\":\"jaychang\",\"startTime\":\"Mar 2, 2023 5:39:30 PM\",\"endTime\":\"Mar 22, 2023 5:39:30 PM\"}";
        A a2 = gson.fromJson(json2, A.class);
        System.out.println(a2);

        String json3 = "{\"username\":\"jaychang\",\"startTime\":\"Mar 2, 2023 5:39:30 PM\",\"endTime\":\"2022-12-19 16:04:31\"}";
        A a3 = gson.fromJson(json3, A.class);
        System.out.println(a3);

    }

    static class A {
        private Date startTime;
        private Date endTime;
        private String username;

        @Override
        public String toString() {
            return "A{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", username='" + username + '\'' +
                    '}';
        }
    }
}