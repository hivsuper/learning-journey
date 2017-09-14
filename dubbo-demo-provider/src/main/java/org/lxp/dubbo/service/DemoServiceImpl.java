package org.lxp.dubbo.service;

import java.util.Collections;
import java.util.List;

import org.lxp.dubbo.vo.DemoVo;

public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return String.format("Hello, %s", new DemoVo(name));
    }

    @Override
    public List<DemoVo> getDemoVo(List<String> names) {
        return Collections.emptyList();
    }
}
