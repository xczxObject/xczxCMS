package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.stereotype.Service;


public interface ConfigService {
    //根据ID查询配置管理信息
    CmsConfig getConfigById(String id);
}
