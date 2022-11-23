<template>
  <div class="table">
    <div class="crumbs"><i class="el-icon-tickets"></i>歌单内歌曲信息</div>
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="mini" @click="delAll"
          >批量删除</el-button
        >
        <el-input
          v-model="select_word"
          size="mini"
          placeholder="请输入歌曲名"
          class="handle-input"
        ></el-input>
        <el-button
          type="primary"
          size="mini"
          @click="centerDialogVisible = true"
          >添加歌曲
        </el-button>
      </div>
    </div>

    <el-table
      size="mini"
      ref="multipleTable"
      border-style="width:100%"
      height="700px"
      :data="tableData"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="40"></el-table-column>

      <el-table-column
        prop="name"
        label="歌手-歌名"
        align="center"
      ></el-table-column>

      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增歌曲弹窗内容 -->
    <el-dialog
      title="添加歌曲"
      :visible.sync="centerDialogVisible"
      width="400px"
      center=""
    >
      <el-form
        :model="registerForm"
        ref="registerForm"
        label-width="80px"
        action=""
        id="tf"
      >
        <el-form-item prop="singerName" label="歌手名字" size="mini">
          <el-input
            v-model="registerForm.singerName"
            placeholder="歌手名字"
          ></el-input>
        </el-form-item>
        <el-form-item prop="songName" label="歌曲名字" size="mini">
          <el-input
            v-model="registerForm.songName"
            placeholder="歌曲名字"
          ></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer">
        <el-button size="mini" @click="centerDialogVisible = false"
          >取消</el-button
        >
        <el-button size="mini" @click="getSongId">确定</el-button>
      </span>
    </el-dialog>

    <!-- 删除歌曲弹窗内容 单个删除 -->
    <el-dialog
      title="删除歌曲"
      :visible.sync="delVisible"
      width="400px"
      center=""
    >
      <div align="center">删除不可恢复，是否确定删除？</div>

      <span slot="footer">
        <el-button size="mini" @click="delVisible = false">取消</el-button>
        <el-button size="mini" @click="deleteRow">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mixin } from "../mixins/index";
import {
  listSongDetail,
  songOfSongrId,
  songOfSongrName,
  listSongAdd,
  delListSong
} from "../api/index";

export default {
  mixins: [mixin], //mixin必须export才能用
  data() {
    return {
      centerDialogVisible: false, //新增歌曲弹窗是否显示

      delVisible: false, //删除歌曲弹窗是否显示
      // 添加框
      registerForm: {
        singerName: "", // 歌手名字
        songName: "" //歌曲名字
      },

      // 放置查询到的歌曲
      tableData: [],

      tempData: [],
      // 搜索框输入的文字
      select_word: "",

      // 当前选择项 单个删除功能
      idx: -1,
      // 当前选中了哪些项 批量删除功能
      multipleSelection: [],
      // 歌单id
      songListId: ""
    };
  },

  // 监控select_word值 模糊查询
  watch: {
    // 搜索框里面的内容发生变化的时候，搜索结果table列表的内容随之变化
    select_word: function() {
      if (this.select_word == "") {
        this.tableData = this.tempData;
      } else {
        this.tableData = [];
        for (let item of this.tempData) {
          if (item.name.includes(this.select_word)) {
            this.tableData.push(item);
          }
        }
      }
    }
  },
  // 获取当前页的数据
  created() {
    this.songListId = this.$route.query.id;
    this.getData();
  },

  methods: {
    // 查询所有歌曲
    getData() {
      // tableData 会发生变化
      // 所以我们让 tempData 不变化，对 tempData 循环过滤产生 tableData 的内容
      this.tempData = [];
      this.tableData = [];
      listSongDetail(this.songListId).then(res => {
        for (let item of res) {
          this.getSong(item.songId);
        }
      });
    },
    // 根据歌曲id查询歌曲对象，放到tempData和tableData里面
    getSong(id) {
      songOfSongrId(id)
        .then(res => {
          this.tempData.push(res);
          this.tableData.push(res);
        })
        .catch(err => {
          console.log(err);
        });
    },
    // 添加歌曲前的准备，获取到歌曲id
    getSongId() {
      let _this = this;
      var songOfName =
        _this.registerForm.singerName + "-" + _this.registerForm.songName;
      songOfSongrName(songOfName).then(res => {
        _this.addSong(res[0].id);
      });
    },
    // 添加歌曲
    addSong(songId) {
      let _this = this;
      let params = new URLSearchParams();
      params.append("songId", songId);
      params.append("songListId", this.songListId);

      listSongAdd(params)
        .then(res => {
          if (res.code == 1) {
            this.getData(); //刷新页面
            this.notify("添加成功", "success");
          } else {
            this.notify("添加失败", "error");
          }
        })
        .catch(err => {
          console.log(err);
        });
      // 不论成功失败，关闭弹窗
      _this.centerDialogVisible = false;
    },

    // 删除歌曲
    deleteRow() {
      delListSong(this.idx, this.songListId)
        .then(res => {
          if (res) {
            this.getData(); //重新查询数据 刷新页面
            this.notify("删除成功", "success");
          } else {
            this.notify("删除失败", "error");
          }
        })
        .catch(err => {
          console.log(err);
        });
      this.delVisible = false;
    }
  }
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

.song-img {
  width: 100%;
  height: 80px;
  border-radius: 5px;
  margin-bottom: 5px;
  overflow: hidden;
}

.pagination {
  display: flex;
  justify-content: center;
}

.play {
  position: absolute;
  z-index: 100;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  top: 18px;
  left: 15px;
}

.icon {
  width: 2em;
  height: 2em;
  color: green;
  fill: currentColor;
  overflow: hidden;
}
</style>
