import Vue from "vue";
import Vuex from "vuex";
Vue.use(Vuex);

// 放缓存数据
const store = new Vuex.Store({
  state: {
    HOST: "http://127.0.0.1:8888",
    isPlay: false, // 是否播放中
    url: "", // 歌曲地址
    id: "" //歌曲id
  },
  // SongAudio组件
  // 取值
  getters: {
    isPlay: state => state.isPlay,
    url: state => state.url,
    id: state => state.id
  },
  // 设置值
  mutations: {
    setIsPlay: (state, isPlay) => {
      state.isPlay = isPlay;
    },
    setUrl: (state, url) => {
      state.url = url;
    },
    setId: (state, id) => {
      state.id = id;
    }
  }
});

export default store;
