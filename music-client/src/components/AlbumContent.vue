<template>
  <!-- 独立出这个页面，可以通用 -->
  <!-- 搜索音乐 显示界面 -->
  <div class="content">
    <h1 class="title">
      <slot name="title"></slot>
    </h1>
    <hr />
    <ul>
      <li class="list-title">
        <div class="song-item">
          <span class="item-index"></span>
          <span class="item-title">歌曲名</span>
          <span class="item-name">艺人</span>
          <span class="item-intro">专辑</span>
        </div>
      </li>
      <!-- 遍历传来的 songList 并显示出来 -->
      <li class="list-content" v-for="(item, index) in songList" :key="index">
        <div
          class="song-item"
          :class="{ 'is-play': id === item.id }"
          @click="
            toplay(item.id, item.url, item.pic, index, item.name, item.lyric)
          "
        >
          <span class="item-index">
            <span v-if="id !== item.id">{{ index + 1 }}</span>
            <svg v-if="id === item.id" class="icon" aria-hidden="true">
              <use xlink:href="#icon-yinliang"></use>
            </svg>
          </span>
          <span class="item-title">{{ replaceFName(item.name) }}</span>
          <span class="item-name">{{ replaceLName(item.name) }}</span>
          <span class="item-intro">{{ item.introduction }}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { mixin } from "../mixins";
import { mapGetters } from "vuex";

export default {
  name: "album-content",
  mixins: [mixin],
  // 获取参数 songList
  props: ["songList"],
  computed: {
    ...mapGetters([
      "id" // 音乐ID
    ])
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/album-content.scss";
</style>
