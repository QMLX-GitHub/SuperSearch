package com.qmlx.springbootinit;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qmlx.springbootinit.model.entity.Post;
import org.ehcache.shadow.org.terracotta.offheapstore.storage.PointerSize;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class FetchPostTest {

    @Test
    void getPostDate(){
        String json = "{\n" +
                "  \"current\": 2,\n" +
                "  \"pageSize\": 8,\n" +
                "  \"sortField\": \"createTime\",\n" +
                "  \"sortOrder\": \"descend\",\n" +
                "  \"category\": \"随笔\",\n" +
                "  \"tags\": [\n" +
                "    \"随笔\"\n" +
                "  ]\n" +
                "}" ;
        String result = HttpRequest.post("https://api.code-nav.cn/api/post/search/page/vo")
                .body(json)
                .execute().
                body();
        //针对结果进行解析
        Map<String,Object> map= JSONUtil.toBean(result, Map.class);
        System.out.println(map);
        JSONObject data = (JSONObject)map.get("data");
        JSONArray records = (JSONArray)data.get("records");
        ArrayList<Post> postList = new ArrayList<>();
        for(Object item:records){
            JSONObject tempRecord=(JSONObject) item;
            Post post = new Post();
            post.setTitle(tempRecord.getStr(tempRecord.getStr("title")));
            post.setContent(tempRecord.getStr(tempRecord.getStr("content")));
            JSONArray tag = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tag.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            String userId = tempRecord.getStr("userId");
            post.setUserId(Long.valueOf(userId));
            System.out.println(post);
            postList.add(post);
        }
        System.out.println(postList);
        System.out.println(result);

    }
}
