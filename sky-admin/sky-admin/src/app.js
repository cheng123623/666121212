// API 配置
axios.defaults.baseURL = 'http://localhost:8080/admin';
axios.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) config.headers['token'] = token;
    return config;
});
axios.interceptors.response.use(res => {
    if (res.data.code === 0) { ElementUI.Message.error(res.data.msg); return Promise.reject(res.data.msg); }
    return res.data;
});

Vue.use(ElementUI, { locale: ElementUI.locale.zhCN });

// ==================== 登录页 ====================
const Login = {
    template: `<div class="login-container"><div class="login-card">
        <h2>苍穹外卖管理系统</h2>
        <el-form :model="form" :rules="rules" ref="formRef">
            <el-form-item prop="username"><el-input v-model="form.username" placeholder="用户名" prefix-icon="el-icon-user"></el-input></el-form-item>
            <el-form-item prop="password"><el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="el-icon-lock"></el-input></el-form-item>
            <el-form-item><el-button type="primary" @click="login" style="width:100%">登 录</el-button></el-form-item>
        </el-form></div></div>`,
    data() { return { form: { username: 'admin', password: '123456' }, rules: { username: [{ required: true, message: '请输入用户名' }], password: [{ required: true, message: '请输入密码' }] } }; },
    methods: {
        login() { this.$refs.formRef.validate(valid => { if (valid) { axios.post('/employee/login', this.form).then(res => { localStorage.setItem('token', res.data.token); localStorage.setItem('userInfo', JSON.stringify(res.data)); this.$router.push('/main'); }); } }); }
    }
};

// ==================== 主页布局 ====================
const MainLayout = {
    template: `<div class="main-container">
        <div class="sidebar"><div class="logo">苍穹外卖</div>
            <el-menu router :default-active="$route.path" background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF">
                <el-menu-item index="/main/employee"><i class="el-icon-user"></i>员工管理</el-menu-item>
                <el-menu-item index="/main/category"><i class="el-icon-menu"></i>分类管理</el-menu-item>
                <el-menu-item index="/main/dish"><i class="el-icon-food"></i>菜品管理</el-menu-item>
                <el-menu-item index="/main/setmeal"><i class="el-icon-s-grid"></i>套餐管理</el-menu-item>
                <el-menu-item index="/main/order"><i class="el-icon-s-order"></i>订单管理</el-menu-item>
                <el-menu-item index="/main/report"><i class="el-icon-data-analysis"></i>数据统计</el-menu-item>
            </el-menu>
        </div>
        <div class="main-area">
            <div class="topbar"><h3>苍穹外卖管理系统</h3><el-button type="text" @click="logout">退出登录</el-button></div>
            <div class="content"><router-view></router-view></div>
        </div></div>`,
    methods: { logout() { localStorage.clear(); this.$router.push('/login'); } }
};

// ==================== 员工管理 ====================
const Employee = {
    template: `<div>
        <div class="search-bar"><el-form :inline="true"><el-form-item><el-input v-model="searchName" placeholder="员工姓名" clearable></el-input></el-form-item>
            <el-form-item><el-button type="primary" @click="loadData">查询</el-button><el-button type="success" @click="showAdd">新增员工</el-button></el-form-item></el-form></div>
        <div class="table-card"><el-table :data="list" border><el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column><el-table-column prop="phone" label="手机号"></el-table-column>
            <el-table-column prop="sex" label="性别" :formatter="r=>r.sex=='1'?'男':'女'"></el-table-column>
            <el-table-column prop="idNumber" label="身份证号"></el-table-column>
            <el-table-column label="状态"><template slot-scope="s"><el-switch v-model="s.row.status==1" @change="toggleStatus(s.row)" active-color="#13ce66"></el-switch></template></el-table-column>
            <el-table-column label="操作" width="100"><template slot-scope="s"><el-button type="text" @click="showEdit(s.row)">编辑</el-button></template></el-table-column></el-table>
            <el-pagination style="margin-top:20px" @current-change="loadData" :total="total" :page-size="10" layout="total,prev,next"></el-pagination></div>
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px"><el-form :model="form" :rules="rules" ref="formRef">
            <el-form-item label="姓名" prop="name"><el-input v-model="form.name"></el-input></el-form-item>
            <el-form-item label="用户名" prop="username"><el-input v-model="form.username" :disabled="!!form.id"></el-input></el-form-item>
            <el-form-item label="手机号" prop="phone"><el-input v-model="form.phone"></el-input></el-form-item>
            <el-form-item label="性别"><el-radio-group v-model="form.sex"><el-radio label="1">男</el-radio><el-radio label="0">女</el-radio></el-radio-group></el-form-item>
            <el-form-item label="身份证号" prop="idNumber"><el-input v-model="form.idNumber"></el-input></el-form-item>
        </el-form><div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></div></el-dialog></div>`,
    data() { return { searchName: '', list: [], total: 0, dialogVisible: false, dialogTitle: '', form: {}, rules: { name: [{ required: true }], username: [{ required: true }], phone: [{ required: true }], idNumber: [{ required: true }] } }; },
    created() { this.loadData(); },
    methods: {
        loadData() { axios.get('/employee/page', { params: { name: this.searchName, page: 1, pageSize: 999 } }).then(res => { this.list = res.data.records; this.total = res.data.total; }); },
        showAdd() { this.dialogTitle = '新增员工'; this.form = { sex: '1' }; this.dialogVisible = true; this.$nextTick(() => this.$refs.formRef && this.$refs.formRef.resetFields()); },
        showEdit(row) { this.dialogTitle = '编辑员工'; this.form = { ...row }; this.dialogVisible = true; },
        submit() { this.$refs.formRef.validate(valid => { if (!valid) return; const api = this.form.id ? axios.put('/employee', this.form) : axios.post('/employee', this.form); api.then(() => { this.dialogVisible = false; this.loadData(); ElementUI.Message.success('操作成功'); }); }); },
        toggleStatus(row) { axios.post('/employee/status/' + (row.status == 1 ? 0 : 1) + '?id=' + row.id).then(() => this.loadData()); }
    }
};

// ==================== 分类管理 ====================
const Category = {
    template: `<div>
        <div class="search-bar"><el-tabs v-model="currentType" @tab-click="loadData"><el-tab-pane label="菜品分类" name="1"></el-tab-pane><el-tab-pane label="套餐分类" name="2"></el-tab-pane></el-tabs>
        <el-form :inline="true"><el-form-item><el-input v-model="searchName" placeholder="分类名称"></el-input></el-form-item>
            <el-form-item><el-button type="primary" @click="loadData">查询</el-button><el-button type="success" @click="showAdd">新增分类</el-button></el-form-item></el-form></div>
        <div class="table-card"><el-table :data="list" border><el-table-column prop="name" label="分类名称"></el-table-column>
            <el-table-column prop="sort" label="排序"></el-table-column><el-table-column label="状态"><template slot-scope="s"><el-switch v-model="s.row.status==1" @change="toggleStatus(s.row)"></el-switch></template></el-table-column>
            <el-table-column label="操作" width="180"><template slot-scope="s"><el-button type="text" @click="showEdit(s.row)">编辑</el-button><el-button type="text" style="color:#F56C6C" @click="del(s.row.id)">删除</el-button></template></el-table-column></el-table></div>
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="400px"><el-form :model="form"><el-form-item label="分类名称"><el-input v-model="form.name"></el-input></el-form-item>
            <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0"></el-input-number></el-form-item></el-form>
        <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></div></el-dialog></div>`,
    data() { return { currentType: '1', searchName: '', list: [], dialogVisible: false, dialogTitle: '', form: {} }; },
    created() { this.loadData(); },
    methods: {
        loadData() { axios.get('/category/list?type=' + this.currentType).then(res => { this.list = res.data || []; }); },
        showAdd() { this.dialogTitle = '新增分类'; this.form = { type: parseInt(this.currentType), sort: 0 }; this.dialogVisible = true; },
        showEdit(row) { this.dialogTitle = '编辑分类'; this.form = { ...row }; this.dialogVisible = true; },
        submit() { const api = this.form.id ? axios.put('/category', this.form) : axios.post('/category', this.form); api.then(() => { this.dialogVisible = false; this.loadData(); }); },
        toggleStatus(row) { axios.post('/category/status/' + (row.status == 1 ? 0 : 1) + '?id=' + row.id).then(() => this.loadData()); },
        del(id) { this.$confirm('确认删除?').then(() => axios.delete('/category?id=' + id).then(() => this.loadData())).catch(() => {}); }
    }
};

// ==================== 菜品管理 ====================
const Dish = {
    template: `<div>
        <div class="search-bar"><el-form :inline="true"><el-form-item><el-input v-model="searchName" placeholder="菜品名称"></el-input></el-form-item>
            <el-form-item><el-select v-model="searchCategoryId" placeholder="分类" clearable><el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id"></el-option></el-select></el-form-item>
            <el-form-item><el-select v-model="searchStatus" placeholder="状态" clearable><el-option label="起售" :value="1"></el-option><el-option label="停售" :value="0"></el-option></el-select></el-form-item>
            <el-form-item><el-button type="primary" @click="loadData">查询</el-button><el-button type="success" @click="showAdd">新增菜品</el-button></el-form-item></el-form></div>
        <div class="table-card"><el-table :data="list" border>
            <el-table-column prop="name" label="菜品名称"></el-table-column><el-table-column prop="image" label="图片"><template slot-scope="s"><img :src="'http://localhost:8080/uploads/'+s.row.image" style="width:60px;height:60px" v-if="s.row.image"></template></el-table-column>
            <el-table-column prop="categoryName" label="分类"></el-table-column><el-table-column prop="price" label="价格(¥)"></el-table-column>
            <el-table-column label="状态"><template slot-scope="s"><el-switch v-model="s.row.status==1" @change="toggleStatus(s.row)"></el-switch></template></el-table-column>
            <el-table-column label="操作" width="150"><template slot-scope="s"><el-button type="text" @click="showEdit(s.row)">编辑</el-button><el-button type="text" style="color:#F56C6C" @click="del(s.row.id)">删除</el-button></template></el-table-column></el-table></div>
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px"><el-form :model="form">
            <el-form-item label="菜品名称"><el-input v-model="form.name"></el-input></el-form-item>
            <el-form-item label="分类"><el-select v-model="form.categoryId"><el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id"></el-option></el-select></el-form-item>
            <el-form-item label="价格"><el-input-number v-model="form.price" :precision="2" :min="0"></el-input-number></el-form-item>
            <el-form-item label="图片"><el-upload action="/common/upload" :on-success="uploadSuccess" :file-list="[]" list-type="picture"><el-button size="small" type="primary">上传图片</el-button></el-upload><img v-if="form.image" :src="'http://localhost:8080/uploads/'+form.image" style="width:100px;margin-top:5px"></el-form-item>
            <el-form-item label="描述"><el-input v-model="form.description" type="textarea"></el-input></el-form-item>
            <el-form-item label="口味"><el-button size="small" @click="addFlavor">添加口味</el-button>
                <div v-for="(f,idx) in form.flavors" :key="idx" style="margin-top:5px"><el-input v-model="f.name" placeholder="口味名称" style="width:120px"></el-input><el-input v-model="f.value" placeholder="口味值(逗号分隔)" style="width:300px;margin-left:5px"></el-input><el-button type="danger" size="mini" @click="form.flavors.splice(idx,1)">删除</el-button></div>
            </el-form-item></el-form>
        <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></div></el-dialog></div>`,
    data() { return { searchName: '', searchCategoryId: null, searchStatus: null, list: [], categories: [], dialogVisible: false, dialogTitle: '', form: { flavors: [], price: 0 } }; },
    created() { this.loadCategories(); this.loadData(); },
    methods: {
        loadCategories() { axios.get('/category/list?type=1').then(res => this.categories = res.data || []); },
        loadData() { axios.get('/dish/page', { params: { name: this.searchName, categoryId: this.searchCategoryId, status: this.searchStatus, page: 1, pageSize: 999 } }).then(res => this.list = res.data.records); },
        showAdd() { this.dialogTitle = '新增菜品'; this.form = { flavors: [], price: 0 }; this.dialogVisible = true; },
        showEdit(row) { axios.get('/dish/' + row.id).then(res => { this.dialogTitle = '编辑菜品'; this.form = { ...res.data, flavors: res.data.flavors || [] }; this.dialogVisible = true; }); },
        submit() { const api = this.form.id ? axios.put('/dish', this.form) : axios.post('/dish', this.form); api.then(() => { this.dialogVisible = false; this.loadData(); }); },
        toggleStatus(row) { axios.post('/dish/status/' + (row.status == 1 ? 0 : 1) + '?id=' + row.id).then(() => this.loadData()); },
        del(id) { this.$confirm('确认删除?').then(() => axios.delete('/dish?ids=' + id).then(() => this.loadData())).catch(() => {}); },
        uploadSuccess(res) { this.form.image = res.data; },
        addFlavor() { this.form.flavors.push({ name: '', value: '' }); }
    }
};

// ==================== 套餐管理 ====================
const Setmeal = {
    template: `<div>
        <div class="search-bar"><el-form :inline="true"><el-form-item><el-input v-model="searchName" placeholder="套餐名称"></el-input></el-form-item>
            <el-form-item><el-select v-model="searchCategoryId" placeholder="分类" clearable><el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id"></el-option></el-select></el-form-item>
            <el-form-item><el-button type="primary" @click="loadData">查询</el-button><el-button type="success" @click="showAdd">新增套餐</el-button></el-form-item></el-form></div>
        <div class="table-card"><el-table :data="list" border>
            <el-table-column prop="name" label="套餐名称"></el-table-column><el-table-column prop="image" label="图片"><template slot-scope="s"><img :src="'http://localhost:8080/uploads/'+s.row.image" style="width:60px;height:60px" v-if="s.row.image"></template></el-table-column>
            <el-table-column prop="categoryName" label="分类"></el-table-column><el-table-column prop="price" label="价格(¥)"></el-table-column>
            <el-table-column label="状态"><template slot-scope="s"><el-switch v-model="s.row.status==1" @change="toggleStatus(s.row)"></el-switch></template></el-table-column>
            <el-table-column label="操作" width="150"><template slot-scope="s"><el-button type="text" @click="showEdit(s.row)">编辑</el-button><el-button type="text" style="color:#F56C6C" @click="del(s.row.id)">删除</el-button></template></el-table-column></el-table></div>
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px"><el-form :model="form">
            <el-form-item label="套餐名称"><el-input v-model="form.name"></el-input></el-form-item>
            <el-form-item label="分类"><el-select v-model="form.categoryId"><el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id"></el-option></el-select></el-form-item>
            <el-form-item label="价格"><el-input-number v-model="form.price" :precision="2" :min="0"></el-input-number></el-form-item>
            <el-form-item label="图片"><el-upload action="/common/upload" :on-success="uploadSuccess" :file-list="[]" list-type="picture"><el-button size="small" type="primary">上传图片</el-button></el-upload></el-form-item>
            <el-form-item label="描述"><el-input v-model="form.description" type="textarea"></el-input></el-form-item></el-form>
        <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></div></el-dialog></div>`,
    data() { return { searchName: '', searchCategoryId: null, list: [], categories: [], dialogVisible: false, dialogTitle: '', form: { price: 0 } }; },
    created() { this.loadCategories(); this.loadData(); },
    methods: {
        loadCategories() { axios.get('/category/list?type=2').then(res => this.categories = res.data || []); },
        loadData() { axios.get('/setmeal/page', { params: { name: this.searchName, categoryId: this.searchCategoryId, page: 1, pageSize: 999 } }).then(res => this.list = res.data.records); },
        showAdd() { this.dialogTitle = '新增套餐'; this.form = { price: 0 }; this.dialogVisible = true; },
        showEdit(row) { axios.get('/setmeal/' + row.id).then(res => { this.dialogTitle = '编辑套餐'; this.form = res.data; this.dialogVisible = true; }); },
        submit() { const api = this.form.id ? axios.put('/setmeal', this.form) : axios.post('/setmeal', this.form); api.then(() => { this.dialogVisible = false; this.loadData(); }); },
        toggleStatus(row) { axios.post('/setmeal/status/' + (row.status == 1 ? 0 : 1) + '?id=' + row.id).then(() => this.loadData()); },
        del(id) { this.$confirm('确认删除?').then(() => axios.delete('/setmeal?ids=' + id).then(() => this.loadData())).catch(() => {}); },
        uploadSuccess(res) { this.form.image = res.data; }
    }
};

// ==================== 订单管理 ====================
const Order = {
    template: `<div>
        <div class="search-bar"><el-form :inline="true"><el-form-item><el-input v-model="searchNumber" placeholder="订单号"></el-input></el-form-item>
            <el-form-item><el-select v-model="searchStatus" placeholder="状态" clearable><el-option label="待付款" :value="1"></el-option><el-option label="待接单" :value="2"></el-option><el-option label="已接单" :value="3"></el-option><el-option label="派送中" :value="4"></el-option><el-option label="已完成" :value="5"></el-option><el-option label="已取消" :value="6"></el-option></el-select></el-form-item>
            <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item></el-form></div>
        <div class="table-card"><el-table :data="list" border>
            <el-table-column prop="number" label="订单号" width="200"></el-table-column><el-table-column prop="userName" label="用户"></el-table-column>
            <el-table-column prop="amount" label="金额(¥)"></el-table-column>
            <el-table-column label="状态"><template slot-scope="s"><el-tag :type="statusType(s.row.status)">{{statusText(s.row.status)}}</el-tag></template></el-table-column>
            <el-table-column prop="orderTime" label="下单时间" width="160" :formatter="r=>r.orderTime"></el-table-column>
            <el-table-column label="操作" width="200"><template slot-scope="s"><el-button size="mini" @click="confirm(s.row)" v-if="s.row.status==2">接单</el-button>
                <el-button size="mini" type="danger" @click="reject(s.row)" v-if="s.row.status==2">拒单</el-button>
                <el-button size="mini" type="danger" @click="cancelOrder(s.row)" v-if="[1,2,3].includes(s.row.status)">取消</el-button></template></el-table-column></el-table></div></div>`,
    data() { return { searchNumber: '', searchStatus: null, list: [] }; },
    created() { this.loadData(); },
    methods: {
        loadData() { axios.get('/order/page', { params: { number: this.searchNumber, status: this.searchStatus, page: 1, pageSize: 999 } }).then(res => this.list = res.data.records); },
        confirm(row) { axios.put('/order/confirm', { id: row.id }).then(() => this.loadData()); },
        reject(row) { this.$prompt('请输入拒单原因', '拒单').then(({value}) => axios.put('/order/rejection', { id: row.id, rejectionReason: value }).then(() => this.loadData())).catch(() => {}); },
        cancelOrder(row) { this.$prompt('请输入取消原因', '取消').then(({value}) => axios.put('/order/cancel', { id: row.id, cancelReason: value }).then(() => this.loadData())).catch(() => {}); },
        statusText(s) { return {1:'待付款',2:'待接单',3:'已接单',4:'派送中',5:'已完成',6:'已取消'}[s]; },
        statusType(s) { return {1:'warning',2:'info',3:'',4:'',5:'success',6:'danger'}[s]; }
    }
};

// ==================== 数据统计 ====================
const Report = {
    template: `<div>
        <div class="search-bar"><el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" @change="loadData"></el-date-picker></div>
        <div class="stat-cards"><div class="stat-card"><div class="num">¥{{data.turnover || 0}}</div><div class="label">营业额</div></div>
            <div class="stat-card"><div class="num">{{data.validOrderCount || 0}}</div><div class="label">有效订单数</div></div>
            <div class="stat-card"><div class="num">{{data.newUsers || 0}}</div><div class="label">新增用户</div></div></div>
        <div class="table-card"><h4>订单统计</h4><el-row :gutter="20"><el-col :span="8"><el-card>待接单: {{orderStats.toBeConfirmed}}</el-card></el-col>
            <el-col :span="8"><el-card>已接单: {{orderStats.confirmed}}</el-card></el-col>
            <el-col :span="8"><el-card>派送中: {{orderStats.deliveryInProgress}}</el-card></el-col></el-row></div></div>`,
    data() { return { dateRange: [], data: {}, orderStats: {} }; },
    created() { this.loadStats(); },
    methods: {
        loadData() { if (this.dateRange && this.dateRange.length === 2) { axios.get('/report/turnoverStatistics', { params: { begin: this.formatDate(this.dateRange[0]), end: this.formatDate(this.dateRange[1]) } }).then(res => this.data = res.data); } },
        loadStats() { axios.get('/report/orderStatistics').then(res => this.orderStats = res.data); },
        formatDate(d) { const dt = new Date(d); return dt.getFullYear() + '-' + String(dt.getMonth()+1).padStart(2,'0') + '-' + String(dt.getDate()).padStart(2,'0'); }
    }
};

// ==================== Router ====================
const router = new VueRouter({
    routes: [
        { path: '/', redirect: '/login' },
        { path: '/login', component: Login },
        { path: '/main', component: MainLayout, children: [
            { path: '', redirect: '/main/employee' },
            { path: 'employee', component: Employee },
            { path: 'category', component: Category },
            { path: 'dish', component: Dish },
            { path: 'setmeal', component: Setmeal },
            { path: 'order', component: Order },
            { path: 'report', component: Report }
        ]}
    ]
});
router.beforeEach((to, from, next) => {
    if (to.path !== '/login' && !localStorage.getItem('token')) { next('/login'); } else { next(); }
});

new Vue({ router }).$mount('#app');
