package org.lxp.dubbo.service;

import java.util.ArrayList;
import java.util.List;

import org.lxp.dubbo.vo.DemoVo;

public class DemoServiceImpl2 implements DemoService {

    @Override
    public String sayHello(String name) {
        return null;
    }

    @Override
    public List<DemoVo> getDemoVo(List<String> names) {
        List<DemoVo> demoVos = new ArrayList<>(names.size());
        names.forEach(name -> {
            demoVos.add(new DemoVo(name));
        });
        return demoVos;
    }
}
