ARG JRE_TAG
FROM openjdk:${JRE_TAG}

ENV APP_MAIN_CLASS=com.github.fufler.mytheresa.ApplicationKt

RUN mkdir -p /opt/app/bin /opt/app/lib /opt/app/conf

VOLUME /opt/app/conf

COPY run.sh /opt/app/bin/run.sh
ADD dependencies/3rd-party/*.jar /opt/app/lib/
ADD dependencies/app/*.jar /opt/app/lib/

ENTRYPOINT ["/bin/sh", "/opt/app/bin/run.sh"]
