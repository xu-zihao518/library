<template>
  <div class="book-component">
    <!-- 主表单：图书列表 -->
    <el-card class="main-form">
      <div class="header">
        <h2>图书管理系统</h2>
        <el-button 
          type="primary" 
          @click="isAddVisible = true"
          class="add-btn"
        >
          <el-icon><Plus /></el-icon> 添加图书
        </el-button>
      </div>

      <!-- 图书列表 -->
       <div class="table-container" style="min-height: 300px;">
      <el-table
  v-if="books.length > 0"
  :data="books"
  border
  stripe
  class="book-table"
>
  <el-table-column type="index" label="序号" width="60"></el-table-column>
  <el-table-column prop="bookId" label="ID" width="100"></el-table-column>
  <el-table-column prop="bookName" label="书名" min-width="150"></el-table-column>
  <el-table-column prop="author" label="作者" width="120"></el-table-column>
  <el-table-column prop="publisher" label="出版社" width="150"></el-table-column>
  <el-table-column prop="category" label="分类" width="100"></el-table-column>
  <el-table-column prop="publicationDate" label="出版日期" width="120">
    <template #default="scope">
      {{ formatDate(scope.row.publicationDate) }}
    </template>
  </el-table-column>
  <el-table-column prop="count" label="库存" width="80">
    <template #default="scope">
      <span :class="getStockClass(scope.row.count)">{{ scope.row.count }}</span>
    </template>
  </el-table-column>
  <!-- 新增操作列 -->
  <el-table-column label="操作" width="100">
    <template #default="scope">
      <el-button
        type="danger"
        size="small"
        @click="handleDelete(scope.row)"
      >
        删除
      </el-button>
    </template>
  </el-table-column>
</el-table>

      <div v-else class="empty-tip">当前无图书数据</div>
       </div>
    </el-card>
 
    <!-- 添加图书表单（模态窗口） -->
    <el-dialog
      v-model="isAddVisible"
      title="新增图书"
      width="600px"
      class="add-dialog"
      @close="resetAddForm"
    >
      <el-form
        :model="addForm"
        :rules="addRules"
        ref="addFormRef"
        label-width="100px"
      >
        <el-form-item label="ISBN" prop="bookISBN">
          <el-input 
            v-model="addForm.bookISBN" 
            placeholder="请输入ISBN"
          ></el-input>
        </el-form-item>

        <el-form-item label="书名" prop="bookName">
          <el-input 
            v-model="addForm.bookName" 
            placeholder="请输入书名"
          ></el-input>
        </el-form-item>

        <el-form-item label="作者" prop="author">
          <el-input 
            v-model="addForm.author" 
            placeholder="请输入作者"
          ></el-input>
        </el-form-item>

        <el-form-item label="出版社" prop="publisher">
          <el-input 
            v-model="addForm.publisher" 
            placeholder="请输入出版社"
          ></el-input>
        </el-form-item>

        <el-form-item label="出版日期" prop="publicationDate">
          <el-date-picker
            v-model="addForm.publicationDate"
            type="date"
            placeholder="请选择出版日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          ></el-date-picker>
        </el-form-item>

          <el-form-item label="分类" prop="category">
            <el-select 
              v-model="addForm.category" 
              placeholder="请选择分类"
              :disabled="isCategoryLoading"
            >
              <el-option
                v-for="cat in categories"
                :key="cat.value"
                :label="cat.label"
                :value="cat.value"
              ></el-option>
              <template #empty>
                <div v-if="isCategoryLoading">加载分类中...</div>
                <div v-else>暂无可用分类</div>
              </template>
            </el-select>
          </el-form-item>

        <el-form-item label="库存数量" prop="count">
          <el-input-number
            v-model="addForm.count"
            :min="0"
            placeholder="请输入库存数量"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input
            v-model="addForm.description"
            type="textarea"
            placeholder="请输入图书描述"
            rows="3"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="isAddVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { ElMessage,ElMessageBox } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { bookListService, bookAddService } from '@/api/book.js'
import { onMounted } from 'vue';
import { bookCategoryListService } from '@/api/category.js'
import { bookDeleteService } from '@/api/book.js';
// 模拟图书数据
const books = ref([]);

// 添加表单状态
const isAddVisible = ref(false);
const addForm = reactive({
  bookISBN: '',
  bookName: '',
  author: '',
  publisher: '',
  publicationDate: '',
  category: null,
  count: 0,
  description: ''
});

// 分类选项
const categories = ref([]);
const addFormRef = ref();
const isCategoryLoading = ref(true);
onMounted(()=>{
  fetchBooks();
  fetchCategories();
})
// 获取分类数据（核心逻辑）
const fetchCategories = async () => {
  try {
    isCategoryLoading.value = true;
    const result = await bookCategoryListService();
    
    if (result.code === 0 && Array.isArray(result.data)) {
      // 转换为 { value: categoryId, label: categoryName } 格式
      categories.value = result.data.map(item => ({
        value: item.categoryId,
        label: item.categoryName
      }));
      
      // 处理无分类数据的情况
      if (categories.value.length === 0) {
        ElMessage.warning('当前没有可用分类，请先添加');
      }
    } else {
      ElMessage.error(`获取分类失败：${result.msg || '未知错误'}`);
      // 设置默认分类（可选）
      categories.value = [
        { value: null, label: '无分类' }
      ];
    }
  } catch (error) {
    ElMessage.error('分类数据加载失败，请检查网络');
    categories.value = [
      { value: null, label: '加载失败' }
    ];
  } finally {
    isCategoryLoading.value = false;
  }
};
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
// 表单验证规则
const addRules = reactive({
  bookISBN: [
    { required: true, message: '请输入ISBN', trigger: 'blur' },
    { pattern: /^978[\d\-]{10,13}$/, message: '须以 978 开头,后面跟随 10 到 13 个字符，这些字符可以是 数字 或 连字符 -。', trigger: 'blur' }
  ],
  bookName: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  publisher: [{ required: true, message: '请输入出版社', trigger: 'blur' }],
  publicationDate: [{ required: true, message: '请选择出版日期', trigger: 'change' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' ,type: 'number' }],
  count: [{ required: true, message: '请输入库存数量', trigger: 'blur' }]
});

// 日期格式化函数
const formatDate = (dateStr) => {
  return dateStr ? new Date(dateStr).toLocaleDateString('zh-CN') : '';
};

// 库存状态样式
const getStockClass = (count) => {
  if (count === 0) return 'text-danger';
  if (count < 50) return 'text-warning';
  return 'text-success';
};

// 提交添加表单
const handleAddSubmit = async () => {
  if (!addFormRef.value) return;
  addFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const result = await bookAddService(addForm);
        if (result.code === 0) {
          ElMessage.success('添加成功');
          isAddVisible.value = false;
          resetAddForm();
          fetchBooks();
        } else {
          ElMessage.error(result.msg || '添加失败');
        }
      } catch (error) {
        ElMessage.error('网络请求失败');
        console.error(error);
      }
    }
  });
};
const delectForm=ref({})

// 重置添加表单
const resetAddForm = () => {
  addForm.bookISBN = '';
  addForm.bookName = '';
  addForm.author = '';
  addForm.publisher = '';
  addForm.publicationDate = '';
  addForm.category = '';
  addForm.count = 0;
  addForm.desc = '';
  
  if (addFormRef.value) {
    addFormRef.value.resetFields();
  }
};

// 删除图书
const handleDelete = async (row) => {
  try {
    // 确认删除弹窗
    const confirm = await ElMessageBox.confirm(
      `确定要删除图书《${row.bookName}》吗？`,
      '警告',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );

    if (confirm !== 'confirm') return; // 用户取消操作

    // 调用删除接口（传入图书ID）
    delectForm.value.bookId=row.bookId;
    delectForm.value.bookName=row.bookName;
    const result = await bookDeleteService(delectForm.value);
  
    if (result.code === 0) {
      ElMessage.success('删除成功');
      fetchBooks(); // 刷新图书列表
    } else {
      ElMessage.error(result.msg || '删除失败，请重试');
    }
  } catch (error) {
    if (error !== 'cancel') { // 忽略用户取消的错误
      ElMessage.error('删除操作失败');
      console.error('删除失败:', error);
    }
  }
};
</script>

<style scoped>
@import '@/Style/common.css';
.book-component {
  padding: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

.main-form {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-radius: 12px;
  padding: 24px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.add-btn {
  padding: 8px 20px;
  font-size: 1.1rem;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.book-table {
  margin-top: 24px;
  font-size: 0.95rem;
  --el-table-header-text-color: #606266;
  --el-table-header-bg-color: #f5f7fa;
}

.book-table :deep(.el-table__cell) {
  white-space: nowrap;
  min-width: 60px;
  padding: 12px 8px;
}

.empty-tip {
  color: #6c757d;
  text-align: center;
  margin-top: 16px;
}

.add-dialog .el-form-item {
  margin-bottom: 16px;
}

/* 库存状态颜色 */
.text-success { color: #28a745; }
.text-warning { color: #ffc107; }
.text-danger { color: #dc3545; }

/* 响应式设计 */
@media (max-width: 768px) {
  .book-component {
    padding: 16px;
  }
  
  .main-form {
    padding: 16px;
  }
  
  .book-table :deep(.el-table__cell) {
    font-size: 12px;
    min-width: 50px;
  }
}
</style>