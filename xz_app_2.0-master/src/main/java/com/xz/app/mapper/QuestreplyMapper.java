package com.xz.app.mapper;


import com.xz.app.pojo.Questreply;
import com.xz.app.pojo.QuestreplyExample;
import com.xz.app.pojo.Questreply;
import com.xz.app.pojo.QuestreplyExample;

import java.util.List;

public interface QuestreplyMapper {
    int deleteByPrimaryKey(Long replyid);

    int insert(Questreply record);

    int insertSelective(Questreply record);

    List<Questreply> selectByExampleWithBLOBs(QuestreplyExample example);

    List<Questreply> selectByExample(QuestreplyExample example);

    Questreply selectByPrimaryKey(Long replyid);

    int updateByPrimaryKeySelective(Questreply record);

    int updateByPrimaryKeyWithBLOBs(Questreply record);

    int updateByPrimaryKey(Questreply record);
}