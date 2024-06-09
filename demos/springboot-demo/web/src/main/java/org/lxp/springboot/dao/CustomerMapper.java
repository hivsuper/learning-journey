package org.lxp.springboot.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.lxp.springboot.dto.Customer;

import java.util.List;

public interface CustomerMapper {
    @Select("""
            <script>
            SELECT c.id, c.name, c.email, c.created_date
            FROM customer AS c
            WHERE c.id in <foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>#{id}</foreach>
            </script>
            """)
    @Results(id = "customerMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "email", column = "email"),
            @Result(property = "createdDate", column = "created_date")
    })
    List<Customer> findByIds(@Param("ids") List<Integer> ids);

    @Select("""
            SELECT c.id, c.name, c.email, c.created_date
            FROM customer AS c
            """)
    @ResultMap("customerMap")
    List<Customer> findAllCustomers();

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("""
            INSERT INTO customer(name, email, created_date)
            VALUES(#{name}, #{email}, #{createdDate})
            """)
    void add(Customer customer);
}