<template>
  <!-- 点击显示播放列表 -->
  <transition name="slide-fade">
    <div class="the-aside" v-if="showAside">
      <h2 class="title">播放列表</h2>
      <ul class="menus">
        <li
          v-for="(item, index) in listOfSongs"
          :class="{ 'is-play': id === item.id }"
          :key="index"
          @click="
            toplay(item.id, item.url, item.pic, index, item.name, item.lyric)
          "
        >
          {{ replaceFName(item.name) }}
        </li>
      </ul>
    </div>
  </transition>
</template>

<script>
import { mixin } from "../mixins";
import { mapGetters } from "vuex";

export default {
  name: "the-aside",
  mixins: [mixin],
  computed: {
    ...mapGetters([
      "id", // 播放中的歌曲id
      "listOfSongs", // 当前歌单列表 在mixins-index.js里已经获取过
      "showAside" // 是否显示侧边栏
    ])
  },
  // 点击空白处隐藏播放列表
  mounted() {
    let _this = this;
    document.addEventListener(
      "click",
      function() {
        _this.$store.commit("setShowAside", false);
      },
      true
    );
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/the-aside.scss";
</style>
