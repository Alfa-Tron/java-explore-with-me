FROM postgres:13.7-alpine
COPY /statistics/server/src/main/resources/schema.sql /docker-entrypoint-initdb.d
EXPOSE 5432