import ch.qos.logback.access.PatternLayoutEncoder

import java.nio.charset.Charset

def USER_DIR = System.getProperty("user.dir")
def dir = "${USER_DIR}${File.separator}logs${File.separator}"


appender("TIME_BASED", RollingFileAppender) {
    append = true
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${dir}/log-%d{yyyy-MM-dd}.log"
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%date{HH:mm:ss.SSS} %-5level [%thread] %logger : %msg%n"
        charset = Charset.forName("UTF-8")
    }
}

root(DEBUG, ["TIME_BASED"])