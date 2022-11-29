/* eslint-disable*/
import axios from "axios";
import { get, post } from "./http";

// =======================> 用户 API
// 登录
export const loginIn = params => post(`consumer/login`, params);
// 注册
export const SignUp = params => post(`consumer/add`, params);
// 更新用户信息
export const updateUserMsg = params => post(`consumer/update`, params);
// 根据用户id查询该用户的详细信息
export const getUserOfId = id => get(`consumer/selectByPrimaryKey?id=${id}`);

// =======================> 歌单 API
// 获取全部歌单
export const getSongList = () => get("songList/allSongList");
// 获取歌单类型
export const getSongListOfStyle = style =>
  get(`songList/likeStyle?style=${style}`);
// 返回标题包含文字的歌单
export const getSongListOfLikeTitle = keywords =>
  get(`songList/likeTitle?title=${keywords}`);

// =======================> 歌单的歌曲 API
// 根据歌单id查询歌曲列表
export const getListSongOfSongId = songListId =>
  get(`listSong/detail?songListId=${songListId}`);

// =======================> 歌手 API
// 返回所有歌手
export const getAllSinger = () => get("singer/allSinger");
// 通过性别对歌手分类
export const getSingerOfSex = sex => get(`singer/singerOfSex?sex=${sex}`);

// =======================> 歌曲 API
// 根据歌曲id查询歌曲对象
export const getSongOfId = id => get(`song/detail?songId=${id}`);
// 根据歌手id查询歌曲
export const getSongOfSingerId = id => get(`song/singer/detail?singerId=${id}`);
// 搜索音乐 模糊查询歌曲
export const getSongOfSingerName = keywords =>
  get(`song/likeSongOfName?songName=${keywords}`);
// // 返回指定歌手名的歌曲
// export const getSongOfSingerName = keywords =>
//   get(`song/singerName/detail?name=${keywords}`);
// 下载音乐
export const download = url =>
  axios({
    method: "get",
    url: url,
    responseType: "blob"
  });

// =======================> 评分 API
// 提交评分
export const setRank = params => post(`/rank/add`, params);
// 获取指定歌单的评分（平均分）
export const getRankOfSongListId = songListId =>
  get(`/rank?songListId=${songListId}`);

// =======================> 评论 API
// 添加评论
export const setComment = params => post(`comment/add`, params);
// 点赞
export const setLike = params => post(`comment/like`, params);
// 返回当前歌曲或歌单的评论列表
export const getAllComment = (type, id) => {
  let url = "";
  if (type === 1) {
    //歌单
    url = `comment/commentOfSongListId?songListId=${id}`;
  } else if (type === 0) {
    //歌曲
    url = `comment/commentOfSongId?songId=${id}`;
  }
  return get(url);
};

// =======================> 收藏 API

// 新增收藏的歌曲 type: 0 代表歌曲， 1 代表歌单
export const setCollection = params => post(`collect/add`, params);
// 指定用户ID的收藏列表
export const getCollectionOfUser = userId =>
  get(`collect/collectOfUserId?userId=${userId}`);
