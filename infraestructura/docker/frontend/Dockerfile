# ==========================================
# ETAPA 1: BUILD
# ==========================================
FROM node:14-alpine AS build

WORKDIR /app

COPY frontend/package*.json ./

RUN npm ci

COPY frontend/ ./

RUN npm run build -- --prod


# ==========================================
# ETAPA 2: RUNTIME
# ==========================================
FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

COPY infraestructura/docker/frontend/nginx.conf /etc/nginx/nginx.conf

COPY --from=build /app/dist/billingApp /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
