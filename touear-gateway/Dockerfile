FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER sondery@163.com.com

RUN mkdir -p /touear/gateway

WORKDIR /touear/gateway

EXPOSE 80

ADD ./target/touear-gateway.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
