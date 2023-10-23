package org.lxp.mock.captor;

import java.util.List;

public interface CaptorService {
    void execute(List<CaptorModel> models);

    String execute(CaptorModel model);
}
