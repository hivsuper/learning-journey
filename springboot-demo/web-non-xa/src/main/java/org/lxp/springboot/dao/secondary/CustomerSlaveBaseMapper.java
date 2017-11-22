package org.lxp.springboot.dao.secondary;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.lxp.springboot.model.secondary.CustomerSlaveBase;
import org.lxp.springboot.model.secondary.CustomerSlaveBaseExample;

public interface CustomerSlaveBaseMapper {
    long countByExample(CustomerSlaveBaseExample example);

    int deleteByExample(CustomerSlaveBaseExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CustomerSlaveBase record);

    int insertSelective(CustomerSlaveBase record);

    List<CustomerSlaveBase> selectByExample(CustomerSlaveBaseExample example);

    CustomerSlaveBase selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CustomerSlaveBase record, @Param("example") CustomerSlaveBaseExample example);

    int updateByExample(@Param("record") CustomerSlaveBase record, @Param("example") CustomerSlaveBaseExample example);

    int updateByPrimaryKeySelective(CustomerSlaveBase record);

    int updateByPrimaryKey(CustomerSlaveBase record);
}