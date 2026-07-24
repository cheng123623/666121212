Page({
  data: { userId: '' },
  onShow() { const token = wx.getStorageSync('token'); this.setData({ userId: token ? '已登录' : '未登录' }); },
  logout() { wx.removeStorageSync('token'); getApp().globalData.token = ''; wx.reLaunch({ url: '/pages/login/login' }); }
});
