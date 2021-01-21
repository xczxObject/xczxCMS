package com.xuecheng.manage_cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@RequestMapping("/freemarker")
@Controller
public class FreemarkerTemplateController {
    @Resource
    RestTemplate restTemplate;

    @RequestMapping("/banner")
    public String tobanner(Map<String,Object> map){
        //使用resttemplate请求轮播图模型数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        //设置模型数据
        map.putAll(body);
        return "index_banner";
    }
}
