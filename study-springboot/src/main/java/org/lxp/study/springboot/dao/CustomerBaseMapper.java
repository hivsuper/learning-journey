package org.lxp.study.springboot.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.lxp.study.springboot.model.CustomerBase;
import org.lxp.study.springboot.model.CustomerBaseExample;

public interface CustomerBaseMapper {
    long countByExample(CustomerBaseExample example);

    int deleteByExample(CustomerBaseExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CustomerBase record);

    int insertSelective(CustomerBase record);

    List<CustomerBase> selectByExample(CustomerBaseExample example);

    CustomerBase selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CustomerBase record, @Param("example") CustomerBaseExample example);

    int updateByExample(@Param("record") CustomerBase record, @Param("example") CustomerBaseExample example);

    int updateByPrimaryKeySelective(CustomerBase record);

    int updateByPrimaryKey(CustomerBase record);
}