import { Routes } from '@angular/router';
import { roleGuard } from './core/guards/role.guard';
import { SimplePageComponent } from './shared/ui/simple-page.component';

export const appRoutes: Routes = [
  { path: '', component: SimplePageComponent, data: { title: 'BlinkIt Mega Dashboard' } },
  { path: 'customer/home', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'], title: 'Customer Home' } },
  { path: 'customer/products', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'], title: 'Products' } },
  { path: 'customer/cart', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'], title: 'Cart' } },
  { path: 'customer/checkout', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'], title: 'Checkout' } },
  { path: 'customer/orders', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'], title: 'Orders' } },
  { path: 'customer/wallet', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'], title: 'Wallet' } },
  { path: 'customer/referral', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['CUSTOMER'], title: 'Referral' } },
  { path: 'admin/products', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'], title: 'Admin Products' } },
  { path: 'admin/inventory', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'], title: 'Admin Inventory' } },
  { path: 'admin/orders', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'], title: 'Admin Orders' } },
  { path: 'admin/refunds', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'], title: 'Admin Refunds' } },
  { path: 'admin/audit', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['ADMIN'], title: 'Admin Audit' } },
  { path: 'warehouse/picking-queue', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['WAREHOUSE_STAFF'], title: 'Picking Queue' } },
  { path: 'warehouse/picklist', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['WAREHOUSE_STAFF'], title: 'Picklist' } },
  { path: 'rider/batches', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['RIDER'], title: 'Rider Batches' } },
  { path: 'rider/order-status', component: SimplePageComponent, canActivate: [roleGuard], data: { roles: ['RIDER'], title: 'Rider Order Status' } }
];
