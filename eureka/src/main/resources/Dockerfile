FROM java
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY build/libs/eureka-1.0.1.jar eureka.jar
RUN bash -c 'touch ./eureka.jar'
CMD ["java","-jar","eureka.jar"]