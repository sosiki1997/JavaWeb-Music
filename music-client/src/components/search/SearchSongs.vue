<template>
  <!-- 搜索音乐 歌曲页面 -->
  <div class="search-songs">
    <album-content :songList="listOfSongs"></album-content>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { mixin } from "../../mixins";
import AlbumContent from "../AlbumContent";

export default {
  name: "search-songs",
  components: {
    AlbumContent
  },
  mixins: [mixin],
  computed: {
    ...mapGetters([
      "listOfSongs" // 存放的音乐
    ])
  },
  mounted() {
    // 在页面加载时执行 getSong
    this.getSong();
  }
};
</script>

<style lang="scss" scoped>
@import "../../assets/css/search-songs.scss";
</style>
