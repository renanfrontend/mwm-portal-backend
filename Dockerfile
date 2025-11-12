FROM docker.io/library/python:3.13-slim

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
ENV PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"

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
    echo "export PATH='/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin'" >> /etc/environment && \
    echo '. /etc/environment' >> /etc/profile

RUN cd /tmp && mkdir -p /etc/apt /var/cache/apt/archives/ /var/cache/apt/archives/partial /var/cache/apt /usr/local/lib/x86_64-linux-gnu /var/cache/app-info \
    && apt-get update && apt-get upgrade --yes && apt-get install --no-install-recommends --no-install-suggests --yes apt-transport-https apt-utils ca-certificates \
        git lsb-release wget curl gnupg gpg gpg-agent locales openssl bzip2 tzdata vim nano sudo xz-utils zip unzip maven \
    && echo 'en_US.UTF-8 UTF-8' > /etc/locale.gen && locale-gen 'en_US.UTF-8' && update-locale && dpkg-reconfigure locales && ln -sf /usr/share/zoneinfo/Etc/UTC /etc/localtime \
    && rm -fr /var/cache/apt/archives/*.deb /var/cache/apt/archives/partial/*.deb /var/cache/apt/*.bin || true \
    && rm -fr /var/lib/apt/lists/* /var/cache/apt/archives/* /usr/local/src/* /tmp/* || true \
    && rm -fr /etc/ld.so.cache || true && ldconfig -v || true

ADD https://github.com/krallin/tini/releases/download/v0.19.0/tini /tini
RUN chmod +x /tini && mkdir -p /app

ADD ./target/bioplanta-0.0.1-SNAPSHOT.jar /app/bioplanta-0.0.1-SNAPSHOT.jar
WORKDIR /app

ENTRYPOINT ["/tini", "--"]
CMD ["java", "-jar", "/app/bioplanta-0.0.1-SNAPSHOT.jar"]
 