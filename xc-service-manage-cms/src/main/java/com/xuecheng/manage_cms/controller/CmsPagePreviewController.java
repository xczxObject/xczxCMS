package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

@Controller
public class CmsPagePreviewController extends BaseController {
    @Resource
    PageService pageService;

    //页面预览
    @RequestMapping(value = "/cms/preview/{pageId}",method = RequestMethod.GET)
    public void preview(@PathVariable("pageId") String pageId) throws Exception {
        String html = pageService.getPageHtml(pageId);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(html.getBytes("utf-8"));
    }
}
