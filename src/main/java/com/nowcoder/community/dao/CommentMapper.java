package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Comment)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-11 09:22:10
 */
@Mapper
public interface CommentMapper {
    //查询返回Comment，进行分页
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    //查询数目的条目数
    int selectCountByEntity(int entityType, int entityId);

    //插入评论
    int insertComment(Comment comment);
}