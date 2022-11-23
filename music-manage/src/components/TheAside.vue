<template>
  <div class="sidebar">
    <!-- 左侧菜单 -->
    <el-menu
      class="sidebar-el-menu"
      :default-active="onRoutes"
      :collapse="collapse"
      background-color="#324157"
      text-color="#ffffff"
      active-text-color="#20a0ff"
      unique-opened
      router
    >
      <template v-for="item in items">
        <template>
          <el-menu-item :index="item.index" :key="item.index">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import bus from "../assets/js/bus";

export default {
  // 菜单内容
  data() {
    return {
      // 默认不折叠
      collapse: false,
      // 由于router和index的对应关系，info 又指向了router里的 path: '/Info'
      // 展示 InfoPage.vue
      items: [
        {
          icon: "el-icon-document",
          index: "info",
          title: "系统首页"
        },
        {
          icon: "el-icon-document",
          index: "consumer",
          title: "用户管理"
        },
        {
          icon: "el-icon-document",
          index: "singer",
          title: "歌手管理"
        },
        {
          icon: "el-icon-document",
          index: "songList",
          title: "歌单管理"
        }
      ]
    };
  },
  // 监控 onRoutes 点击变化
  computed: {
    onRoutes() {
      // 去掉“/”
      return this.$route.path.replace("/", "");
    }
  },
  created() {
    // 通过 Event Bus 进行组件间通信，来折叠侧边栏
    // 绑定两个vue互不相同组件，接收header组件传过来的信息，让collapse属性发生变化
    bus.$on("collapse", msg => {
      this.collapse = msg;
    });
  }
};
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  background-color: #334256;
  left: 0;
  top: 70px;
  bottom: 0;
  /* 超出范围上下滚动 */
  overflow-y: scroll;
}
/* 去掉侧边滚动栏 */
.sidebar::-webkit-scrollbar {
  width: 0;
}
/* 折叠状态不使用该样式 */
.sidebar-el-menu:not(.el-menu--collapse) {
  width: 150px;
}
/* 让循环高度是100%，避免宽度不统一 */
.sidebar > ul {
  height: 100%;
}
</style>
