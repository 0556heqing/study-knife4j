package com.heqing.knife4j.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.heqing.knife4j.model.User;
import com.heqing.knife4j.util.ResultUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author heqing
 * @date 2021/8/16 19:14
 * 学习资料
 * http://www.mianshigee.com/tutorial/knife4j/
 * https://book.itxueyuan.com/962W/G7GRG
 *
 * @Api：用在类上，说明该类的作用。
 *
 * @ApiOperation：注解来给API增加方法说明。
 *
 * @ApiImplicitParams : 用在方法上包含一组参数说明。
 *
 * @ApiImplicitParam：用来注解来给方法入参增加说明。
 * 参数：
 * ·paramType：指定参数放在哪个地方
 * ··header：请求参数放置于Request Header，使用@RequestHeader获取
 * ··query：请求参数放置于请求地址，使用@RequestParam获取
 * ··path：（用于restful接口）-->请求参数的获取：@PathVariable
 * ··body：（不常用）
 * ··form（不常用）
 * ·name：参数名
 * ·dataType：参数类型
 * ·required：参数是否必须传(true | false)
 * ·value：说明参数的意思
 * ·defaultValue：参数的默认值
 *
 * @ApiResponses：用于表示一组响应
 *
 * @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
 * ——code：数字，例如400
 * ——message：信息，例如"请求参数异常!"
 * ——response：抛出异常的类
 *
 * @ApiModel：描述一个Model的信息（一般用在请求参数无法使用@ApiImplicitParam注解进行描述的时候）
 * 参数：
 * ·value–为模型提供备用名称
 * ·description–提供详细的类描述
 * ·parent–为模型提供父类以允许描述继承关系
 * ·discriminatory–支持模型继承和多态，使用鉴别器的字段的名称，可以断言需要使用哪个子类型
 * ·subTypes–从此模型继承的子类型数组
 * ·reference–指定对应类型定义的引用，覆盖指定的任何其他元数据
 *
 * @ApiModelProperty：描述一个model的属性
 * 参数：
 * ·value–字段说明
 * ·name–重写属性名字
 * ·dataType–重写属性类型
 * ·required–是否必填
 * ·example–举例说明
 * ·hidden–隐藏
 *
 * @ApiOperationSupport(order=1)：排序位置
 */
@Api(tags = "示例 - 普通入参", description = "用来演示Swagger的一些注解")
@RestController
@RequestMapping("/knife4j")
public class Knife4jController {

    private  final Logger log = LoggerFactory.getLogger(Knife4jController.class);

    @GetMapping("/index")
    public String knife4j(){
        log.info("Controller --> hello knife4j");
        return "hello knife4j";
    }


    @ApiOperationSupport(order=1)
    @ApiOperation(value = "示例 - 返回结果", notes = "演示 入参：无，出参：user")
    @ApiResponses( value = {
        @ApiResponse(code = 0, message = "SUCCESS"),
        @ApiResponse(code = -1, message = "ERROR")
    })
    @GetMapping("/response")
    public ResultUtil<User> response(){
        log.info("---------- response ----------");

        User user = new User();
        user.setName("heqing");
        user.setAge(30);
        user.setAddress("安庆市");
        user.setCreateTime(new Date());

        return  ResultUtil.buildSuccess(user);
    }


    @ApiOperation(value="示例 - PathVariable", notes = "演示 @PathVariable 注解")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType ="path", name = "id", value = "id", dataType = "String", defaultValue = "1"),
    })
    @GetMapping("/pathVariable/{id}")
    public ResultUtil pathVariable(@PathVariable("id") String id) {
        log.info("pathVariable ---> id = "+id);
        return ResultUtil.buildSuccess();
    }


    @ApiOperation(value="示例 - requestParam", notes = "演示 @RequestParam 注解")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType ="query", name = "name", value = "名字", required = true, dataType = "String", defaultValue ="heqing", allowableValues="heqing,shiyan"),
        @ApiImplicitParam(paramType ="query", name = "age", value = "年龄", dataType = "Integer", defaultValue ="30", allowableValues="range[1,5]")
    })
    @PostMapping("/requestParam")
    public ResultUtil requestParam(@RequestParam(value="name", defaultValue="heqing") String name, @RequestParam("age") Integer age){
        log.info("requestParam ---> name = "+name+", age = "+age);
        return ResultUtil.buildSuccess();
    }


    @ApiOperation(value="示例 - request", notes = "演示@PathVariable、@RequestParam、@RequestBody组合入参")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType ="path", name = "id", value = "id", dataType = "Long", defaultValue = "1"),
        @ApiImplicitParam(paramType ="query", name = "code", value = "code", dataType = "String", defaultValue = "c1")
    })
    @PostMapping("/request/{id}")
    public ResultUtil request(@PathVariable("id") Long id, @RequestParam("code") String code, @RequestBody User user){
        log.info("request ---> id = "+id+", code = "+code+", user = "+JSONObject.toJSONString(user));
        return ResultUtil.buildSuccess();
    }


    @ApiOperation(value = "示例 - requestList", notes = "演示入参 List")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType ="query", name = "idList", value = "id列表", defaultValue = "1,2,3")
    })
    @PostMapping("/requestList")
    public ResultUtil requestList(@RequestParam(value = "idList") List<Long> idList, @RequestBody List<User> userList){
        log.info("requestBody ---> idList = "+ JSONObject.toJSONString(idList) +", userList = "+ JSONObject.toJSONString(userList));
        return ResultUtil.buildSuccess();
    }


    @ApiOperation(value = "示例 - requestMap", notes = "演示入参 Map")
    @PostMapping("/requestMap")
    public ResultUtil requestMap(@RequestBody Map<String, User> userMap){
        log.info("requestBody ---> usermap = "+ JSONObject.toJSONString(userMap));
        return ResultUtil.buildSuccess();
    }

}
