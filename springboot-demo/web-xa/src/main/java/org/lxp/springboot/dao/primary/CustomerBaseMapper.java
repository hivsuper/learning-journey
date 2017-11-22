package org.lxp.springboot.dao.primary;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.lxp.springboot.model.primary.CustomerBase;
import org.lxp.springboot.model.primary.CustomerBaseExample;

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

    @Insert("insert into test.customer (`name`, email, created_date) values (#{name,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, #{createdDate,jdbcType=DATE})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert2(CustomerBase customerBase);
}