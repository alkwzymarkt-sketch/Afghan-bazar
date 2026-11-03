FROM node:18-alpine AS builder

WORKDIR /app
COPY package*.json ./
COPY schema.prisma ./
RUN npm install
COPY . .
RUN npx prisma generate
RUN npm run build || true

FROM node:18-alpine AS runner
WORKDIR /app

ENV NODE_ENV=production
COPY --from=builder /app ./

EXPOSE 5000
CMD ["sh", "-c", "npx prisma db push --accept-data-loss=false && node dist/server.js || ts-node server.ts"]
