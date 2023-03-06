# syntax=docker/dockerfile:1.4
FROM alpine:3.7 AS builder
RUN apk add --no-cache \
        uwsgi-python3 \
        python3
EXPOSE 80

WORKDIR /app

COPY requirements.txt /app
RUN --mount=type=cache,target=/root/.cache/pip \
    pip3 install -r requirements.txt

RUN addgroup -S python && adduser -S 999 -G python
USER 999

COPY . /app
CMD [ "uwsgi", "--ini", "/app/uwsgi.ini" ]