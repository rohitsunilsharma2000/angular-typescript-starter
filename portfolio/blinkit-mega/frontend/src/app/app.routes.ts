import { Routes } from '@angular/router';
import { roleGuard } from './core/guards/role.guard';
import { HomePageComponent } from './features/customer/home/home.page';
import { ProductListPageComponent } from './features/customer/product-list/product-list.page';
import { ProductDetailPageComponent } from './features/customer/product-detail/product-detail.page';
import { CartPageComponent } from './features/customer/cart/cart.page';
import { CheckoutPageComponent } from './features/customer/checkout/checkout.page';
import { OrdersPageComponent } from './features/customer/orders/orders.page';
import { OrderDetailPageComponent } from './features/customer/order-detail/order-detail.page';
import { WalletPageComponent } from './features/customer/wallet/wallet.page';
import { ReferralPageComponent } from './features/customer/referral/referral.page';
import { AdminProductsPageComponent } from './features/admin/products/products.page';
import { AdminInventoryPageComponent } from './features/admin/inventory/inventory.page';
import { AdminOrdersPageComponent } from './features/admin/orders/orders.page';
import { AdminRefundsPageComponent } from './features/admin/refunds/refunds.page';
import { AdminAuditPageComponent } from './features/admin/audit/audit.page';
import { WarehousePickingQueuePageComponent } from './features/warehouse/picking-queue/picking-queue.page';
import { WarehousePicklistPageComponent } from './features/warehouse/picklist/picklist.page';
import { RiderBatchesPageComponent } from './features/rider/batches/batches.page';
import { RiderOrderStatusPageComponent } from './features/rider/order-status/order-status.page';

export const appRoutes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'customer/home', component: HomePageComponent },
  { path: 'customer/products', component: ProductListPageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'] } },
  { path: 'customer/products/:id', component: ProductDetailPageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'] } },
  { path: 'customer/cart', component: CartPageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'] } },
  { path: 'customer/checkout', component: CheckoutPageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'] } },
  { path: 'customer/orders', component: OrdersPageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'] } },
  { path: 'customer/orders/:id', component: OrderDetailPageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'] } },
  { path: 'customer/wallet', component: WalletPageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'] } },
  { path: 'customer/referral', component: ReferralPageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'] } },

  { path: 'admin/products', component: AdminProductsPageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'] } },
  { path: 'admin/inventory', component: AdminInventoryPageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'] } },
  { path: 'admin/orders', component: AdminOrdersPageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'] } },
  { path: 'admin/refunds', component: AdminRefundsPageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'] } },
  { path: 'admin/audit', component: AdminAuditPageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'] } },
  { path: 'warehouse/picking-queue', component: WarehousePickingQueuePageComponent, canActivate: [roleGuard], data: { roles: ['WAREHOUSE_STAFF'] } },
  { path: 'warehouse/picklist', component: WarehousePicklistPageComponent, canActivate: [roleGuard], data: { roles: ['WAREHOUSE_STAFF'] } },
  { path: 'rider/batches', component: RiderBatchesPageComponent, canActivate: [roleGuard], data: { roles: ['RIDER'] } },
  { path: 'rider/order-status', component: RiderOrderStatusPageComponent, canActivate: [roleGuard], data: { roles: ['RIDER'] } },
  { path: '**', redirectTo: 'customer/home' }
];
