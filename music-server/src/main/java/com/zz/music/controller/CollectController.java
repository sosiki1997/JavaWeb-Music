package com.zz.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.zz.music.domain.Collect;
import com.zz.music.service.CollectService;
import com.zz.music.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 收藏控制类
 */
@RestController
@RequestMapping("/collect")
public class CollectController {
    /* 返回json格式的数据给前端 */
    @Autowired
    private CollectService collectService;
    /**
     * 添加收藏
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addCollect(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        /* 获取前端传来的数据 */
        String userId = request.getParameter("userId");          //用户id
        String type = request.getParameter("type");              //收藏类型
        String songId = request.getParameter("songId");          //歌曲id
        if(songId==null||songId.equals("")){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "收藏的歌曲不存在");
            return jsonObject;
        }
        if(collectService.existSongId(Integer.parseInt(userId),Integer.parseInt(songId))){
            jsonObject.put(Consts.CODE, 2);
            jsonObject.put(Consts.MSG, "歌曲已收藏");
            return jsonObject;
        }

        /* 保存到收藏的对象中，把数据存入数据库中 */
        Collect collect = new Collect();
        collect.setUserId(Integer.parseInt(userId));
        collect.setType(new Byte(type));
        collect.setSongId(Integer.parseInt(songId));


        /* 判断是否收藏成功 */
        boolean flag = collectService.insert(collect);
        if(flag){
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "收藏成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "收藏失败");
        return jsonObject;

    }

    /**
     * 删除收藏
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteCollect(HttpServletRequest request){
        String userId = request.getParameter("userId");          //用户id
        String songId = request.getParameter("songId");          //歌曲id
        boolean flag = collectService.deleteByUserIdSongId(Integer.parseInt(userId),Integer.parseInt(songId));
        return flag;
    }

    /**
     * 查询所有收藏
     */
    @RequestMapping(value = "/allCollect", method = RequestMethod.GET)
    public Object allCollect(HttpServletRequest request){
        return collectService.allCollect();
    }
    /**
     * 根据用户id查询收藏列表
     */
    @RequestMapping(value = "/collectOfUserId", method = RequestMethod.GET)
    public Object collectOfUserId(HttpServletRequest request){
        String userId = request.getParameter("userId");
        return collectService.collectOfUserId(Integer.parseInt(userId));
    }
}
