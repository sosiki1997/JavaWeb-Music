export const mixin = {
  // 共用的常用方法
  methods: {
    // 提示信息
    notify(title, type) {
      this.$notify({
        title: title,
        type: type
      });
    },
    // 根据相对地址获取绝对地址
    getUrl(url) {
      return `${this.$store.state.HOST}${url}`;
    },

    // 获取生日
    attachBirth(val) {
      return String(val).slice(0, 10);
    },
    // 获取性别
    changeSex(value) {
      if (value === 0) {
        return "女";
      } else if (value === 1) {
        return "男";
      } else if (value === 2) {
        return "组合";
      } else if (value === 3) {
        return "不明";
      } else if (value === "男" || value === "女") {
        return value;
      }
    },

    // 上传图片之前的校验
    beforeAvatorUpload(file) {
      // 判断是不是图片
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 或 PNG 格式!");
        return false; // 不再继续执行
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    // 上传图片成功后
    handleAvatorSuccess(res) {
      // 当前页面
      let _this = this;
      if (res.code == 1) {
        // 重新查询，刷新页面
        _this.getData();
        _this.$notify({
          title: "上传成功",
          type: "success"
        });
      } else {
        _this.$notify({
          title: "上传失败",
          type: "error"
        });
      }
    },
    // 弹出删除窗口
    handleDelete(id) {
      this.idx = id;
      this.delVisible = true;
    },
    // 将已经选择的项赋值给 multipleSelection
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 批量删除已经选择的项
    delAll() {
      for (let item of this.multipleSelection) {
        this.handleDelete(item.id);
        this.deleteRow();
      }
      this.multipleSelection = [];
    }
  }
};
