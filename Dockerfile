# 第一阶段：编译打包（优化依赖缓存 + 清理冗余）
FROM maven:3.8.5-openjdk-8 AS builder
WORKDIR /app

# 1. 缓存 Maven 依赖（仅 pom 变更时重新下载）
COPY pom.xml .
RUN mvn dependency:go-offline -Dmaven.test.skip=true

# 2. 编译打包 + 清理缓存
COPY src ./src
RUN mvn clean package -DskipTests && \
    rm -rf ~/.m2/repository  # 清理构建残留

# 第二阶段：运行（非 root + 规范配置目录）
FROM openjdk:8-jre-alpine
WORKDIR /app

# 1. 创建非 root 用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 2. 配置目录（权限赋予非 root 用户）
RUN mkdir -p /app/config && \
    chown -R appuser:appgroup /app /app/config

# 3. 复制 jar 包（从构建阶段）
COPY --from=builder /app/target/*.jar app.jar

# 4. 切换非 root 用户
USER appuser

# 5. 暴露端口
EXPOSE 8081

# 6. 启动命令（支持配置目录 + 多环境）
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=classpath:/,file:/app/config/"]