package com.nsntc.zuul.swagger;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: DocumentationConfig
 * Package: com.nsntc.zuul.swagger
 * Description:
 * @author wkm
 * Create DateTime: 2018/1/4 下午8:41
 * Version: 1.0
 */
@Primary
@SpringBootConfiguration
public class DocumentationConfig implements SwaggerResourcesProvider {

    /**
     * Method Name: get
     * Description: 获取资源路径
     * Create DateTime: 2018/1/4 下午9:12
     * @return
     */
    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList(4);
        resources.add(swaggerResource("注册登录系统", "/sso/v2/api-docs", "1.0"));
        resources.add(swaggerResource("菜单权限系统", "/sys/v2/api-docs", "1.0"));
        return resources;
    }

    /**
     * Method Name: swaggerResource
     * Description:
     * Create DateTime: 2018/1/4 下午9:12
     * @param name
     * @param location
     * @param version
     * @return
     */
    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
