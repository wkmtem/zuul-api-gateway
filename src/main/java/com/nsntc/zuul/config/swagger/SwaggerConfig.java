package com.nsntc.zuul.config.swagger;

import com.nsntc.zuul.config.yml.SwaggerYml;
import com.nsntc.zuul.constant.SwaggerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Class Name: SwaggerConfig
 * Package: com.nsntc.interview.config.swagger
 * Description: 
 * @author wkm
 * Create DateTime: 2018/1/4 下午3:35
 * Version: 1.0
 */
@SpringBootConfiguration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private SwaggerYml swaggerYml;

    /**
     * Method Name: swaggerSpringfoxDocket
     * Description: 主页面基本配置信息
     * Create DateTime: 2018/1/4 下午9:14
     * @return
     */
    @Bean
    public Docket swaggerSpringfoxDocket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(this.apiInfo());
    }

    /**
     * Method Name: uiConfig
     * Description: UI配置
     * Create DateTime: 2018/1/4 下午9:16
     * @return
     */
    @Bean
    public UiConfiguration uiConfig() {
        return new UiConfiguration(SwaggerConstant.UI_CFG_VALIDATOR_URL,
                                   SwaggerConstant.UI_CFG_DOC_EXPANSION,
                                   SwaggerConstant.UI_CFG_APIS_SORTER,
                                   SwaggerConstant.UI_CFG_DEFAULT_MODEL_RENDERING,
                                   UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
                                   SwaggerConstant.UI_CFG_JSON_EDITOR,
                                   SwaggerConstant.UI_CFG_SHOW_REQUEST_HEADERS,
                                   SwaggerConstant.UI_CFG_REQUEST_TIMEOUT);
    }

    /**
     * Method Name: addResourceHandlers
     * Description: 静态资源映射
     * Create DateTime: 2018/1/4 下午3:57
     * //@param registry 静态资源注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** spring boot */
        registry.addResourceHandler(com.nsntc.commons.constant.SwaggerConstant.RESOURCE_STATIC_PATTERN)
                .addResourceLocations(com.nsntc.commons.constant.SwaggerConstant.RESOURCE_STATIC_PATH);

        registry.addResourceHandler(com.nsntc.commons.constant.SwaggerConstant.RESOURCE_HTML_PATTERN)
                .addResourceLocations(com.nsntc.commons.constant.SwaggerConstant.RESOURCE_HTML_PATH);
        registry.addResourceHandler(com.nsntc.commons.constant.SwaggerConstant.RESOURCE_JARS_PATTERN)
                .addResourceLocations(com.nsntc.commons.constant.SwaggerConstant.RESOURCE_JARS_PATH);
        super.addResourceHandlers(registry);
    }

    /**
     * Method Name: apiInfo
     * Description:
     * Create DateTime: 2018/1/4 下午4:05
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(this.swaggerYml.getTitle())
                .description(this.swaggerYml.getDescription())
                .version(this.swaggerYml.getApiVersion())
                .termsOfServiceUrl(this.swaggerYml.getBaseUrl())
                .contact(new Contact(this.swaggerYml.getCreatedBy(),
                        this.swaggerYml.getUrl(),
                        this.swaggerYml.getEmail()))
                .license(this.swaggerYml.getLicense())
                .licenseUrl(this.swaggerYml.getLicenseUrl())
                .build();
    }
}
