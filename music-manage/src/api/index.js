/* eslint-disable*/
import { get, post, deletes } from "./http";

// 对应后台方法

// =======================> 管理员 API
// 判断管理员是否登录成功
export const getLoginStatus = params => post(`admin/login/status`, params);

// =======================> 歌手 API
// 查询歌手
// 后端已经写好了查询的方法，这里添加一个查询的函数即可，它没有参数
export const getAllSinger = () => get(`singer/allSinger`);

// 添加歌手
// 将参数params提交到了后端
// 后端SingerController.java里的singer/add 里写了addSinger方法
export const setSinger = params => post(`singer/add`, params);

// 编辑歌手
export const updateSinger = params => post(`singer/update`, params);

// 删除歌手
export const delSinger = id => get(`singer/delete?id=${id}`);

// =======================> 歌曲 API
// 根据歌手id查询歌曲
export const songOfSingerId = id => get(`song/singer/detail?singerId=${id}`);

// 编辑歌曲
export const updateSong = params => post(`song/update`, params);

// 删除歌曲
export const delSong = id => get(`song/delete?id=${id}`);

// 根据歌曲id查询歌曲对象 歌单查询歌曲功能
export const songOfSongrId = id => get(`song/detail?songId=${id}`);

// 根据歌曲名获取歌曲对象 歌单查询歌曲功能
export const songOfSongrName = songName =>
  get(`song/songOfSongrName?songName=${songName}`);

// 查询所有歌曲数量 系统首页
export const allSong = () => get(`song/allSong`);

// =======================> 歌单 API
// 查询歌手
export const getAllSongList = () => get(`songList/allSongList`);
// 添加歌单
export const setSongList = params => post(`songList/add`, params);

// 编辑歌单
export const updateSongList = params => post(`songList/update`, params);

// 删除歌单
export const delSongList = id => get(`songList/delete?id=${id}`);

// =======================> 歌单歌曲 API
// 根据歌单id查询歌曲列表
export const listSongDetail = songListId =>
  get(`listSong/detail?songListId=${songListId}`);

// 给歌单增加歌曲
export const listSongAdd = params => post(`listSong/add`, params);

// 删除歌单内的歌曲
export const delListSong = (songId, songListId) =>
  get(`listSong/delete?songId=${songId}&songListId=${songListId}`);

// =======================> 用户 API
// 查询用户
export const getAllConsumer = () => get(`consumer/allConsumer`);
// 添加用户
export const setConsumer = params => post(`consumer/add`, params);
// 编辑用户
export const updateConsumer = params => post(`consumer/update`, params);
// 删除用户
export const delConsumer = id => get(`consumer/delete?id=${id}`);

// // =======================> 收藏列表 API

// // =======================> 评论列表 API
