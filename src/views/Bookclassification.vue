<template>
  <el-card shadow="page-container">
    <!-- 卡片头部 -->
    <template #header>
      <div class="header">
        <span>图书种类</span>
        <div class="extra">
          <el-button type="primary" size="default" @click="dialogVisible = true">
            添加分类
          </el-button>
        </div>
      </div>
    </template>

    <!-- 添加分类弹窗 -->
    <el-dialog
      title="图书种类添加"
      v-model="dialogVisible"
      width="30%"
      @close="handleDialogClose"
    >
      <el-form
        :model="categoryForm"
        :rules="rules"
        label-width="80px"
        :inline="false"
        size="normal"
      >
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="categoryForm.categoryName" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 表格组件 -->
    <el-table
      :data="categories"
      border
      stripe
      @selection-change="handleSelectionChange"
      v-loading="isLoading"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column type="index" width="50" />

      <!-- 动态表格列 -->
      <el-table-column
        v-for="col in tableColumns"
        :key="col.prop"
        :prop="col.prop"
        :label="col.label"
        :width="col.width"
      />

      <!-- 操作列 -->
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button
            size="small"
            type="primary"
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
            :disabled="isDeleting"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑分类弹窗 -->
    <el-dialog
      title="图书种类编辑"
      v-model="editDialogVisible"
      width="30%"
      @close="handleEditDialogClose"
    >
      <el-form
        :model="editForm"
        :rules="rules"
        label-width="80px"
        :inline="false"
        size="normal"
      >
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="editForm.categoryName" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEditSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { bookCategoryListService, categoryAddService, categoryUpdateService,categoryDeleteService } from '@/api/category.js'; 

// 状态管理
const isLoading = ref(false);
const isDeleting = ref(false);
const categories = ref([]);
const selectedRows = ref([]);
const dialogVisible = ref(false);
const editDialogVisible = ref(false); // 编辑弹窗显示状态

// 添加表单数据
const categoryForm = reactive({
  categoryName: ''
});

// 编辑表单数据（包含ID）
const editForm = reactive({
  categoryId: '',
  categoryName: ''
});
const deleteForm = reactive({
  categoryId: '',
  categoryName: ''
});


// 表格列配置
const tableColumns = reactive([
  { prop: 'categoryId', label: '分类ID', width: '120' },
  { prop: 'categoryName', label: '分类名称', width: '180' },
  { prop: 'createTime', label: '创建时间', width: '240' },
]);

// 表单验证规则
const rules = reactive({
  categoryName: [
    { required: true, message: '请输入分类名称', trigger: 'blur' }
  ]
});

// 生命周期：获取数据
onMounted(() => {
  fetchCategories();
});

// 获取分类数据
const fetchCategories = async () => {
  try {
    isLoading.value = true;
    const result = await bookCategoryListService();
    categories.value = result.data || [];
  } catch (error) {
    ElMessage.error('获取分类数据失败');
    console.error('错误:', error);
  } finally {
    isLoading.value = false;
  }
};

// 选中行处理
const handleSelectionChange = (rows) => {
  selectedRows.value = rows;
};

// 编辑按钮点击事件
const handleEdit = (row) => {
  // 填充编辑表单数据
  editForm.categoryId = row.categoryId;
  editForm.categoryName = row.categoryName;
  // 显示编辑弹窗
  editDialogVisible.value = true;
};


// 删除处理
const handleDelete = async (row) => {
  try {
    isDeleting.value = true;
    const confirm = await ElMessageBox.confirm(
      `确定要删除分类 ${row.categoryName} 吗？`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );

    if (confirm === 'confirm') {
      deleteForm.categoryId=row.categoryId,
       deleteForm.categoryName=row.categoryName
      const response = await categoryDeleteService(deleteForm);

      if (response.code === 0) {
        ElMessage.success('删除成功');
        fetchCategories();
      } else {
        ElMessage.error(response.msg || '删除失败');
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除操作失败');
      console.error('错误:', error);
    }
  } finally {
    isDeleting.value = false;
  }
};

// 关闭添加弹窗时重置表单
const handleDialogClose = () => {
  categoryForm.categoryName = '';
};

// 关闭编辑弹窗时重置表单
const handleEditDialogClose = () => {
  editForm.categoryId = 0;
  editForm.categoryName = '';
};

// 添加分类提交
const handleAddSubmit = async () => {
  try {
    const result = await categoryAddService(categoryForm);
    if (result.code === 0) {
      ElMessage.success(result.msg || '添加成功');
      fetchCategories();
      dialogVisible.value = false;
    } else {
      ElMessage.error(result.msg || '添加失败');
    }
  } catch (error) {
    ElMessage.error('添加分类失败');
    console.error('错误:', error);
  }
};

// 编辑分类提交
const handleEditSubmit = async () => {
  try {
    const result = await categoryUpdateService(editForm);
    if (result.code === 0) {
      ElMessage.success(result.msg || '编辑成功');
      fetchCategories();
      editDialogVisible.value = false;
    } else {
      ElMessage.error(result.msg || '编辑失败');
    }
  } catch (error) {
    ElMessage.error('编辑分类失败');
    console.error('错误:', error);
  }
};
</script>
<style scoped>
@import '@/Style/common.css';
/* 重置外层容器样式 */
:deep(.el-card__body) {
  padding: 0 !important;
  height: 100%;
}

/* 主容器样式 */
.page-container {
  width: 100vw;
  height: 100vh;
  margin: 0;
  padding: 24px;
  box-sizing: border-box;
  overflow: hidden;
}

.el-card {
  width: 100%;
  height: 100%;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 表格容器样式 */
.table-container {
  flex: 1;
  padding: 24px;
  overflow: auto;
}

/* 表格样式 */
.el-table {
  width: 100% !important;
  font-size: 0.95rem;
  --el-table-header-text-color: #606266;
  --el-table-header-bg-color: #f5f7fa;
}

.el-table__cell {
  white-space: nowrap;
  min-width: 60px;
  padding: 12px 8px;
}

/* 强制覆盖Element Plus默认样式 */
:deep(.el-card) {
  --el-card-padding: 0;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .page-container {
    padding: 16px;
  }
  
  .table-container {
    padding: 16px;
  }
  
  .el-table__cell {
    font-size: 12px;
    min-width: 50px;
  }
}
</style>