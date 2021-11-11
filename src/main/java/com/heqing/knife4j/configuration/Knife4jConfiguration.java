package com.heqing.knife4j.configuration;

import com.heqing.knife4j.util.ErrorEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author heqing
 * @date 2021/8/16 19:14
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        List<ResponseMessage> respMsgList = respMsgList();
        return new Docket(DocumentationType.SWAGGER_2)
                // 全局请求参数
                .globalOperationParameters(getGlobalOperationParameters())
                // 添加全局响应状态码
                .globalResponseMessage(RequestMethod.GET, respMsgList)
                .globalResponseMessage(RequestMethod.POST, respMsgList)
                .globalResponseMessage(RequestMethod.PUT, respMsgList)
                .globalResponseMessage(RequestMethod.DELETE, respMsgList)
                // api信息
                .apiInfo(apiInfo())
                //分组名称
                .groupName("1.X版本")
                .select()
//                // 指定Controller扫描包路径
//                .apis(RequestHandlerSelectors.basePackage("com.heqing.controller"))
                 // 仅扫描方法上有ApiOperation的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any()).build();
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/doc.html#/home
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("Knife4j使用示例")
                // 简介
                .description("源码路径：https://github.com/0556heqing/study-knife4j")
                // 版本
                .version("1.0")
                // 服务Url
                .termsOfServiceUrl("https://github.com/0556heqing/study-knife4j").build();
    }

    /**
     * 公共入参信息，如 token、system、channel ...
     * @return
     */
    private List<Parameter> getGlobalOperationParameters() {
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name("token").description("认证Token").defaultValue("test")
                .modelRef(new ModelRef("string")).parameterType("header").required(true);
        pars.add(parameterBuilder.build());
        parameterBuilder.name("system").description("请求系统（pc/h5/app）").defaultValue("pc")
                .allowableValues(new AllowableListValues(Arrays.asList("pc","h5","app"), "string"))
                .modelRef(new ModelRef("string")).parameterType("cookie").required(false);
        pars.add(parameterBuilder.build());
        return pars;
    }

    /**
     * 全局返回码
     * @return
     */
    private List<ResponseMessage> respMsgList() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        Arrays.stream(ErrorEnum.values()).forEach(errorEnum -> {
            responseMessageList.add(
                    new ResponseMessageBuilder()
                            .code(errorEnum.getCode())
                            .message(errorEnum.getMsg())
                            .responseModel(new ModelRef(errorEnum.getMsg()))
                            .build()
            );
        });
        return responseMessageList;
    }
}