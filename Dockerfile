FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace/app

# Needed for arm64 support. Open gradle issue: https://github.com/gradle/gradle/issues/18212
ENV JAVA_OPTS="-Djdk.lang.Process.launchMechanism=vfork"

COPY . /workspace/app
RUN apt-get -y update && apt-get -y install git
RUN ./gradlew clean build -x test

# Copy the non-plain jar into /build/dependency
RUN mkdir -p build/dependency && cd build/dependency && jar -xf $(ls ../libs/*.jar | grep -v "plain.jar")

FROM eclipse-temurin:17-jre
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-cp","app:app/lib/*","com.aerospike.restclient.AerospikeRestGatewayApplication"]
