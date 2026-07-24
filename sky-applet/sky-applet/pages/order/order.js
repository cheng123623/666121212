const api = require('../../api/index');
const statusMap = { 1: '待付款', 2: '待接单', 3: '已接单', 4: '派送中', 5: '已完成', 6: '已取消' };
Page({
  data: { orders: [], currentStatus: '', statusTabs: [
    { label: '全部', value: '' }, { label: '待付款', value: 1 }, { label: '待接单', value: 2 },
    { label: '已接单', value: 3 }, { label: '派送中', value: 4 }, { label: '已完成', value: 5 }
  ]},
  onShow() { this.loadOrders(); },
  loadOrders() { api.getOrders(this.data.currentStatus).then(data => { const orders = (data || []).map(o => ({ ...o, statusText: statusMap[o.status] || '未知' })); this.setData({ orders }); }); },
  switchTab(e) { this.setData({ currentStatus: e.currentTarget.dataset.status }); this.loadOrders(); },
  goDetail(e) { wx.navigateTo({ url: '/pages/order-detail/order-detail?id=' + e.currentTarget.dataset.id }); }
});
