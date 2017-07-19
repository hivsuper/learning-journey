package org.lxp.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MyRequest {
    private String uri;
    private Map<String, List<String>> params;

    public MyRequest(String uri, Map<String, List<String>> params) {
        this.uri = uri;
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, List<String>> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return String.format("MyRequest [uri=%s, params=%s]", uri, printMap());
    }

    private String printMap() {
        String rtn = null;
        if (params != null) {
            List<String> t = new ArrayList<>(params.size());
            for (Entry<String, List<String>> entry : params.entrySet()) {
                t.add(entry.getKey() + "=" + entry.getValue().toString());
            }
            rtn = t.toString();
        }
        return rtn;
    }
}
