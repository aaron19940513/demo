package com.demo.mapper;

import com.demo.DBEnum;
import com.demo.anno.DynamicTable;
import com.demo.entity.Coffee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemoMapper {

    public List<Coffee> selectAllCoffee(@DynamicTable DBEnum dbEnum, String a, String b);

    public Coffee selectOneCoffee(@Param("coffeeId") int coffeeId);
}
