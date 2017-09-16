package org.lxp.dubbo.vo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * implement Serializable to avoid exception during data exchange  
 */
public class DemoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int index = 0;
    private int id;
    private String name;
    private Date date;

    public DemoVo(String name) {
        this(++index, name);
    }

    private DemoVo(int id, String name) {
        this.id = id;
        this.name = name;
        this.date = Calendar.getInstance().getTime();
    }

    public static int getIndex() {
        return index;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("DemoVo [id=%s, name=%s, date=%s]", id, name, date);
    }
}
