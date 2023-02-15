package com.qizy.mapper;


import com.qizy.mysql.model.StoreLbsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Type:CorpApiMapper
 * @Desc:
 * @author: kjw
 * @date: 2022/3/17 3:05 下午
 */
@Mapper
public interface StoreLbsMapper {


    void batchInsertOrUpdate(@Param("saveList") List<StoreLbsModel> saveList);

    List<StoreLbsModel> queryBetween(@Param("start") int start, @Param("end") int end);

    List<Integer> getByIds(@Param("ids") List<Integer> ids);
}