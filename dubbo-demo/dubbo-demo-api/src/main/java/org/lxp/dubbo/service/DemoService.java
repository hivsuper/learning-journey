package org.lxp.dubbo.service;

import java.util.List;

import org.lxp.dubbo.vo.DemoVo;

public interface DemoService {
    public String sayHello(String name);

    public List<DemoVo> getDemoVo(List<String> names);
}
