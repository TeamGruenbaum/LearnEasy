FROM node:23.7.0 AS build
WORKDIR /app
COPY package.json ./
RUN npm install
COPY ./ ./
RUN npm run build

FROM node:23.7.0
WORKDIR /app
COPY --from=build /app/dist ./dist
RUN npm install -g serve
EXPOSE 5173
ENTRYPOINT ["serve", "--single", "--no-request-logging", "--no-clipboard", "--cors", "dist", "-l", "5173"]