package com.restaurant.restaurant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

@RunWith(SpringRunner.class)
//@SpringBootTest
@Ignore
public class RestaurantApplicationTests {

    private static final int PORT = 8090;
    private static final String addr = "http://localhost:" + PORT;
    private HttpClient httpClient;
    private static final String testUser = "rest@test.com";
    private static final String testPass = "1";


    @Before
    public void init() throws IOException {
        httpClient = HttpClientBuilder.create().build();
    }

    @Test
    public void contextLoads() throws IOException {

        URI uri = URI.create(addr + "/rest/authority?login=" + testUser + "&password=" + testPass);
        HttpGet getRequest = new HttpGet(uri);

        String token = EntityUtils.toString(httpClient.execute(getRequest).getEntity());

        System.out.println(token);

        uri = URI.create(addr + "/web/create/client");
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Authorization", "Bearer " + token);
        String content = "{\n" +
                "    name: \"Вася\",\n" +
                "    phone: \"+79102812664\",\n" +
                "    vip: true,\n" +
                "    tags: [],\n" +
                "    newtags: [\"Лучший клиент\", \"Красное вино\"]\n" +
                "}";
        HttpEntity httpEntity = new BasicHttpEntity();

        ((BasicHttpEntity) httpEntity).setContent(new ByteArrayInputStream(content.getBytes()));
        httpPost.setEntity(httpEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        System.out.println("client_id: " +  EntityUtils.toString(httpResponse.getEntity()));

    }

}
