package com.lxp.notes.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxp.notes.exception.BaseException;
import com.lxp.notes.service.ExceptionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/二")
public class ExceptionController {
    static final String 捕获_RUNTIME_EXCEPTION = "捕获RuntimeException";
    @Inject
    private ExceptionService exceptionService;

    @ResponseBody
    @RequestMapping(value = "1+2-no", method = POST)
    @ApiOperation(value = 捕获_RUNTIME_EXCEPTION)
    public String _二_1_no(@ApiParam(value = "eg. [\"A\",\"B\"]", required = true) @RequestBody List<String> list,
            @RequestParam int index) {
        return exceptionService._二_1_no(list, index);
    }

    @ResponseBody
    @RequestMapping(value = "1+2-yes", method = POST)
    @ApiOperation(value = "禁止捕获RuntimeException", notes = "eg. IndexOutOfBoundsException 或者 NullPointerException，应该预检查")
    public String _二_1_yes(@ApiParam(value = "eg. [\"A\",\"B\"]", required = true) @RequestBody List<String> list,
            @RequestParam int index) {
        return exceptionService._二_1_yes(list, index);
    }

    @ResponseBody
    @RequestMapping(value = "3-yes", method = GET)
    @ApiOperation(value = "禁止对大段代码进行 try-catch", notes = "理解有所不同，大段代码需要拆分，这样就不会在大段代码做try-catch")
    public void _二_3_yes() {
    }

    @ResponseBody
    @RequestMapping(value = "4-yes", method = GET)
    @ApiOperation(value = "对捕获的异常负责", notes = "捕获的异常要处理，处理不了则继续抛出")
    public String _二_4_yes() {
        return exceptionService._二_4_yes();
    }

    @ResponseBody
    @RequestMapping(value = "5-yes", method = GET)
    @ApiOperation(value = "try-catch与事务", notes = "1.编程式事务需要手动回滚 <br/>2.若使用声明式事务，尽量不要捕捉与事务相关的异常")
    public void _二_5_yes() {
    }

    @ResponseBody
    @RequestMapping(value = "6-yes", method = GET)
    @ApiOperation(value = "finally块必须对资源对象、流对象进行关闭", notes = "重点是需要关闭")
    public void _二_6_yes() throws IOException {
        exceptionService._二_6_yes();
    }

    @ResponseBody
    @RequestMapping(value = "7-no", method = GET)
    @ApiOperation(value = "禁止在 finally 块中使用 return")
    public String _二_7_no() {
        return exceptionService._二_7_no(1);
    }

    @ResponseBody
    @RequestMapping(value = "8-no", method = GET)
    @ApiOperation(value = "尽量明确抛出异常的类型", notes = "1.不要抛出不需要的异常，更新代码时及时更新异常信息<br/>2.更加不要为方便只抛出Exception")
    public void _二_8_no() throws BaseException, IOException {
        exceptionService._二_8_no();
    }
}
