// src/app/core/constants/api.routes.ts

import { environment } from "../environments/environment";


const API = environment.apiBaseUrl;

export const ApiRoutes = {
  auth: {
    login: `${API}/auth/login`,
    verifyOtp: `${API}/auth/verify-otp`,
    register: `${API}/auth/register`,
    password: {
      request: `${API}/auth/password/request`,
      validate: `${API}/auth/password/validate`,
      update: `${API}/auth/password/update`,
    },
  },
  notifications: {
    translated: `${API}/api/notifications/translated`,
    all: `${API}/api/notifications`,
    markAsRead: (id: number) => `${API}/api/notifications/${id}/read`
  },
  profile: {
    me: `${API}/api/user/profile/me`,
    update: `${API}/api/user/profile/update`,
  },
  acciones: {
    scrap: `${API}/acciones/scrap`
  },
  orders: {
    me: `${API}/api/orders/me`,
    user: `${API}/api/orders/user`,
    orders: `${API}/api/orders`,
    userCount: (username: string) => `${API}/api/orders/user/${username}/count`,
    market: (market: string) => `${API}/api/orders/market/${market}`,
    company: (company: string) => `${API}/api/orders/company/${company}`,
    execute: (id: number) => `${API}/api/orders/${id}/execute`,
    reject: (id: number) => `${API}/api/orders/${id}/rejectByLimit`,
    updateStatus: (id: number) => `${API}/api/orders/${id}/status`
  },
  transactions: {
    balance: `${API}/api/balance`,
    history: `${API}/api/history`,
    chart: `${API}/api/chart-data`,
    deposit: `${API}/api/balance/update`
  },
    stocks: {
    bySymbol: (symbol: string) => `${API}/api/stocks/twelve/${symbol}`
  }

};
