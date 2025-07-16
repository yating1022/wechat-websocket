# 基于官方OpenJDK镜像
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制项目文件到容器中
COPY . /app

# 使用Maven Wrapper构建jar包（如果有mvnw），否则用系统maven
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# 发现target目录下的jar包名
RUN JAR_NAME=$(ls target/*.jar | grep -v 'original' | head -n 1) && cp $JAR_NAME app.jar

# 容器启动时运行jar包
ENTRYPOINT ["java", "-jar", "app.jar"] 