FROM postgres:latest
ENV POSTGRES_USER shopping-cart
ENV POSTGRES_PASSWORD shopping-cart
ADD create_tables.sql /docker-entrypoint-initdb.d/
ADD create_user_tables.sql /docker-entrypoint-initdb.d/