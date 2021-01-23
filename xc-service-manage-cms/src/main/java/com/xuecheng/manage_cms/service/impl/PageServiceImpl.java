package com.xuecheng.manage_cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.PageService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService{

    @Resource
    CmsPageRepository cmsPageRepository;
    @Resource
    RestTemplate restTemplate;
    @Resource
    CmsTemplateRepository cmsTemplateRepository;
    @Resource
    GridFsTemplate gridFsTemplate;
    @Resource
    GridFSBucket gridFSBucket;
    @Resource
    RabbitTemplate rabbitTemplate;

    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {

        //自定义条件查询
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        //自定义匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());//精准查询

        //条件值对象
        CmsPage cmsPage = new CmsPage();

        //设置条件值(站点Id)
        if(StringUtils.isNoneEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }

        //设置模板Id
        if(StringUtils.isNoneEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }

        //设置页面别名
        if(StringUtils.isNoneEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

        //设置页面Id
        if(StringUtils.isNoneEmpty(queryPageRequest.getPageId())){
            cmsPage.setPageId(queryPageRequest.getPageId());
        }

        //设置页面名称
        if(StringUtils.isNoneEmpty(queryPageRequest.getPageName())){
            cmsPage.setPageName(queryPageRequest.getPageName());
        }

        //定义条件对象
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        //分页参数
        if(page <=0){
            page = 1;
        }
        page = page -1;
        if(size<=0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);//自定义条件并分页
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    @Override
    public CmsPageResult add(CmsPage cmsPage) {
        //校验页面名称、站点Id、页面webpath的唯一性
        //调用dao新增页面
        //根据页面名称、站点Id、页面webpath去cms_page集合,如果查到说明此页面已经存在，如果查询不到再继续添加
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),cmsPage.getSiteId(),cmsPage.getPageWebPath());
        if(cmsPage1!=null){
            //页面已存在,抛出异常页面已存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
            cmsPage.setPageId(null);
            cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);

    }

    @Override
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            CmsPage cmsPage= optional.get();
            return cmsPage;
        }
        return null;
    }

    @Override
    public CmsPageResult update(String id, CmsPage cmsPage) {
        CmsPage cmsPage1 = this.getById(id);
        if(cmsPage1!=null){
            cmsPage1.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            cmsPage1.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            cmsPage1.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            cmsPage1.setPageName(cmsPage.getPageName());
            //更新访问路径
            cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新dataURL
            cmsPage1.setDataUrl(cmsPage.getDataUrl());
            cmsPageRepository.save(cmsPage1);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage1);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    @Override
    public ResponseResult delete(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Override
    public String getPageHtml(String pageId) {
        //获取数据模型
        Map model = getModelByPageId(pageId);
        if(model == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面模板
        String template = getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行静态化
        String html=generateHtml(template,model);
        return html;
    }

    //执行静态化
    private String generateHtml(String templateContent,Map model){
        //创建配置对象
        Configuration configuration=new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader=new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        //向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            Template template = configuration.getTemplate("template");
            //调用api静态化
             String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
             return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //获取页面模型数据
    private Map getModelByPageId(String pageId){
        //取出页面信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出页面的dataURL
        String dataUrl=cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            //页面dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //通过resttemplate请求dataURL获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl,Map.class);
        Map map = forEntity.getBody();
        return map;
    }
    //获取页面模板信息
    private String getTemplateByPageId(String pageId){
        //查询页面信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取页面模板id
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //从GridFs中取出模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream=gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource对象，获取流
            GridFsResource gridFsResource=new GridFsResource(gridFSFile,gridFSDownloadStream);
            //从流中取数据
            try {
                String val = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return val;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //页面发布
    @Override
    public ResponseResult postPage(String pageId) {
        //执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        //将页面静态化文件存储到GridFs中
        CmsPage cmsPage = saveHtml(pageId, pageHtml);
        //向MQ发信息
        sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //向mq 发送消息
    @Override
    public void sendPostPage(String pageId){
        //先得到页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Map<String,String> msg=new HashMap<>();
        msg.put("pageId",pageId);
        //转换json串
        String jsonString = JSON.toJSONString(msg);
        //发送给mq
        //站点id
        String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,jsonString);
    }

    //保存html到GridFS
    @Override
    public CmsPage saveHtml(String pageId,String content){
        //先得到页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        ObjectId objectId = null;
        try {
            //将htmlContent内容转成输入流
            InputStream inputStream = IOUtils.toInputStream(content, "utf-8");
            //保存html文件到GridFS
            objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将html文件id更新到CmsPage中
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }


}
