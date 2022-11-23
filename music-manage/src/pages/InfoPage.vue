<template>
  <div>
    <el-row :gutter="20" class="mgb20">
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ consumerCount }}</div>
              <div>用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ songCount }}</div>
              <div>歌曲总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ singerCount }}</div>
              <div>歌手数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ songListCount }}</div>
              <div>歌单数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mgb20">
      <el-col :span="12">
        <el-card>
          <h3 class="mgb20">用户性别比例</h3>
          <div style="background-color:white">
            <ve-pie :data="consumerSex" :theme="options"></ve-pie>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <h3 class="mgb20">歌曲类型分布</h3>
          <div style="background-color:white">
            <ve-histogram :data="songStyle"></ve-histogram>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mgb20">
      <el-col :span="12">
        <el-card>
          <h3 class="mgb20">歌手性别比例</h3>
          <div style="background-color:white">
            <ve-pie :data="singerSex"></ve-pie>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <h3 class="mgb20">歌曲国籍分布</h3>
          <div style="background-color:white">
            <ve-histogram :data="country"></ve-histogram>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  getAllConsumer,
  allSong,
  getAllSinger,
  getAllSongList
} from "../api/index";
export default {
  data() {
    return {
      consumerCount: 0, //用户总数
      songCount: 0, //歌曲总数
      singerCount: 0, //歌手数量
      songListCount: 0, //歌单数量
      consumer: [], //所有用户
      consumerSex: {
        //按性别分类的用户数
        columns: ["性别", "总数"],
        rows: [
          { 性别: "女", 总数: 0 },
          { 性别: "男", 总数: 0 }
        ]
      },
      options: {
        color: ["#ffc0cb", "#87cefa"]
      },
      songStyle: {
        //按歌单风格分类
        columns: ["风格", "总数"],
        rows: [
          { 风格: "华语流行", 总数: 0 },
          { 风格: "粤语经典", 总数: 0 },
          { 风格: "欧美", 总数: 0 },
          { 风格: "日韩", 总数: 0 }
        ]
      },
      singerSex: {
        //按性别分类的歌手数
        columns: ["性别", "总数"],
        rows: [
          { 性别: "女", 总数: 0 },
          { 性别: "男", 总数: 0 },
          { 性别: "组合", 总数: 0 },
          { 性别: "未知", 总数: 0 }
        ]
      },
      country: {
        columns: ["国籍", "总数"],
        rows: [
          { 国籍: "中国", 总数: 0 },
          { 国籍: "日本", 总数: 0 },
          { 国籍: "韩国", 总数: 0 },
          { 国籍: "欧美", 总数: 0 }
        ]
      }
    };
  },
  // 页面加载时就开始执行
  created() {},
  // 页面所有元素加载完后才开始执行
  mounted() {
    this.getConsumer();
    this.getSong();
    this.getSinger();
    this.getSongList();
  },
  methods: {
    getConsumer() {
      // 用户总数
      getAllConsumer().then(res => {
        this.consumer = res;
        this.consumerCount = res.length;
        this.consumerSex.rows[0]["总数"] = this.setSex(0, this.consumer);
        this.consumerSex.rows[1]["总数"] = this.setSex(1, this.consumer);
      });
    },
    setSex(sex, val) {
      //根据性别获取数量
      let count = 0;
      for (let item of val) {
        if (sex == item.sex) {
          count++;
        }
      }
      return count;
    },
    getSong() {
      // 歌曲总数
      allSong().then(res => {
        this.songCount = res.length;
      });
    },
    getSinger() {
      //歌手数量
      getAllSinger().then(res => {
        this.singer = res;
        this.singerCount = res.length;
        this.singerSex.rows[0]["总数"] = this.setSex(0, this.singer);
        this.singerSex.rows[1]["总数"] = this.setSex(1, this.singer);
        this.singerSex.rows[2]["总数"] = this.setSex(2, this.singer);
        this.singerSex.rows[3]["总数"] = this.setSex(3, this.singer);
        for (let item of res) {
          this.getByCountry(item.location);
        }
      });
    },
    getSongList() {
      getAllSongList().then(res => {
        this.songListCount = res.length;
        for (let item of res) {
          this.getByStyle(item.style);
        }
      });
    },
    getByStyle(style) {
      //根据歌单风格获取数量
      for (let item of this.songStyle.rows) {
        if (style.includes(item["风格"])) {
          item["总数"]++;
        }
      }
    },
    getByCountry(location) {
      // 根据location获取数量
      for (let item of this.country.rows) {
        if (location.includes(item["国籍"])) {
          item["总数"]++;
        }
      }
    }
  }
};
</script>

<style>
.grid-content {
  display: flex;
  align-items: center;
  height: 50px;
}

.grid-cont-center {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: darkgray;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}
</style>
