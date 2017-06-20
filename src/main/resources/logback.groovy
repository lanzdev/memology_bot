import java.nio.charset.Charset

def USER_DIR = System.getProperty("user.dir")
def date = timestamp("yyyy-MM-dd")
def fileName = "log-${date}.log"


appender("FILE", FileAppender) {
    def filePath = "${USER_DIR}${File.separator}logs${File.separator}${fileName}"
    file = filePath
    println "File with logs: ${filePath}"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%date{HH:mm:ss.SSS} %-5level [%thread] %logger : %msg%n"
        charset = Charset.forName("UTF-8")
    }
}

root(DEBUG, ["FILE"])