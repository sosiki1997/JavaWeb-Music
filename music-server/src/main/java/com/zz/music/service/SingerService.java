package com.zz.music.service;

import com.zz.music.domain.Singer;

import java.util.List;

/**
 * 歌手
 */
public interface SingerService {
    /**
     * 增加：是否增加成功，返回boolean即可
     */
    public boolean insert(Singer singer);
    /**
     * 修改：是否增加成功，返回boolean即可
     */
    public boolean update(Singer singer);
    /**
     * 删除：是否增加成功，返回boolean即可
     */
    public boolean delete(Integer id);
    /**
     * 根据主键查询整个对象
     */
    public Singer selectByPrimaryKey(Integer id);
    /**
     * 查询所有歌手
     */
    public List<Singer> allSinger();
    /**
     * 根据歌手名字模糊查询列表
     */
    public List<Singer> singerOfName(String name);
    /**
     * 根据性别查询
     */
    public List<Singer> singerOfSex(Integer sex);
}
