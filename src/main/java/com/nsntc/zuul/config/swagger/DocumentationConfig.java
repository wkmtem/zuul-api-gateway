package com.nsntc.zuul.config.swagger;

import com.nsntc.zuul.config.yml.SwaggerYml;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SwaggerYml swaggerYml;

    /**
     * Method Name: get
     * Description: 获取资源路径
     * Create DateTime: 2018/1/4 下午9:12
     * @return
     */
    @Override
    public List<SwaggerResource> get() {
        String names = this.swaggerYml.getResource().get("names");
        String locations = this.swaggerYml.getResource().get("locations");
        String versions = this.swaggerYml.getResource().get("versions");

        List<SwaggerResource> resources = null;
        String[] nameArr = null;
        String[] locationArr = null;
        String[] versionArr = null;
        if (StringUtils.isNotEmpty(names)) {
            nameArr = StringUtils.split(names, ',');
            locationArr = StringUtils.split(locations, ',');
            versionArr = StringUtils.split(versions, ',');
        }
        if (null != nameArr && nameArr.length > 0) {
            resources = new ArrayList<>(nameArr.length);
            for (int i = 0; i < nameArr.length; i ++) {
                resources.add(swaggerResource(StringUtils.trim(nameArr[i]),
                        StringUtils.trim(locationArr[i]), StringUtils.trim(versionArr[i])));
            }
        }
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
