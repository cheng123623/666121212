const api = require('../../api/index');
const statusMap = { 1: '待付款', 2: '待接单', 3: '已接单', 4: '派送中', 5: '已完成', 6: '已取消' };
Page({
  data: { order: {} },
  onLoad(options) {
    api.getOrderDetail(options.id).then(data => {
      data.statusText = statusMap[data.status] || '未知';
      this.setData({ order: data });
    });
  },
  cancelOrder() {
    wx.showModal({ title: '确认取消', success: (res) => {
      if (res.confirm) api.cancelOrder(this.data.order.id).then(() => {
        wx.showToast({ title: '已取消' });
        setTimeout(() => wx.navigateBack(), 1000);
      });
    }});
  }
});
