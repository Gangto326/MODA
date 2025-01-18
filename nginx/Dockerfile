FROM nginx:alpine

# 기본 nginx 설정 파일 제거
RUN rm /etc/nginx/conf.d/default.conf

# 현재 디렉토리의 nginx.conf를 복사
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]