# 第一阶段：编译打包（使用Maven镜像）
FROM maven:3.8.5-openjdk-8 AS builder

# 设置工作目录
WORKDIR /app

# 复制pom.xml和源代码
COPY pom.xml .
COPY src ./src

# 编译打包（跳过测试）
RUN mvn clean package -DskipTests

# 第二阶段：运行（使用精简的JRE镜像）
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 从构建阶段复制jar包到当前镜像
COPY --from=builder /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8081

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
