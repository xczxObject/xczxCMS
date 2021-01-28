package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

public interface PageService {

    //分页查询
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    //新增页面
    public CmsPageResult add(CmsPage cmsPage);

    //根据页面id查询页面
    public CmsPage getById(String id);

    //修改页面
    public CmsPageResult update(String id,CmsPage cmsPage);

    //根据id删除页面
    public ResponseResult delete(String id);

    //页面静态化
    String getPageHtml(String pageId);

    //页面发布
    public ResponseResult postPage(String pageId);

    //保存html到GridFS
    public CmsPage saveHtml(String pageId,String content);

    //向mq 发送消息
    public void sendPostPage(String pageId);

    CmsPageResult save(CmsPage cmsPage);

    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
