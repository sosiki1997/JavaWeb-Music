package com.zz.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.zz.music.domain.Comment;
import com.zz.music.service.CommentService;
import com.zz.music.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 评论控制类
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    /* 返回json格式的数据给前端 */
    @Autowired
    private CommentService commentService;
    /**
     * 添加评论
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addComment(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        /* 获取前端传来的数据 */
        String userId = request.getParameter("userId");          //用户id
        String type = request.getParameter("type");              //评论类型
        String songId = request.getParameter("songId");          //歌曲id
        String songListId = request.getParameter("songListId");  //歌单id
        String content = request.getParameter("content").trim(); //评论内容

        /* 保存到评论的对象中，把数据存入数据库中 */
        Comment comment = new Comment();
        comment.setUserId(Integer.parseInt(userId));
        comment.setType(new Byte(type));
        if(new Byte(type) == 0){
            comment.setSongId(Integer.parseInt(songId));
        }else{
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);

        /* 判断是否评论成功 */
        boolean flag = commentService.insert(comment);
        if(flag){
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "评论成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "评论失败");
        return jsonObject;

    }
    /**
     * 修改评论
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateComment(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        /* 获取前端传来的数据 */
        String id = request.getParameter("id").trim();          //主键
        String userId = request.getParameter("userId").trim();          //用户id
        String type = request.getParameter("type").trim();              //评论类型
        String songId = request.getParameter("songId").trim();          //歌曲id
        String songListId = request.getParameter("songListId").trim();  //歌单id
        String content = request.getParameter("content").trim();        //评论内容

        /* 保存到评论的对象中 */
        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUserId(Integer.parseInt(userId));
        comment.setType(new Byte(type));
        if(songId!=null&&songId.equals("")){
            songId = null;
        }else{
            comment.setSongId(Integer.parseInt(songId));
        }
        if(songListId!=null&&songListId.equals("")){
            songListId = null;
        }else{
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);

        /* 判断是否保存成功 */
        boolean flag = commentService.update(comment);
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
     * 删除评论
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteComment(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        boolean flag = commentService.delete(Integer.parseInt(id));
        return flag;
    }
    /**
     * 根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        return commentService.selectByPrimaryKey(Integer.parseInt(id));
    }
    /**
     * 查询所有评论
     */
    @RequestMapping(value = "/allComment", method = RequestMethod.GET)
    public Object allComment(HttpServletRequest request){
        return commentService.allComment();
    }
    /**
     * 根据歌曲id查询评论
     */
    @RequestMapping(value = "/commentOfSongId", method = RequestMethod.GET)
    public Object commentOfSongId(HttpServletRequest request){
        String songId = request.getParameter("songId").trim();
        return commentService.commentOfSongId(Integer.parseInt(songId));
    }
    /**
     * 根据歌单id查询评论
     */
    @RequestMapping(value = "/commentOfSongListId", method = RequestMethod.GET)
    public Object commentOfSongListId(HttpServletRequest request){
        String songListId = request.getParameter("songListId").trim();
        return commentService.commentOfSongListId(Integer.parseInt(songListId));
    }
    /**
     * 点赞评论
     */
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public Object like(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        /* 获取前端传来的数据 */
        String id = request.getParameter("id").trim();              //主键
        String up = request.getParameter("up").trim();          //点赞

        /* 保存到评论的对象中 */
        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUp(Integer.parseInt(up));

        /* 判断是否保存成功 */
        boolean flag = commentService.update(comment);
        if(flag){
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "点赞成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "点赞失败");
        return jsonObject;
    }

}