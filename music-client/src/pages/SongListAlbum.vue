<template>
  <!-- 点击首页歌单进入的页面 -->
  <div class="song-list-album">
    <div class="album-slide">
      <div class="album-img">
        <img :src="attachImageUrl(singers.pic)" alt="" />
      </div>
      <div class="album-info">
        <h2>简介：</h2>
        <span>
          {{ singers.introduction }}
        </span>
      </div>
    </div>
    <div class="album-content">
      <div class="album-title">
        <p>{{ singers.title }}</p>
      </div>
      <!--评分-->
      <div class="album-score">
        <div>
          <h3>歌单评分：</h3>
          <div>
            <!-- 五角星 -->
            <el-rate v-model="value5" disabled></el-rate>
          </div>
        </div>
        <span>{{ value5 * 2 }}</span>
        <div>
          <h3>评价：</h3>
          <div @click="pushValue()">
            <el-rate v-model="value3" show-text allow-half></el-rate>
          </div>
        </div>
      </div>
      <!--歌曲-->
      <div class="songs-body">
        <album-content :songList="listOfSongs">
          <template slot="title">歌单</template>
        </album-content>
        <comment :playId="songListId" :type="1"></comment>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from "../mixins";
import { mapGetters } from "vuex";
import AlbumContent from "../components/AlbumContent";
import Comment from "../components/Comment";
import {
  getRankOfSongListId,
  setRank,
  getSongOfId,
  getListSongOfSongId
} from "../api/index";

export default {
  name: "song-list-album",
  components: {
    AlbumContent,
    Comment
  },
  // mixins: [mixin],
  data() {
    return {
      songLists: [],
      singers: {},
      count: 0, // 点赞数
      songListId: "", // 歌单ID
      value3: 0, //提交评价的分数
      value5: 0 //平均分
    };
  },
  computed: {
    ...mapGetters([
      "loginIn", // 登录标识
      "tempList", // 当前歌单信息
      "listOfSongs", // 当前播放列表
      "userId", // 用户ID
      "avator" // 用户头像
    ])
  },
  // created() {
  //   this.songListId = this.$route.params.id; // 给歌单ID赋值
  //   this.singers = this.tempList;
  //   this.getSongId();
  // },
  created() {
    this.songListId = this.tempList.id; // 给歌单ID赋值
    this.singers = this.tempList; //歌单/歌手图片
    this.getSongId(); // 获取歌单里面的歌曲ID
    this.getRank(this.songListId); // 获取评分
  },
  mixins: [mixin],
  methods: {
    // 获取当前歌单的歌曲列表
    getSongId() {
      getListSongOfSongId(this.songListId)
        .then(res => {
          // 获取歌单里的歌曲信息
          for (let item of res) {
            this.getSongList(item.songId);
          }
          this.$store.commit("setListOfSongs", this.songLists);
        })
        .catch(err => {
          console.log(err);
        });
    },
    // 根据歌曲id获取歌曲信息
    getSongList(id) {
      getSongOfId(id)
        .then(res => {
          this.songLists.push(res);
        })
        .catch(err => {
          console.log(err);
        });
    },
    // 获取歌单评分
    getRank(id) {
      // 平均分
      getRankOfSongListId(id)
        .then(res => {
          // 半颗星的计分方式，显示的时候再 * 2
          this.value5 = res / 2;
        })
        .catch(err => {
          console.log(err);
        });
    },
    // 提交评价
    pushValue() {
      if (this.loginIn) {
        let params = new URLSearchParams();
        params.append("songListId", this.songListId);
        params.append("consumerId", this.userId);
        params.append("score", this.value3 * 2);
        setRank(params)
          .then(res => {
            if (res.code === 1) {
              this.getRank(this.songListId);
              this.notify("评分成功", "success");
            } else {
              this.notify("评分失败", "error");
            }
          })
          .catch(err => {
            this.notify("您已经评价过啦", "error");
          });
      } else {
        this.value3 = null;
        this.notify("请先登录", "warning");
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/song-list-album.scss";
</style>
