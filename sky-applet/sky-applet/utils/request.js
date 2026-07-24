const app = getApp();
const baseUrl = app.globalData.baseUrl;

const request = (url, method, data) => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + url, method, data,
      header: { 'authorization': app.globalData.token || wx.getStorageSync('token') || '' },
      success(res) {
        if (res.data.code === 1) { resolve(res.data.data); }
        else { wx.showToast({ title: res.data.msg, icon: 'none' }); reject(res.data.msg); }
      },
      fail(err) { wx.showToast({ title: '网络错误', icon: 'none' }); reject(err); }
    });
  });
};
module.exports = {
  get: (url, data) => request(url, 'GET', data),
  post: (url, data) => request(url, 'POST', data),
  put: (url, data) => request(url, 'PUT', data),
  del: (url, data) => request(url, 'DELETE', data)
};
