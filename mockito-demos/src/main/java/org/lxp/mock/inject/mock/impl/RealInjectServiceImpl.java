package org.lxp.mock.inject.mock.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.lxp.mock.inject.mock.InjectService;
import org.lxp.mock.inject.mock.RealInjectService;

@Named("realService")
public class RealInjectServiceImpl implements RealInjectService {
    @Inject
    private InjectService injectService;

    @Override
    public String getName() {
        return String.format("Real Name is %s", injectService.getName());
    }

}
