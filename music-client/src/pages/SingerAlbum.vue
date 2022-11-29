<template>
  <!-- 点击首页歌手进入的页面 -->
  <div class="singer-album">
    <div class="album-slide">
      <div class="singer-img">
        <img :src="attachImageUrl(singer.pic)" alt="" />
      </div>
      <ul class="info">
        <li v-if="singer.sex == 0 || singer.sex == 1">
          性别：{{ attachSex(singer.sex) }}
        </li>
        <li>生日：{{ attachBirth(singer.birth) }}</li>
        <li>故乡：{{ singer.location }}</li>
      </ul>
    </div>
    <div class="album-content">
      <div class="intro">
        <h2>{{ singer.name }}</h2>
        <span>{{ singer.introduction }}</span>
      </div>
      <div class="content">
        <album-content :songList="listOfSongs">
          <template slot="title">歌单</template>
        </album-content>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from "../mixins";
import { mapGetters } from "vuex";
import AlbumContent from "../components/AlbumContent";
import { getSongOfSingerId } from "../api/index";

export default {
  name: "singer-album",
  components: {
    AlbumContent
  },
  mixins: [mixin],
  data() {
    return {
      singerId: "", //前面传来的歌手id
      singer: {} //当前歌手信息
    };
  },
  computed: {
    ...mapGetters([
      "tempList", //当前歌手对象
      "listOfSongs" //当前播放列表
    ])
  },
  mounted() {
    this.singerId = this.$route.params.id; // 给歌单ID赋值
    this.singer = this.tempList;
    this.getSongList();
  },
  methods: {
    // 根据歌手id查询歌曲
    getSongList() {
      getSongOfSingerId(this.singerId)
        .then(res => {
          this.$store.commit("setListOfSongs", res);
        })
        .catch(err => {
          console.log(err);
        });
    },
    // 获取歌手性别
    attachSex(value) {
      if (value === 0) {
        return "女";
      } else if (value === 1) {
        return "男";
      }
      return "";
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/singer-album.scss";
</style>
