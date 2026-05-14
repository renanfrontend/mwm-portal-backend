FROM maven:4.0.0-rc-5-eclipse-temurin-17-noble AS builder

WORKDIR /app

COPY . /app

RUN mvn install -DskipTests


#----

FROM eclipse-temurin:17.0.17_10-jre-noble

USER root

SHELL ["/bin/bash", "-o", "pipefail", "-o", "errexit", "-o", "nounset", "-o", "nolog", "-c"]

ENV TERM='xterm'
ENV TZ='Etc/UTC'
ENV LANG='en_US.UTF-8'
ENV LANGUAGE='en_US:en'
ENV LC_CTYPE='C.UTF-8'
ENV LC_MESSAGES='C.UTF-8'
ENV LC_CTYPE='C.UTF-8'
ENV LC_COLLATE='C.UTF-8'
ENV LC_TIME='C.UTF-8'
ENV LC_MESSAGES='C.UTF-8'
ENV LC_MONETARY='C.UTF-8'
ENV LC_PAPER='C.UTF-8'
ENV LC_MEASUREMENT='C.UTF-8'
ENV LC_ALL='C.UTF-8'
ENV ACCEPT_EULA='y'
ENV DEBIAN_FRONTEND='noninteractive'
ENV PYTHONWARNINGS='ignore::FutureWarning'

RUN echo "export TERM='xterm'" > /etc/environment && \
    echo "export TZ='Etc/UTC'" >> /etc/environment && \
    echo "export LANG='en_US.UTF-8'" >> /etc/environment && \
    echo "export LANGUAGE='en_US:en'" >> /etc/environment && \
    echo "export LC_CTYPE='C.UTF-8'" >> /etc/environment && \
    echo "export LC_MESSAGES='C.UTF-8'" >> /etc/environment && \
    echo "export LC_CTYPE='C.UTF-8'" >> /etc/environment && \
    echo "export LC_COLLATE='C.UTF-8'" >> /etc/environment && \
    echo "export LC_TIME='C.UTF-8'" >> /etc/environment && \
    echo "export LC_MESSAGES='C.UTF-8'" >> /etc/environment && \
    echo "export LC_MONETARY='C.UTF-8'" >> /etc/environment && \
    echo "export LC_PAPER='C.UTF-8'" >> /etc/environment && \
    echo "export LC_MEASUREMENT='C.UTF-8'" >> /etc/environment && \
    echo "export LC_ALL='C.UTF-8'" >> /etc/environment && \
    echo "export ACCEPT_EULA='y'" >> /etc/environment && \
    echo "export DEBIAN_FRONTEND='noninteractive'" >> /etc/environment && \
    echo "export PYTHONWARNINGS='ignore::FutureWarning'" >> /etc/environment && \
    echo '. /etc/environment' >> /etc/profile

RUN apt-get update && apt-get install --no-install-recommends --no-install-suggests --yes \
        ca-certificates tzdata locales \
    && echo 'en_US.UTF-8 UTF-8' > /etc/locale.gen && locale-gen 'en_US.UTF-8' && update-locale \
    && ln -sf /usr/share/zoneinfo/Etc/UTC /etc/localtime \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app/dist

COPY --from=builder /app/target/*.jar /app/dist/

EXPOSE 8080


CMD ["java", "-jar", "/app/dist/bioplanta-0.0.1-SNAPSHOT.jar"]