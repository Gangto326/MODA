FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build && ls -l /app/dist
RUN echo "Build completed. Contents of /app:" && ls -l /app

# Production stage
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
RUN ls -l /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]