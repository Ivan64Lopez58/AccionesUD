# Etapa 1: Construcción de Angular
FROM node:22-slim AS builder
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npx ng build --configuration production --project acciones-ud

# Etapa 2: Servir con NGINX
FROM nginx:alpine
COPY --from=builder /app/dist/acciones-ud/browser /usr/share/nginx/html

# 👇 Añade esta línea para configurar NGINX correctamente
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

