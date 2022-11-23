package com.zz.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.zz.music.domain.SongList;
import com.zz.music.service.SongListService;
import com.zz.music.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 歌单控制类
 */
@RestController
@RequestMapping("/songList")
public class SongListController {
    /* 返回json格式的数据给前端 */
    @Autowired
    private SongListService songListService;
    /**
     * 添加歌单
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSongList(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        /* 获取前端传来的数据 */
        String title = request.getParameter("title").trim();      //标题
        String pic = request.getParameter("pic").trim();        //歌单图片
        String introduction = request.getParameter("introduction").trim();  //简介
        String style = request.getParameter("style").trim();  //风格

        /* 保存到歌单的对象中，把数据存入数据库中 */
        SongList songList = new SongList();
        songList.setTitle(title);
        songList.setPic(pic);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        /* 判断是否保存成功 */
        boolean flag = songListService.insert(songList);
        if(flag){
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "添加成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "添加失败");
        return jsonObject;

    }
    /**
     * 修改歌单
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateSongList(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        /* 获取前端传来的数据 */
        String id = request.getParameter("id").trim();          //主键
        String title = request.getParameter("title").trim();      //标题
        String introduction = request.getParameter("introduction").trim();  //简介
        String style = request.getParameter("style").trim();  //风格

        /* 保存到歌单的对象中 */
        SongList songList = new SongList();
        songList.setId(Integer.parseInt(id));
        songList.setTitle(title);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        /* 判断是否保存成功 */
        boolean flag = songListService.update(songList);
        if(flag){
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "修改成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "修改失败");
        return jsonObject;
    }
    /**
     * 删除歌单
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSongList(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        boolean flag = songListService.delete(Integer.parseInt(id));
        return flag;
    }
    /**
     * 根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        return songListService.selectByPrimaryKey(Integer.parseInt(id));
    }
    /**
     * 查询所有歌单
     */
    @RequestMapping(value = "/allSongList", method = RequestMethod.GET)
    public Object allSongList(HttpServletRequest request){
        return songListService.allSongList();
    }
    /**
     * 根据歌单标题精确查询列表
     */
    @RequestMapping(value = "/songListOfTitle", method = RequestMethod.GET)
    public Object songListOfTitle(HttpServletRequest request){
        String name = request.getParameter("title").trim();          //主键
        return songListService.songListOfTitle("title");              //查询
    }
    /**
     * 根据歌单标题模糊查询列表
     */
    @RequestMapping(value = "/likeTitle", method = RequestMethod.GET)
    public Object likeTitle(HttpServletRequest request){
        String title = request.getParameter("title").trim();          //主键
        return songListService.likeTitle("%"+title+"%");              //查询
    }
    /**
     * 根据歌单风格模糊查询列表
     */
    @RequestMapping(value = "/likeStyle", method = RequestMethod.GET)
    public Object likeStyle(HttpServletRequest request){
        String style = request.getParameter("style").trim();          //主键
        return songListService.likeStyle("%"+style+"%");              //查询
    }

    /**
     * 更新歌单图片
     */
    @RequestMapping(value = "/updateSongListPic", method = RequestMethod.POST)
    public Object updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();
        if(avatorFile.isEmpty()){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "文件上传失败  ");
            return jsonObject;
        }
        /* 文件名 = 当前时间到毫秒 + 原来的文件名，避免重名 文件被覆盖 */
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        /* 文件路径 */
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"img"
                +System.getProperty("file.separator")+"songListPic";
        /* 如果文件路径不存在，新增给路径*/
        File filel = new File(filePath);
        if(!filel.exists()){
            filel.mkdir();
        }
        /* 实际的文件地址 */
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        /* 存储到数据库里的相对文件地址 */
        String storeAvatorPath = "/img/songListPic/"+fileName;
        /* IO异常 报了异常就不应该更新数据库，因此都要写到try里*/
        try {
            /* 上传文件 */
            avatorFile.transferTo(dest);
            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic(storeAvatorPath);
            boolean flag = songListService.update(songList);
            if (flag){
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put(Consts.MSG, "上传成功");
                jsonObject.put("pic",storeAvatorPath);
                return jsonObject;
            }
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "上传失败");
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE, 0);
            /* 将错误信息打印给前端 */
            jsonObject.put(Consts.MSG, "上传失败"+e.getMessage());
        }finally {
            return jsonObject;
        }
    }
}
