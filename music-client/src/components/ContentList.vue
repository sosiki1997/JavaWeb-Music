<template>
  <!--展示歌单/歌手-->
  <div class="content-list">
    <ul class="section-content">
      <li
        class="content-item"
        v-for="(item, index) in contentList"
        :key="index"
      >
        <div class="kuo" @click="goAblum(item, item.name)">
          <img class="item-img" :src="attachImageUrl(item.pic)" alt="" />
          <!-- 歌单/歌手图片上的播放图标 -->
          <div class="mask" @click="goAblum(item, item.name)">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-bofang"></use>
            </svg>
          </div>
        </div>
        <!-- 歌单标题/歌手名字 -->
        <p class="item-name">{{ item.name || item.title }}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import { mixin } from "../mixins";

export default {
  name: "content-list",
  mixins: [mixin],
  props: ["contentList"],
  methods: {
    // 点击首页歌单/歌手进入歌曲列表
    goAblum(item, type) {
      this.$store.commit("setTempList", item);
      if (type) {
        //有name值的是歌手
        this.$router.push({ path: `/singer-album/${item.id}` }); //歌手
      } else {
        this.$router.push({ path: `/song-list-album/${item.id}` }); //歌单
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/content-list.scss";
</style>
