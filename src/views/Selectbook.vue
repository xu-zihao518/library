<template>
  <div class="book-form-container">
    <!-- 搜索栏 -->
    <div class="search-section">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入书名搜索"
        class="search-input"
      >
        <template #append>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </div>

    <!-- 图书列表 -->
    <el-table
      :data="filteredBooks"
      border
      style="width: 100%; margin-top: 20px"
      @row-click="handleRowClick"
    >
      <el-table-column type="index" label="序号" width="50"></el-table-column>
      <el-table-column prop="bookId" label="图书ID" width="120"></el-table-column>
      <el-table-column prop="bookName" label="书名" width="200"></el-table-column>
      <el-table-column prop="author" label="作者" width="150"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      title="图书详情"
      width="500px"
    >
      <el-form :model="detailBook" label-width="100px">
        <el-form-item label="图书ID:">
          <span>{{ detailBook.bookId }}</span>
        </el-form-item>
        <el-form-item label="书名:">
          <span>{{ detailBook.bookName }}</span>
        </el-form-item>
         <el-form-item label="书的ISBN:">
          <span>{{ detailBook.bookISBN }}</span>
        </el-form-item>
        <el-form-item label="作者:">
          <span>{{ detailBook.author }}</span>
        </el-form-item>
        <el-form-item label="出版社:">
          <span>{{ detailBook.publisher }}</span>
        </el-form-item>
        <el-form-item label="出版日期:">
          <span>{{ formatDate(detailBook.publicationDate) }}</span>
        </el-form-item>
        <el-form-item label="分类:">
          <span>{{ detailBook.category }}</span>
        </el-form-item>
        <el-form-item label="库存数量:">
          <span :class="getStockClass(detailBook.count)">{{ detailBook.count }} 本</span>
        </el-form-item>
        <el-form-item label="描述:">
          <p style="line-height: 1.6; color: #6c757d;">{{ detailBook.description }}</p>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { bookListService } from '@/api/book.js'; // 假设这是实际的接口服务

// 状态管理
const books = ref([]); // 存储图书列表数据
const searchKeyword = ref('');
const detailVisible = ref(false);
const detailBook = ref({});

// 初始化获取图书列表
onMounted(() => {
  fetchBooks();
});

// 获取图书列表的方法
const fetchBooks = async () => {
  try {
    const result = await bookListService(); // 调用实际接口
    if (result.code === 0) {
      books.value = result.data; // 将接口返回的data数组赋值给books
    } else {
      ElMessage.error('获取图书列表失败');
    }
  } catch (error) {
    ElMessage.error('网络请求失败');
    console.error(error);
  }
};

const filteredBooks = computed(() => {
  const keyword = searchKeyword.value.trim();
  if (!keyword) return books.value; // 无搜索词时显示全部数据
  return books.value.filter(book => 
    book.bookName.includes(keyword) // 根据书名进行模糊匹配
  );
});

// 日期格式化函数
const formatDate = (dateStr) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
};

// 库存状态样式函数
const getStockClass = (count) => {
  if (count === 0) return 'text-danger';
  if (count < 50) return 'text-warning';
  return 'text-success';
};

// 点击行或详情按钮查看详情
const handleDetail = (book) => {
  detailBook.value = { ...book }; // 浅拷贝当前图书数据
  detailVisible.value = true; // 显示详情弹窗
};

// 搜索方法（可扩展为带参数的接口请求）
const handleSearch = () => {
  const keyword = searchKeyword.value.trim();
  if (!keyword) {
    ElMessage.warning('请输入搜索关键词');
    return;
  }
  ElMessage.success(`正在搜索：${keyword}`);
};
</script>

<style scoped>
@import '@/Style/common.css';
.book-form-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.search-section {
  margin-bottom: 24px;
}

.search-input {
  max-width: 600px;
  margin: 0 auto;
}

.table-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-radius: 12px;
  padding: 24px;
}

.book-table {
  width: 100%;
  margin-top: 16px;
  font-size: 0.95rem;
  --el-table-header-text-color: #606266;
  --el-table-header-bg-color: #f5f7fa;
}

.book-table :deep(.el-table__cell) {
  white-space: nowrap;
  min-width: 60px;
  padding: 12px 8px;
}

.detail-dialog .el-form-item {
  margin-bottom: 16px;
}

.detail-value {
  color: #333;
  font-weight: 500;
  font-size: 1em;
}

.detail-description {
  line-height: 1.6;
  color: #6c757d;
  margin: 0;
}

.text-success { color: #28a745; }
.text-warning { color: #ffc107; }
.text-danger { color: #dc3545; }

/* 响应式设计 */
@media (max-width: 768px) {
  .book-form-container {
    padding: 16px;
  }
  
  .table-card {
    padding: 16px;
  }
  
  .search-input {
    max-width: 100%;
  }
  
  .book-table :deep(.el-table__cell) {
    font-size: 12px;
    min-width: 50px;
    padding: 8px 4px;
  }
  
  .detail-dialog {
    width: 90%;
  }
}
</style>