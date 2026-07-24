const api = require('../../api/index');
Page({
  data: { loading: false },
  handleLogin() {
    this.setData({ loading: true });
    wx.login({
      success: (res) => {
        api.login(res.code).then(data => {
          getApp().globalData.token = data.token;
          wx.setStorageSync('token', data.token);
          wx.switchTab({ url: '/pages/index/index' });
        }).finally(() => this.setData({ loading: false }));
      }
    });
  }
});
