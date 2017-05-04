import java.nio.charset.Charset

def USER_DIR = System.getProperty("user.dir")
def date = timestamp("yyyy-MM-dd")
def fileName = "log-${date}.log"


appender("FILE", FileAppender) {
    file = "${USER_DIR}\\logs\\${fileName}"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%date{HH:mm:ss.SSS} %-5level [%thread] %logger : %msg%n"
        charset = Charset.forName("UTF-8")
    }
}

root(DEBUG, ["FILE"])