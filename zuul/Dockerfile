FROM java
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY build/libs/zuul-1.0.1.jar zuul.jar
RUN bash -c 'touch ./zuul.jar'
CMD ["java","-jar","zuul.jar"]