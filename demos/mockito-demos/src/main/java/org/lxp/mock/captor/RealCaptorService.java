package org.lxp.mock.captor;

import java.util.List;

public interface RealCaptorService {
    void batchExecute(String propertyString, int propertyInt);

    void execute(String propertyString, int propertyInt);

    void execute(CaptorModel captorModel);

    void asyncExecute(CaptorModel captorModel);

    List<String> execute(int index);
}
