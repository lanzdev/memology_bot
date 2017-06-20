import ch.qos.logback.access.PatternLayoutEncoder
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.rolling.helper.FileNamePattern

import java.nio.charset.Charset

def USER_DIR = System.getProperty("user.dir")
def date = timestamp("yyyy-MM-dd")
def dir = "${USER_DIR}${File.separator}logs${File.separator}"
def fileName = "log-${date}.log"


appender("TIME_BASED", RollingFileAppender) {
//    def filePath = "${USER_DIR}${File.separator}logs${File.separator}${fileName}"
//    file = filePath
//    println "File with logs: ${filePath}"
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