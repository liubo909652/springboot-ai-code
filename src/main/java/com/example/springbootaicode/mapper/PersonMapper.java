package com.example.springbootaicode.mapper;

import com.example.springbootaicode.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PersonMapper {

    Person selectById(@Param("id") Long id);
}
