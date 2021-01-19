package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.SiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value="cms站点管理接口",description = "cms站点管理接口，提供页面的站点内容")
@RestController
@RequestMapping("/cms/site")
public class CmsSiteController {

    @Resource
    SiteService siteService;

    @GetMapping("/list")
    @ApiOperation("查询所有站点信息")
    public QueryResponseResult findlist(){
        return siteService.findList();
    }
}
