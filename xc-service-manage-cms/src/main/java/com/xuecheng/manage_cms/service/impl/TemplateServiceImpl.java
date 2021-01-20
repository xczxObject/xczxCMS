package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.TemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Resource
    CmsTemplateRepository cmsTemplateRepository;

    @Override
    public QueryResponseResult findList() {
        List<CmsTemplate> list = cmsTemplateRepository.findAll();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(list);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}

//憨批李壮