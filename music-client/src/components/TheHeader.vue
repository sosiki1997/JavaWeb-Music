<template>
  <div class="the-header">
    <!--图标-->
    <div class="header-logo" @click="goHome">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icon-erji"></use>
      </svg>
      <span>{{ musicName }}</span>
    </div>
    <ul class="navbar" ref="change">
      <!-- 左侧导航栏 -->
      <li
        :class="{ active: item.name === activeName }"
        v-for="item in navMsg"
        :key="item.path"
        @click="goPage(item.path, item.name)"
      >
        {{ item.name }}
      </li>
      <!-- 搜索音乐 -->
      <li>
        <div class="header-search">
          <input
            type="text"
            placeholder="搜索音乐"
            @keyup.enter="goSearch()"
            v-model="keywords"
          />
          <div class="search-btn" @click="goSearch()">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-sousuo"></use>
            </svg>
          </div>
        </div>
      </li>
      <!-- 右侧导航栏 -->
      <li
        v-show="!loginIn"
        :class="{ active: item.name === activeName }"
        v-for="item in loginMsg"
        :key="item.type"
        @click="goPage(item.path, item.name)"
      >
        {{ item.name }}
      </li>
    </ul>
    <!--设置-->
    <div class="header-right" v-show="loginIn">
      <!-- 头像 -->
      <div id="user">
        <!-- <img src="../assets/img/user.jpg" alt="" /> -->
        <img :src="attachImageUrl(avator)" alt="" />
      </div>
      <ul class="menu">
        <li
          v-for="(item, index) in menuList"
          :key="index"
          @click="goMenuList(item.path)"
        >
          {{ item.name }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { mixin } from "../mixins";
import { mapGetters } from "vuex";
import { navMsg, loginMsg, menuList } from "../assets/data/header";

export default {
  name: "the-header",
  mixins: [mixin],
  data() {
    return {
      musicName: "Zz-music",
      navMsg: [], // header左侧导航栏
      loginMsg: [], // header右侧导航栏
      menuList: [], // 用户下拉菜单项
      keywords: "" //搜索关键字
    };
  },
  computed: {
    // mapGetters从整个工程其它地方获取数据
    ...mapGetters(["userId", "activeName", "avator", "username", "loginIn"])
  },
  created() {
    this.navMsg = navMsg;
    this.loginMsg = loginMsg;
    this.menuList = menuList;
  },
  mounted() {
    // 找到头像，点击出现下拉菜单
    document.querySelector("#user").addEventListener(
      "click",
      function(e) {
        document.querySelector(".menu").classList.add("show");
        e.stopPropagation(); // 关键在于阻止冒泡
      },
      false
    );
    // 点击内部不关闭菜单 点击“菜单”内部时，阻止事件冒泡。(这样点击内部时，菜单不会关闭)
    document.querySelector(".menu").addEventListener(
      "click",
      function(e) {
        e.stopPropagation();
      },
      false
    );
    // 点击外部关闭菜单
    document.addEventListener(
      "click",
      function() {
        document.querySelector(".menu").classList.remove("show");
      },
      false
    );
  },
  methods: {
    goHome() {
      this.$router.push({ path: "/" });
    },
    goPage(path, value) {
      document.querySelector(".menu").classList.remove("show");
      // 点击选中菜单 下划线
      this.changeIndex(value);
      if (!this.loginIn && path === "/my-music") {
        this.notify("请先登录", "warning");
      } else {
        this.$router.push({ path: path });
      }
    },
    // 点击选中菜单 下划线
    changeIndex(value) {
      this.$store.commit("setActiveName", value);
    },
    // 点击菜单
    goMenuList(path) {
      if (path === 0) {
        this.$store.commit("setIsActive", false);
      }
      document.querySelector(".menu").classList.remove("show");
      if (path) {
        this.$router.push({ path: path });
      } else {
        this.$store.commit("setLoginIn", false);
        this.$router.go(0); //刷新页面
      }
    },
    goSearch() {
      this.$store.commit("setSearchword", this.keywords);
      this.$router.push({
        path: "/search",
        query: { keywords: this.keywords }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/the-header.scss";
</style>
