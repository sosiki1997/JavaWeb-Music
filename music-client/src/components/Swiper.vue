<template>
  <div class="swiper">
    <div class="swiper-container">
      <el-carousel :interval="4000" type="card" height="280px">
        <el-carousel-item v-for="(item, index) in swiperList" :key="index">
          <img :src="item.picImg" />
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script>
import { swiperList } from "../assets/data/swiper";

export default {
  name: "swiper",
  data() {
    return {
      // 轮播图数据在 assets-data-swiper.js 里设置
      swiperList: []
    };
  },
  created() {
    this.swiperList = swiperList;
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/swiper.scss";
</style>
