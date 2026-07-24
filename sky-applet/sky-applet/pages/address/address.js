const api = require('../../api/index');
Page({
  data: { addresses: [] },
  onShow() { this.loadAddresses(); },
  loadAddresses() { api.getAddresses().then(data => this.setData({ addresses: data || [] })); },
  edit(e) { wx.navigateTo({ url: '/pages/address-add/address-add?id=' + e.currentTarget.dataset.id }); },
  del(e) { wx.showModal({ title: '确认删除', success: (res) => { if (res.confirm) { api.deleteAddress(e.currentTarget.dataset.id).then(() => this.loadAddresses()); } } }); }
});
