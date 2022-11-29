import { getSongOfSingerName, getCollectionOfUser } from "../api/index";
import { mapGetters } from "vuex";

export const mixin = {
  computed: {
    ...mapGetters(["userId", "loginIn"])
  },
  methods: {
    // 提示信息
    notify(title, type) {
      this.$notify({
        title: title,
        type: type
      });
    },
    // 获取图片信息
    attachImageUrl(srcUrl) {
      return srcUrl
        ? this.$store.state.configure.HOST + srcUrl || "../assets/img/user.jpg"
        : "";
    },
    // 获取歌手生日 在歌手页显示
    attachBirth(val) {
      let birth = String(val).match(/[0-9-]+(?=\s)/);
      return Array.isArray(birth) ? birth[0] : birth;
    },
    // 得到名字后部分
    replaceFName(str) {
      let arr = str.split("-");
      return arr[1];
    },
    // 得到名字前部分
    replaceLName(str) {
      let arr = str.split("-");
      return arr[0];
    },
    // 播放
    toplay: function(id, index, url, pic, name, lyric) {
      this.$store.commit("setId", id);
      this.$store.commit("setListIndex", index); //当前歌曲在列表中的位置
      this.$store.commit("setUrl", this.$store.state.configure.HOST + url);
      this.$store.commit("setpicUrl", this.$store.state.configure.HOST + pic);
      this.$store.commit("setTitle", this.replaceFName(name)); //歌名
      this.$store.commit("setArtist", this.replaceLName(name)); //歌手名
      this.$store.commit("setLyric", this.parseLyric(lyric));
      this.$store.commit("setIsActive", false);
      if (this.loginIn) {
        getCollectionOfUser(this.userId)
          .then(res => {
            for (let item of res) {
              if (item.songId === id) {
                this.$store.commit("setIsActive", true);
                break;
              }
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    // 解析歌词
    parseLyric(text) {
      let lines = text.split("\n"); //按行放到数组lines
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g; //时间格式的正则表达式
      let result = []; //返回值

      // 对于歌词格式不对的特殊处理
      if (!/\[.+\]/.test(text)) {
        return [[0, text]];
      }
      // 去掉前面格式不正确的行
      while (!pattern.test(lines[0])) {
        lines = lines.slice(1);
      }

      lines[lines.length - 1].length === 0 && lines.pop();
      // 遍历每一行，形成数组，有两个参数：时间、歌词
      for (let item of lines) {
        let time = item.match(pattern); // 存前面的时间段
        let value = item.replace(pattern, ""); // 存后面的歌词
        // console.log(time) // 时间
        // console.log(value) // 歌词数据
        for (let item1 of time) {
          //把时间转换成秒 便于计算
          let t = item1.slice(1, -1).split(":"); //取出时间，换算成数组
          if (value !== "") {
            result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value]);
          }
        }
      }
      // 按照第一个参数时间 排序
      result.sort(function(a, b) {
        return a[0] - b[0];
      });
      return result;
    },
    // 搜索音乐 根据歌手名字模糊查询歌曲
    getSong() {
      if (!this.$route.query.keywords) {
        this.$store.commit("setListOfSongs", []);
        this.notify("您输入内容为空", "warning");
      } else {
        getSongOfSingerName(this.$route.query.keywords)
          .then(res => {
            if (!res.length) {
              this.$store.commit("setListOfSongs", []);
              this.notify("系统暂无该歌曲", "warning");
            } else {
              this.$store.commit("setListOfSongs", res);
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    }
  }
};
