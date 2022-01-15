FROM node:14.17.4-alpine AS build-step
RUN mkdir -p /app
WORKDIR /app

COPY package.json /app/

RUN npm install

COPY . /app/

RUN npm build --prod

#FROM nginx:1.17.1-alpine
#COPY --from=build-step /app/dist/shortify /usr/share/nginx/html
#COPY nginx.conf /etc/nginx/nginx.conf

