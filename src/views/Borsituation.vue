<template>
  <div class="borrow-record-page">
    <el-page-header content="图书借阅记录" @back="handleBack">
      <template #title>
        <el-icon><el-icon-book-open /></el-icon>
        <span>借阅记录管理</span>
      </template>
    </el-page-header>

    <el-card class="w-full" shadow="always">
      <div v-if="isLoading" class="py-8 flex justify-center">
        <el-skeleton animated :count="6" />
      </div>

      <div v-else-if="borrowRecords.length === 0" class="py-8 flex flex-col items-center">
        <el-empty description="暂无借阅记录" />
        <el-button type="primary" @click="fetchRecords">
          <el-icon><el-icon-refresh /></el-icon> 刷新记录
        </el-button>
      </div>

      <el-table
        v-else
        :data="borrowRecords"
        stripe
        border
        size="small"
        highlight-current-row
        @row-click="handleRowClick"
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="borrowId" label="借阅ID" width="120" />
        <el-table-column prop="userId" label="用户ID" width="180" />
        <el-table-column prop="bookname" label="图书名称" width="180" />
        <el-table-column prop="borrowDate" label="借阅日期" width="140">
          <template #default="scope">
            {{ formatDate(scope.row.borrowDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="应还日期" width="140">
          <template #default="scope">
            {{ formatDate(scope.row.dueDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="returnDate" label="归还日期" width="140">
          <template #default="scope">
            {{ scope.row.returnDate ? formatDate(scope.row.returnDate) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusColor(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="scope">
            <el-button
              v-if="scope.row.status === '借阅中'"
              type="primary"
              size="mini"
              @click="handleReturn(scope.row.borrowId)"
            >
              <el-icon><el-icon-check /></el-icon> 归还
            </el-button>
            <el-button
              type="info"
              size="mini"
              @click="handleDetail(scope.row.borrowId)"
              style="margin-left: 8px"
            >
              <el-icon><el-icon-info /></el-icon> 详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { borrowerListService } from '@/api/borrow';
import { useTokenStore } from '@/stores/token';
import { ElMessage } from 'element-plus';
import dayjs from 'dayjs';

interface BorrowRecord {
  borrowId: number;
  userId: string;
  bookname: string;
  bookId: number;
  borrowDate: string;
  dueDate: string;
  returnDate?: string | null;
  status: '借阅中' | '已归还' | '逾期';
}

interface ApiResponse {
  code: number;
  msg: string;
  data: BorrowRecord[];
}

const tokenStore = useTokenStore();
const isLoading = ref(false);
const borrowRecords = ref<BorrowRecord[]>([]);

onMounted(() => {
  fetchRecords();
});

const fetchRecords = async () => {
  isLoading.value = true;
  try {
    const response: ApiResponse = await borrowerListService();
    if (response.code === 0) {
      borrowRecords.value = response.data;
    } else {
      ElMessage.error(response.msg || '获取记录失败');
    }
  } catch (error) {
    ElMessage.error('网络请求失败，请重试');
    console.error('请求错误:', error);
  } finally {
    isLoading.value = false;
  }
};

const formatDate = (date: string) => dayjs(date).format('YYYY-MM-DD');

const getStatusColor = (status: string) => ({
  '借阅中': 'warning',
  '已归还': 'success',
  '逾期': 'danger'
}[status]);

const handleReturn = (borrowId: number) => {
  ElMessage.success(`已处理归还操作：${borrowId}`);
};

const handleDetail = (borrowId: number) => {
  console.log('查看详情:', borrowId);
};

const handleBack = () => {
  console.log('返回上一页');
};
</script>

<style scoped>
.borrow-record-page {
  padding: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

/* 卡片容器，模仿 .main-form */
.el-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-radius: 12px;
  padding: 24px;
  margin-top: 24px;
  transition: box-shadow 0.3s ease;
}

.el-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 页头样式，模仿参考组件的 .header 布局 */
.el-page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

/* 表格容器及表格本身，模仿 .table-container 和 .book-table */
.el-table {
  width: 100%;
  margin-top: 24px;
  font-size: 0.95rem;
  --el-table-header-text-color: #606266;
  --el-table-header-bg-color: #f5f7fa;
}

.el-table__cell {
  white-space: nowrap;
  min-width: 60px;
  padding: 12px 8px;
}

/* 空状态，模仿 .empty-tip */
.py-8.flex.flex-col.items-center {
  color: #6c757d;
  text-align: center;
  margin-top: 16px;
}

/* 按钮样式，对齐参考组件的按钮 */
.el-button {
  padding: 8px 20px;
  font-size: 1.1rem;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.el-button--primary {
  background-color: #409eff;
  border-color: #409eff;
}

.el-button--info {
  background-color: #909399;
  border-color: #909399;
}

/* 标签样式，模仿状态颜色逻辑 */
.el-tag {
  padding: 0 8px;
  border-radius: 4px;
}

/* 滚动条样式，保持一致 */
.el-table::-webkit-scrollbar {
  height: 8px;
}

.el-table::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.el-table::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.el-table::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式，模仿参考组件的移动端适配 */
@media (max-width: 768px) {
  .borrow-record-page {
    padding: 16px;
  }
  
  .el-table__cell {
    font-size: 12px;
    min-width: 50px;
  }
  
  .el-page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>