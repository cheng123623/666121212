const api = require('../../api/index');
Page({
  data: { dish: {}, quantity: 1, selectedFlavors: [] },
  onLoad(options) {
    api.getDishById(options.id).then(data => {
      this.setData({ dish: data });
      if (data.flavors) this.setData({ selectedFlavors: data.flavors.map(() => '') });
    });
  },
  onFlavorChange(e) { const idx = e.currentTarget.dataset.index; const val = e.detail.value; const arr = [...this.data.selectedFlavors]; arr[idx] = val; this.setData({ selectedFlavors: arr }); },
  minus() { if (this.data.quantity > 1) this.setData({ quantity: this.data.quantity - 1 }); },
  plus() { this.setData({ quantity: this.data.quantity + 1 }); },
  addCart() {
    const flavorStr = this.data.selectedFlavors.filter(f => f).join(',');
    const data = { dishId: this.data.dish.id, dishFlavor: flavorStr || undefined };
    api.addCart(data).then(() => { wx.showToast({ title: '已加入购物车' }); wx.navigateBack(); });
  }
});
