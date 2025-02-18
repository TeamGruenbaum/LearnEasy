FROM node:23.7.0 AS build
WORKDIR /app
COPY package.json ./
RUN npm install
COPY ./src ./src
COPY ./tsconfig.json ./
RUN npx tsc

FROM node:23.7.0
WORKDIR /app
COPY --from=build /app/dist ./dist
COPY --from=build /app/package.json ./package.json
RUN npm install --omit=dev --omit=optional
ENTRYPOINT ["npx", "mocha", "--timeout", "10000", "/app/dist/IntegrationTests.js"]