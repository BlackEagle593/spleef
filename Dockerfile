# PaperSpigot
FROM adoptopenjdk/openjdk14:alpine-jre as paper

ARG MC_VERSION=Paper-1.16

WORKDIR /root

RUN apk update && \
    apk add ca-certificates && \
    update-ca-certificates

# Download and run MC_VERSION PaperClip, afterwards removing work directories
RUN wget -O PaperClip.jar https://papermc.io/ci/job/${MC_VERSION}/lastSuccessfulBuild/artifact/paperclip.jar && \
    java -jar PaperClip.jar && \
    rm -rf logs eula.txt server.properties


# Builder
FROM gradle:jdk11 as builder

# Copy source files
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

# Build with gradle
RUN gradle shadow


# Server
FROM adoptopenjdk/openjdk14:alpine-jre

ENV JVM_MEMORY=512M
ENV JVM_ARGUMENTS="-XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:-UseParallelGC -XX:-UseG1GC -XX:+UseZGC -XX:MaxGCPauseMillis=100 -XX:TargetSurvivorRatio=90 -XX:+AlwaysPreTouch -XX:+ParallelRefProcEnabled"
ENV MC_ARGUMENTS="-Dcom.mojang.eula.agree=true"

WORKDIR /root

EXPOSE 25565

VOLUME /root/world

HEALTHCHECK --start-period=15s --interval=15s --timeout=5s \
    CMD echo -e '\x0f\x00\x00\x09\x31\x32\x37\x2e\x30\x2e\x30\x2e\x31\x63\xdd\x01\x01\x00' \
    | nc 127.0.0.1 25565 -w 1 | grep version

# Copy server.jar
COPY --from=paper /root/cache/patched_*.jar server.jar

# Copy plugins
COPY --from=builder /home/gradle/src/build/libs/*-all.jar plugins/

ENTRYPOINT /opt/java/openjdk/bin/java \
    -Xms${JVM_MEMORY} ${JVM_ARGUMENTS} ${MC_ARGUMENTS} \
    -cp "server.jar" "org.bukkit.craftbukkit.Main"
