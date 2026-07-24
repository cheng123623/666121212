const api = require('../../api/index');
Page({
  data: { form: { sex: '1', isDefault: 0 }, labels: ['', '家', '公司', '学校'] },
  onLoad(options) {
    if (options.id) {
      api.getAddresses().then(addrs => {
        const addr = addrs.find(a => a.id == options.id);
        if (addr) this.setData({ form: addr });
      });
    }
  },
  onInput(e) { const f = e.currentTarget.dataset.field; this.setData({ ['form.' + f]: e.detail.value }); },
  onRadio(e) { this.setData({ 'form.sex': e.detail.value }); },
  onLabelChange(e) { this.setData({ 'form.label': this.data.labels[e.detail.value] }); },
  onDefaultChange(e) { this.setData({ 'form.isDefault': e.detail.value ? 1 : 0 }); },
  save() {
    const api_cmd = this.data.form.id ? api.updateAddress : api.saveAddress;
    api_cmd(this.data.form).then(() => { wx.showToast({ title: '保存成功' }); wx.navigateBack(); });
  },
  deleteAddr() {
    wx.showModal({ title: '确认删除', success: (res) => {
      if (res.confirm) api.deleteAddress(this.data.form.id).then(() => { wx.showToast({ title: '已删除' }); wx.navigateBack(); });
    }});
  }
});
