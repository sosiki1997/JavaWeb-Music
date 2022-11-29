const configure = {
  state: {
    HOST: "http://localhost:8888", //后台访问地址根目录
    loginIn: false, // 用户是否登录
    searchword: "", // 搜索关键词
    showAside: false, // 是否显示侧边栏 播放列表
    activeName: "", // 当前选中的菜单名
    index: 0, // 列表中的序号
    isActive: false
  },
  // 获取
  getters: {
    isActive: state => {
      let isActive = state.isActive;
      if (!isActive) {
        isActive = JSON.parse(
          window.sessionStorage.getItem("isActive") || null
        );
      }
      return isActive;
    },
    loginIn: state => {
      let loginIn = state.loginIn;
      if (!loginIn) {
        loginIn = JSON.parse(window.sessionStorage.getItem("loginIn") || null);
      }
      return loginIn;
    },
    activeName: state => {
      let activeName = state.activeName;
      if (!activeName) {
        activeName = JSON.parse(
          window.sessionStorage.getItem("activeName") || null
        );
      }
      return activeName;
    },
    showAside: state => {
      let showAside = state.showAside;
      if (!showAside) {
        showAside = JSON.parse(
          window.sessionStorage.getItem("showAside") || null
        );
      }
      return showAside;
    },
    index: state => {
      let index = state.index;
      if (!index) {
        index = JSON.parse(window.sessionStorage.getItem("index") || null);
      }
      return index;
    },
    searchword: state => state.searchword
  },
  // 设置
  mutations: {
    setIsActive: (state, isActive) => {
      state.isActive = isActive;
      window.sessionStorage.setItem("isActive", JSON.stringify(isActive));
    },
    setLoginIn: (state, loginIn) => {
      state.loginIn = loginIn;
      window.sessionStorage.setItem("loginIn", JSON.stringify(loginIn));
    },
    setActiveName: (state, activeName) => {
      state.activeName = activeName;
      // 把activeName转换成JSON格式放到sessionStorage
      window.sessionStorage.setItem("activeName", JSON.stringify(activeName));
    },
    setShowAside: (state, showAside) => {
      state.showAside = showAside;
      window.sessionStorage.setItem("showAside", JSON.stringify(showAside));
    },
    setIndex: (state, index) => {
      state.index = index;
    },
    setSearchword: (state, searchword) => {
      state.searchword = searchword;
    }
  },
  actions: {}
};

export default configure;
