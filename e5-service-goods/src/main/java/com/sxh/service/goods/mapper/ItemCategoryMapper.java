package com.sxh.service.goods.mapper;

import com.sxh.service.goods.pojo.ItemCategory;
import com.sxh.service.goods.pojo.ItemCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ItemCategoryMapper {
    int countByExample(ItemCategoryExample example);

    int deleteByExample(ItemCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ItemCategory record);

    int insertSelective(ItemCategory record);

    List<ItemCategory> selectByExample(ItemCategoryExample example);

    ItemCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ItemCategory record, @Param("example") ItemCategoryExample example);

    int updateByExample(@Param("record") ItemCategory record, @Param("example") ItemCategoryExample example);

    int updateByPrimaryKeySelective(ItemCategory record);

    int updateByPrimaryKey(ItemCategory record);
}