package org.lxp.mock.captor.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.lxp.mock.captor.CaptorModel;
import org.lxp.mock.captor.CaptorService;
import org.lxp.mock.captor.RealCaptorService;

public class RealCaptorServiceImpl implements RealCaptorService {
    private final CaptorService captorService;

    public RealCaptorServiceImpl(CaptorService captorService) {
        this.captorService = captorService;
    }

    @Override
    public void batchExecute(String propertyString, int propertyInt) {
        captorService.execute(Collections.singletonList(new CaptorModel(propertyString, propertyInt)));
    }

    @Override
    public void execute(String propertyString, int propertyInt) {
        captorService.execute(new CaptorModel(propertyString, propertyInt));
    }

    @Override
    public void execute(CaptorModel captorModel) {
        captorService.execute(captorModel);
    }

    @Override
    public void asyncExecute(CaptorModel captorModel) {
        Executors.newSingleThreadExecutor().submit(() -> captorService.execute(captorModel));
    }

    @Override
    public List<String> execute(int index) {
        List<String> rtn = new ArrayList<>(index);
        IntStream.range(1, index)
                .forEach(value -> rtn.add(captorService.execute(new CaptorModel(String.valueOf(value), value))));
        return rtn;
    }
}