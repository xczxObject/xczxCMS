package com.xuecheng.manage_cms;

import com.xuecheng.manage_cms.service.PageService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {
    @Resource
    PageService pageService;

    public void testGetPageHtml(){
        pageService.getPageHtml("");
    }
}
