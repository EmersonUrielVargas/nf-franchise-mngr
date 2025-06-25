FROM amazoncorretto:21.0.7-alpine3.21
VOLUME /tmp
COPY *.jar franchises.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

ENV DB_HOST=${DB_HOST}
ENV DB_PORT=${DB_PORT}
ENV DB_DATABASE=${DB_DATABASE}
ENV DB_SCHEMA=${DB_SCHEMA}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_USER_PASSWORD=${DB_USER_PASSWORD}
ENV ALLOWED_ORIGINS=${ALLOWED_ORIGINS}

USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar franchises.jar" ]
