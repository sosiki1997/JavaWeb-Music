package com.zz.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.zz.music.domain.Singer;
import com.zz.music.service.SingerService;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 歌手控制类
 */
@RestController
@RequestMapping("/singer")
public class SingerController {
    /* 返回json格式的数据给前端 */
    @Autowired
    private SingerService singerService;
    /**
     * 添加歌手
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSinger(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        /* 获取前端传来的数据 */
        String name = request.getParameter("name").trim();      //姓名
        String sex = request.getParameter("sex").trim();        //性别
        String pic = request.getParameter("pic").trim();        //头像
        String birth = request.getParameter("birth").trim();    //生日
        String location = request.getParameter("location").trim();  //地区
        String introduction = request.getParameter("introduction").trim();  //简介

        /* 把生日 string 转换成 datetime 格式，和数据库一致 */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate =  new Date();
        try {
            /* 让它抛出异常 */
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /* 保存到歌手的对象中，把数据存入数据库中 */
        Singer singer = new Singer();
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setPic(pic);
        singer.setBirth(birthDate);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        /* 判断是否保存成功 */
        boolean flag = singerService.insert(singer);
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
     * 修改歌手
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateSinger(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        /* 获取前端传来的数据 */
        String id = request.getParameter("id").trim();          //主键
        String name = request.getParameter("name").trim();      //姓名
        String sex = request.getParameter("sex").trim();        //性别
        String birth = request.getParameter("birth").trim();    //生日
        String location = request.getParameter("location").trim();  //地区
        String introduction = request.getParameter("introduction").trim();  //简介
        /* 把生日转换成Date格式，和数据库一致 */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate =  new Date();
        try {
            /* 让它抛出异常 */
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /* 保存到歌手的对象中 */
        Singer singer = new Singer();
        singer.setId(Integer.parseInt(id));
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setBirth(birthDate);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        /* 判断是否保存成功 */
        boolean flag = singerService.update(singer);
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
     * 删除歌手
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSinger(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        boolean flag = singerService.delete(Integer.parseInt(id));
        return flag;
    }
    /**
     * 根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        return singerService.selectByPrimaryKey(Integer.parseInt(id));
    }
    /**
     * 查询所有歌手
     */
    @RequestMapping(value = "/allSinger", method = RequestMethod.GET)
    public Object allSinger(HttpServletRequest request){
        return singerService.allSinger();
    }
    /**
     * 根据歌手名字模糊查询列表
     */
    @RequestMapping(value = "/singerOfName", method = RequestMethod.GET)
    public Object singerOfName(HttpServletRequest request){
        String name = request.getParameter("name").trim();          //主键
        return singerService.singerOfName("%"+name+"%");              //模糊查询名字
    }
    /**
     * 根据性别查询
     */
    @RequestMapping(value = "/singerOfSex", method = RequestMethod.GET)
    public Object singerOfSex(HttpServletRequest request){
        String sex = request.getParameter("sex").trim();          //性别
        return singerService.singerOfSex(Integer.parseInt(sex));
    }
    /**
     * 更新歌手图片
     */
    @RequestMapping(value = "/updateSingerPic", method = RequestMethod.POST)
    public Object updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
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
                +System.getProperty("file.separator")+"singerPic";
        /* 如果文件路径不存在，新增给路径*/
        File filel = new File(filePath);
        if(!filel.exists()){
            filel.mkdir();
        }
        /* 实际的文件地址 */
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        /* 存储到数据库里的相对文件地址 */
        String storeAvatorPath = "/img/singerPic/"+fileName;
        /* IO异常 报了异常就不应该更新数据库，因此都要写到try里*/
        try {
            /* 上传文件 */
            avatorFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeAvatorPath);
            boolean flag = singerService.update(singer);
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
