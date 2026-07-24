const api = require('../../api/index');
Page({
  data: { cart: [], total: 0 },
  onShow() { this.loadCart(); },
  loadCart() { api.getCart().then(data => { const cart = data || []; const total = cart.reduce((s, i) => s + parseFloat(i.amount), 0).toFixed(2); this.setData({ cart, total }); }); },
  sub(e) { const item = this.data.cart[e.currentTarget.dataset.index]; api.subCart({ dishId: item.dishId, setmealId: item.setmealId }).then(() => this.loadCart()); },
  add(e) { const item = this.data.cart[e.currentTarget.dataset.index]; api.addCart({ dishId: item.dishId, setmealId: item.setmealId }).then(() => this.loadCart()); },
  checkout() {
    api.getAddresses().then(addrs => {
      const defaultAddr = addrs.find(a => a.isDefault === 1) || addrs[0];
      if (!defaultAddr) { wx.showToast({ title: '请先添加地址', icon: 'none' }); return; }
      api.submitOrder({
        addressBookId: defaultAddr.id, payMethod: 1, amount: parseFloat(this.data.total),
        estimatedDeliveryTime: null, deliveryStatus: 1, tablewareNumber: 1, tablewareStatus: 1, packAmount: 0
      }).then(() => { wx.showToast({ title: '下单成功' }); wx.switchTab({ url: '/pages/order/order' }); });
    });
  }
});
