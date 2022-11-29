package com.zz.music.service;

import com.zz.music.domain.Comment;

import java.util.List;

/**
 * 评论service接口
 */
public interface CommentService {
    /**
     * 增加
     */
    public boolean insert(Comment comment);
    /**
     * 修改
     */
    public boolean update(Comment comment);
    /**
     * 删除
     */
    public boolean delete(Integer id);
    /**
     * 根据主键查询整个对象
     */
    public Comment selectByPrimaryKey(Integer id);
    /**
     * 查询所有评论
     */
    public List<Comment> allComment();
    /**
     * 根据歌曲id查询评论
     */
    public List<Comment> commentOfSongId(Integer songId);
    /**
     * 根据歌单id查询评论
     */
    public List<Comment> commentOfSongListId(Integer songListId);
}
