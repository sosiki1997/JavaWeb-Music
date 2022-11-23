<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="mini" @click="delAll"
          >批量删除</el-button
        >
        <el-input
          v-model="select_word"
          size="mini"
          placeholder="筛选关键字"
          class="handle-input"
        ></el-input>
        <el-button
          type="primary"
          size="mini"
          @click="centerDialogVisible = true"
          >添加歌单
        </el-button>
      </div>
    </div>
    <!-- computed 里的 data -->
    <el-table
      size="mini"
      ref="multipleTable"
      border-style="width:100%"
      height="700px"
      :data="data"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column label="歌单图片" width="110px" align="center">
        <!-- scope 当前单元格 -->
        <template slot-scope="scope">
          <div class="songList-img">
            <!-- 数据库里pic属性存的是相对地址，需要用到缓存 加上前面的后端地址 -->
            <img :src="getUrl(scope.row.pic)" style="width:100%" />
          </div>
          <!-- 上传歌单图片 -->
          <el-upload
            :action="uploadUrl(scope.row.id)"
            :before-upload="beforeAvatorUpload"
            :on-success="handleAvatorSuccess"
          >
            <el-button size="mini">更换图片</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column
        prop="title"
        label="标题"
        width="120"
        align="center"
      ></el-table-column>
      <el-table-column label="简介 ">
        <template slot-scope="scope">
          <p style="height:100px;overflow:scroll">
            {{ scope.row.introduction }}
          </p>
        </template>
      </el-table-column>
      <el-table-column
        prop="style"
        label="风格"
        width="120"
        align="center"
      ></el-table-column>
      <el-table-column label="歌曲管理 " width="110" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="songEdit(scope.row.id)"
            >歌曲管理</el-button
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 翻页 -->
    <div class="pagination">
      <el-pagination
        background
        layout="total, prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="tableData.length"
        @current-change="handleCurrentChange"
      ></el-pagination>
    </div>
    <!-- 新增歌单弹窗内容 -->
    <el-dialog
      title="添加歌单"
      :visible.sync="centerDialogVisible"
      width="400px"
      center=""
    >
      <el-form :model="registerForm" ref="registerForm" label-width="80px">
        <el-form-item prop="title" label="标题" size="mini">
          <el-input v-model="registerForm.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input
            v-model="registerForm.introduction"
            placeholder="简介"
            type="textarea"
          ></el-input>
        </el-form-item>
        <el-form-item prop="style" label="风格" size="mini">
          <el-input v-model="registerForm.style" placeholder="风格"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer">
        <el-button size="mini" @click="centerDialogVisible = false"
          >取消</el-button
        >
        <el-button size="mini" @click="addSongList">确定</el-button>
      </span>
    </el-dialog>
    <!-- 编辑歌单弹窗内容 -->
    <el-dialog
      title="修改歌单"
      :visible.sync="editVisible"
      width="400px"
      center=""
    >
      <el-form :model="form" ref="form" label-width="80px">
        <el-form-item prop="title" label="标题" size="mini">
          <el-input v-model="form.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input
            v-model="form.introduction"
            placeholder="简介"
            type="textarea"
          ></el-input>
        </el-form-item>
        <el-form-item prop="style" label="风格" size="mini">
          <el-input v-model="form.style" placeholder="风格"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer">
        <el-button size="mini" @click="editVisible = false">取消</el-button>
        <el-button size="mini" @click="editSave">确定</el-button>
      </span>
    </el-dialog>
    <!-- 删除歌单弹窗内容 单个删除 -->
    <el-dialog
      title="删除歌单"
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
import {
  setSongList,
  getAllSongList,
  updateSongList,
  delSongList
} from "../api/index";
import { mixin } from "../mixins/index";

export default {
  mixins: [mixin], //mixin必须export才能用
  data() {
    return {
      centerDialogVisible: false, //新增歌单弹窗是否显示
      editVisible: false, //编辑歌单弹窗是否显示
      delVisible: false, //删除歌单弹窗是否显示
      // 添加框
      registerForm: {
        title: "",
        introduction: "",
        style: ""
      },
      // 编辑框
      form: {
        id: "", //新增歌单后，后台生成的id
        title: "",
        introduction: "",
        style: ""
      },
      // 放置查询到的歌单
      tableData: [],

      tempData: [],
      // 搜索框输入的文字
      select_word: "",
      // 每一页的大小
      pageSize: 5,
      // 当前页数
      currentPage: 1,
      // 当前选择项 单个删除功能
      idx: -1,
      // 当前选中了哪些项 批量删除功能
      multipleSelection: []
    };
  },
  computed: {
    // 计算当前搜索结果表里的数据
    data() {
      return this.tableData.slice(
        (this.currentPage - 1) * this.pageSize,
        this.currentPage * this.pageSize
      );
    }
  },
  // 监控select_word值
  watch: {
    // 搜索框里面的内容发生变化的时候，搜索结果table列表的内容随之变化
    select_word: function() {
      if (this.select_word == "") {
        this.tableData = this.tempData;
      } else {
        this.tableData = [];
        for (let item of this.tempData) {
          if (item.title.includes(this.select_word)) {
            this.tableData.push(item);
          }
        }
      }
    }
  },
  created() {
    this.getData();
  },
  methods: {
    // 获取当前页 翻页功能的实现
    handleCurrentChange(val) {
      this.currentPage = val;
    },
    // 查询所有歌单
    getData() {
      // tableData 会发生变化
      // 所以我们让 tempData 不变化，对 tempData 循环过滤产生 tableData 的内容
      this.tempData = [];
      this.tableData = [];
      getAllSongList().then(res => {
        this.tempData = res;
        this.tableData = res;
        this.currentPage = 1;
      });
    },
    // 添加歌单
    addSongList() {
      let params = new URLSearchParams();
      params.append("title", this.registerForm.title);

      params.append("pic", "/img/songListPic/default.jpg");

      params.append("introduction", this.registerForm.introduction);

      params.append("style", this.registerForm.style);
      // 放完参数后进行提交，到后台异步执行，返回一个值res
      setSongList(params)
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
      this.centerDialogVisible = false;
    },
    // 弹出编辑歌单页面
    handleEdit(row) {
      this.editVisible = true;
      this.form = {
        id: row.id,
        title: row.title,
        introduction: row.introduction,
        style: row.style
      };
    },
    // 保存编辑
    editSave() {
      let params = new URLSearchParams();
      params.append("id", this.form.id);
      params.append("title", this.form.title);
      params.append("introduction", this.form.introduction);
      params.append("style", this.form.style);
      updateSongList(params)
        .then(res => {
          if (res.code == 1) {
            this.getData(); //刷新页面
            this.notify("修改成功", "success");
          } else {
            this.notify("修改失败", "error");
          }
        })
        .catch(err => {
          console.log(err);
        });
      this.editVisible = false;
    },
    // 更新歌单图片
    uploadUrl(id) {
      // 取后端地址
      return `${this.$store.state.HOST}/songList/updateSongListPic?id=${id}`;
    },
    // 删除歌单
    deleteRow() {
      delSongList(this.idx)
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
    },
    // 超链接 转向歌曲管理页面
    songEdit(id, name) {
      this.$router.push({ path: `/ListSong`, query: { id } });
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

.songList-img {
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
</style>
