FROM openjdk:17-alpine

WORKDIR /app

ENV TZ Asia/Seoul



ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/mincourse.jar ${JAR_PATH}/mincourse.jar

# 포트 노출
#EXPOSE 8080

# 애플리케이션 실행
#CMD ["java", "-jar","-Dspring.profiles.active=prod", "./build/libs/mincourse.jar"]
#CMD ["java", "-javaagent:./data/pinpoint-agent-2.5.3/pinpoint-bootstrap-2.5.3.jar", "-Dpinpoint.agentId=mincourse", "-Dpinpoint.applicationName=mincourse", "-Dpinpoint.config=./data/pinpoint-agent-2.5.3/pinpoint-root.config", "-jar", "-Dspring.profiles.active=prod", "./build/libs/mincourse.jar"]
CMD ["java", "-javaagent:./data/pinpoint-agent-3.0.0/pinpoint-bootstrap-3.0.0.jar", "-Dpinpoint.agentId=mincourse", "-Dpinpoint.applicationName=mincourse", "-Dpinpoint.config=./data/pinpoint-agent-3.0.0/pinpoint-root.config", "-jar", "-Dspring.profiles.active=prod", "./build/libs/mincourse.jar"]
