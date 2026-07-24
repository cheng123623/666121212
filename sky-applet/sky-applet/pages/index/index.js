const api = require('../../api/index');
Page({
  data: { categories: [], dishes: [], currentId: 0, cartCount: 0 },
  onLoad() { this.loadCategories(); },
  onShow() { this.loadCartCount(); },
  loadCategories() {
    api.getCategories(1).then(data => {
      this.setData({ categories: data });
      if (data.length > 0) { this.setData({ currentId: data[0].id }); this.loadDishes(); }
    });
  },
  loadDishes() { api.getDishes(this.data.currentId).then(data => this.setData({ dishes: data })); },
  switchTab(e) { this.setData({ currentId: e.currentTarget.dataset.id }); this.loadDishes(); },
  goDetail(e) { wx.navigateTo({ url: '/pages/dish/dish?id=' + e.currentTarget.dataset.id }); },
  goCart() { wx.navigateTo({ url: '/pages/cart/cart' }); },
  loadCartCount() { api.getCart().then(data => this.setData({ cartCount: data ? data.length : 0 })); }
});
