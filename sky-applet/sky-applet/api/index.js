const api = require('../utils/request');

module.exports = {
  login: (code) => api.post('/user/login', { code }),
  getCategories: (type) => api.get('/category/list?type=' + type),
  getDishes: (categoryId) => api.get('/dish/list?categoryId=' + categoryId),
  getDishById: (id) => api.get('/dish/' + id),
  getSetmeals: (categoryId) => api.get('/setmeal/list?categoryId=' + categoryId),
  getSetmealById: (id) => api.get('/setmeal/' + id),
  addCart: (data) => api.post('/shoppingCart/add', data),
  getCart: () => api.get('/shoppingCart/list'),
  cleanCart: () => api.del('/shoppingCart/clean'),
  subCart: (data) => api.post('/shoppingCart/sub', data),
  submitOrder: (data) => api.post('/order/submit', data),
  getOrders: (status) => api.get('/order/historyOrders?status=' + (status || '')),
  getOrderDetail: (id) => api.get('/order/orderDetail/' + id),
  cancelOrder: (id) => api.put('/order/cancel/' + id),
  getAddresses: () => api.get('/addressBook/list'),
  saveAddress: (data) => api.post('/addressBook', data),
  updateAddress: (data) => api.put('/addressBook', data),
  deleteAddress: (id) => api.del('/addressBook?id=' + id),
  setDefault: (data) => api.put('/addressBook/default', data)
};
