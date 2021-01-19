package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.TemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value="cms模版管理接口",description = "cms模版管理接口，提供页面的模块内容")
@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController {

    @Resource
    TemplateService templateService;

    @GetMapping("/list")
    @ApiOperation("查询所有模版信息")
    public QueryResponseResult findList(){
        return templateService.findList();
    }
}
