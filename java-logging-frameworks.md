## Java日志系统

### 困惑
Java与日志相关包和库有 java.util.logging log4j1、log4j2、logback、commons-logging、slf4j、log4j-api、log4j-core、log4j-1.2-api、log4j-jcl、log4j-slf4j-impl、log4j-jul、logback-core、logback-classic、logback-access、slf4j-api、slf4j-log4j12、slf4j-simple、jcl-over-slf4j、slf4j-jdk14、log4j-over-slf4j、slf4j-jcl、log4j-to-slf4j

1. 这些包都是干啥的？
2. 记录日志需要使用那些包？
3. 有些包应该是属于同一个系统，为什么要拆分那么包，如logback-core、logback-classic、logback-access？
4. log4j-over-slf4j，slf4j-jcl这些包名看起来跟两个系统有关系，那么关系是什么？为什么会有这些关系？
5. log4j1和log4j2这种像同一套系统，为什么需要区分？

### 日志系统
现有日志系统实现方案有以下几种：

1. Java原生日志系统：java.util.logging
2. Apache开源日志系统：log4j(log4j1, log4j2)
3. log4j创始人设计的有一个开源日志系统：Logback

#### 原生日志 java.util.logging

logging基础:

* Logger 对外发布的日志记录器，应用系统可以通过该对象完成日志记录的功能
* Level 日志的记录级别
* LogRecord 日志信息描述对象
* Filter 日志过滤器，接口对象，在日志被 Handler 处理之前，起过滤作用
* Handler 日志处理器，接口对象，决定日志的输出方式
* Formatter 日志格式化转换器，接口对象，决定日志的输出格式
![logging 原理](/Users/xiahaihu/Documents/private/logging 原理.png)

示例: 

```
import java.io.IOException;
import java.util.logging.*;

public class LoggingTest {

    private static final Logger logger = Logger.getLogger(LoggingTest.class.getName());

    public static void main(String[] args) {

        // 简单输出一个info日志
        logger.info("a info");      // console输出: a info

        // 设置日志级别为warning，输出日志
        logger.setLevel(Level.WARNING);
        logger.info("a info");      // 该日志不输出
        logger.warning("a warn");   // console输出: a warn

        // 自定义一个Filter
        logger.setFilter(new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {           // 只输出SEVERE级别的日志
                return Level.SEVERE.equals(record.getLevel());
            }
        });

        // 自定义一个Handler来捕获日志（用日志文件来记录日志），定义日志级别，自定义输出格式
        try {
            Handler handler = new FileHandler("%h/logs/learn-logger/java%g.log");
            handler.setLevel(Level.SEVERE);
            handler.setEncoding("utf8");
            handler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    // 自定义日志输出格式
                    return record.getLoggerName() + ": " + record.getMessage() + " [" + record.getLevel() + "] ";
                }
            });

            logger.addHandler(handler);

        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("a info");          // 因为日志级别为severe，所以console不输出该日志
        logger.warning("a warn");       // 因为日志级别为severe，所以console不输出该日志
        logger.severe("a severe");      // console输出:a severe

        /*
            cat: /Users/xiahaihu/logs/learn-logger/java0.log            自定义记录日志文件
            com.shearf.learn.logger.LoggingTest: a severe [SEVERE]      自定义日志格式
         */
    }
}	
```
> https://segmentfault.com/a/1190000004227150

#### log4j1

log4j是一个用Java编写的可靠，快速和灵活的日志框架（API），它在Apache软件许可下发布。

Log4j是高度可配置的，并可通过在运行时的外部文件配置。它根据记录的优先级别，并提供机制，以指示记录信息到许多的目的地，诸如：数据库，文件，控制台，UNIX系统日志等。

log4j有如下特点：

* log4j的是线程安全的
* log4j是经过优化速度的
* log4j是基于一个名为记录器的层次结构
* log4j的支持每个记录器多输出追加器（appender）
* log4j支持国际化。
* log4j并不限于一组预定义的设备
* 日志行为可以使用配置文件在运行时设置
* log4j设计从一开始就是处理Java异常
* log4j使用多个层次，即ALL，TRACE，DEBUG，INFO，WARN，ERROR和FATAL
* 日志输出的格式可以通过扩展Layout类容易地改变
* 日志输出的目标，以及在写入策略可通过实现Appender程序接口改变，log4j会故障停止。然而，尽管它肯定努力确保传递，log4j不保证每个日志语句将被传递到目的地。

*2015年8月停止更新*


maven依赖：

```
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

示例：

```
import org.apache.log4j.Logger;

public class Log4jTest {

    private static final Logger logger = Logger.getLogger(Log4jTest.class);

    public static void main(String[] args) {
        logger.debug("Log4jTest debug: a debug message");
        logger.info("Log4jTest info: a info message");
        logger.warn("Log4jTest warn: a warn message");
        logger.error("Log4jTest error: a error message");
    }
}
```
配置：classpath: log4j.properties

```
log4j.rootLogger = warn, console
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss} [%t %p] %m%n

```
#### log4j2
log4j2是log4j1.*的升级，同时不兼容；对比log4j在代码结构上对实现和接口进行了分离，性能上更优，支持更多的API等等特性。

详见 [https://logging.apache.org/log4j/2.x/](https://logging.apache.org/log4j/2.x/)

maven依赖：

```
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.2</version>
</dependency>
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.2</version>
  </dependency>
```
示例：

```
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Test {

    private static final Logger logger = LogManager.getLogger(Log4j2Test.class);

    public static void main(String[] args) {
        logger.info("a info");
        logger.warn("a warn");
    }
}
```
配置：classpath:log4j2.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
        <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n"/> </Console>
    </Appenders>
    <Loggers>
        <Root level="debug">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```
**升级log4j到log4j2**

项目使用log4j2作为日志系统，但是依赖的库文件还存在很多使用log4j的，不能对log4j的包进行修改替换成log4j2。使用log4j-1.2-api替换掉原来log4j，日志系统就切换到log4j2

```
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-1.2-api</artifactId>
</dependency>
<!-- 删除log4j -->
<!-- <dependency> -->
<!--    <groupId>log4j</groupId> -->
<!--    <artifactId>log4j</artifactId> -->
<!-- </dependency> -->
```
**使用log4j2代替jul**
项目使用log4j2作为日志系统，但是依赖库文件使用java.util.logging(jul)，用log4j2替代原来的jui。需要进行如下操作：

1. maven新增依赖

	```
	<dependency>
	    <groupId>org.apache.logging.log4j</groupId>
	    <artifactId>log4j-jul</artifactId>
	</dependency>
	```
2. 	运行程序使用修改一下JVM参数: -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager


#### Logback

Logback是由log4j创始人设计的又一个开源日志组件，跟log4j非常相似，但是比log4j更优秀。[Reasons to prefer logback over log4j](https://logback.qos.ch/reasonsToSwitch.html)。


maven依赖：

```
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
    <version>1.1.11</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.1.11</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.24</version>
</dependency>
```

示例：

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {

    private static final Logger logger = LoggerFactory.getLogger(LogbackTest.class);

    public static void main(String[] args) {

        logger.info("a info");
    }
}
```
配置：

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="DEBUG">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```
**使用logback代替jul**

logback使用slf4j-api接口来实现日志，jul需要先转化为slf4j，再通过logback来实现

1. maven新增依赖

	```
	<dependency>
	    <groupId>org.slf4j</groupId>
	    <artifactId>jul-to-slf4j</artifactId>
	</dependency>
	```
2. 在代码中执行: 

	```
	static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
	```

#### Apache commons-logging - jcl

前面介绍了log4j2，logback等日志系统，对于开发者而言，每种日志都有不同的写法。如果我们以实际的日志框架来进行编写，代码就限制死了，之后就很难再更换日志系统，很难做到无缝切换。

java原则：面向接口编程，而不是面向实现编程。所以我们应该是按照一套统一的API来进行日志编程，实际的日志框架来实现这套API，这样的话，即使更换日志框架，也可以做到无缝切换。

apache commons-logging 是一套统一的API，基于API进行日志编程，让具体的日志系统来实现这套API的功能。

示例：

```
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonLoggingTest {

    private static final Log log = LogFactory.getLog(CommonLoggingTest.class);

    public static void main(String[] args) {

        log.error("common logging make a error log");
    }
}
```
commons-logging with jul(java.util.logging)

```
<dependency>
    <groupId>commons-logging</groupId>
    <artifactId>commons-logging</artifactId>
    <version>1.2</version>
</dependency>
<!-- 不依赖其他包，因为java.util.logging sdk自带 -->
```


commons-logging with log4j

```
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
<dependency>
    <groupId>commons-logging</groupId>
    <artifactId>commons-logging</artifactId>
    <version>1.2</version>
</dependency>
```

commons-logging with log4j2

```
<dependency>
    <groupId>commons-logging</groupId>
    <artifactId>commons-logging</artifactId>
    <version>1.2</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-jcl</artifactId>
    <version>2.8.2</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.7</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.7</version>
</dependency>
```

commons-logging with logback

```
 <dependencies>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jcl-over-slf4j</artifactId>
    </dependency>
</dependencies>
```

使用common-logging作为日志的通用实现，lib中加载相应的日志系统依赖就可以了，common-logging会自动去库中寻找日志依赖包

#### slf4j（Simple Logging Facade for Java）
java简单日志门面。 是对不同日志框架提供的一个门面封装。可以在部署的时候不修改任何配置即可接入一种日志实现方案

与commons-logging的主要差异：commons-logging通过运行时动态查找决定采用哪种日志框架，slf4j是编译时绑定到具体的日志框架，性能上由于commons-logging

##### 1. slf4j-api使用示例
maven

```
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
    </dependency>
</dependencies>
```
示例
```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jTest.class);

    public static void main(String[] args) {

        LOGGER.info("SLF4J make a info log");
    }
}
```

##### 2. slf4j用jul实现
maven

```
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-jdk14</artifactId>
    </dependency>
</dependencies>
```

##### 3. slf4j用log4j实现
maven

```
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
    </dependency>
</dependencies>
```
##### 4. slf4j用log4j 2 实现
maven

```
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-slf4j-impl</artifactId>
    </dependency>
</dependencies>
```
##### 5. slf4j用logback实现（logback使用slf4j原生接口）
maven

```	
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
    </dependency>
</dependencies>

```

##### 6. commons-logging, log4j, log4j 2, jul 切换成slf4j接口，并用logback实现，不修改原有日志接口
maven

```
<dependencies>
	<dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>log4j-over-slf4j</artifactId>    <!-- log4j to use slf4j instead -->
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jcl-over-slf4j</artifactId>      <!-- commons-logging to use slf4j instead -->
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-to-slf4j</artifactId>      <!-- log4j 2 to use slf4j instead -->
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jul-to-slf4j</artifactId>        <!-- jul to use slf4j instead -->
    </dependency>
    <!-- not required, better to remove them. start -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-1.2-api</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
    </dependency>
    <!-- not required, better to remove them. end -->
</dependencies>
```

> 备注：由于jul的日志level与slf4j接口定义的level不一致，用slf4j接口来接管jul需要执行：SLF4JBridgeHandler.install()

### 总结解惑

#### 1. 这些包都是干啥的
* java.util.logging java原生日志系统包
* log4j log4j版本1的日志系统
* log4j2 log4j版本2的日志系统
* logback logback日志系统 
* commons-logging 通用日志接口 简称jcl
* slf4j 通用日志接口
* log4j-api log4j版本2（log4j 2）的日志接口
* log4j-core log4j 2的日志接口实现
* log4j-1.2-api 用log4j 2来接管log4版本1的jar包，不修改log4j第一版的接口
* log4j-jcl jcl接口用log4j 2实现的jar包(Bridge)
* log4j-slf4j-impl slf4j接口用log4j 2实现的jar包(Bridge)
* log4j-jul jul接口用log4j 2来实现(log4j-jul)
* logback-core logback底层依赖为 logback-classic和logback-access服务
* logback-classic 对slf4j接口的实现
* logback-access 与Servlet容器集成提供通过Http访问日志的功能
* slf4j-api slf4j日志接口
* slf4j-log4j12 slf4j接口用log4j(log4j 1)实现
* log4j-to-slf4j slf4j接口用log4j 2实现
* slf4j-simple slf4j接口的简单实现
* jcl-over-slf4j jcl接口转交给slf4j接口
* slf4j-jdk14 slf4j接口使用jul实现
* log4j-over-slf4j log4j转交给slf4j接口
* slf4j-jcl slf4j接口转交给commons-logging接口来做实现

#### 2. 记录日志需要使用那些包
根据自己的需求来选择日志系统

1. 先选择日志接口，slf4j or commons-logging?
2. 选择log4j 2 还是 logback作为日志底层 （不建议选择jul, log4j）
3. 根据系统中存在的日志系统选择相应的log4j 2或者slf4j与这些系统的桥接包，来接管系统已有的日志系统到你选择的日志系统(log4j 2 or logback)

***注意包存在冲突，不能同时存在，原因是一些包是做桥接的，将日志系统A委托给日志系统B实现，存在日志A委托给B，B又委托给A，就存在冲突，产生内存溢出***

* jcl-over-slf4j 与 slf4j-jcl
* log4j-over-slf4j 与 slf4j-log4j12
* jul-to-slf4j 与 slf4j-jdk14

#### 3. 有些包应该是属于同一个系统，为什么要拆分那么包，如logback-core、logback-classic、logback-access？
日志系统内，各个包的作用不同，做了合理的业务拆分，可以根据具体业务需求加载，更合理。详见各个包的作用说明
#### 4. log4j-over-slf4j，slf4j-jcl这些包名看起来跟两个系统有关系，那么关系是什么？为什么会有这些关系？
因为一个程序或者系统依赖的lib中各个库都有自己的日志系统实现，可能才log4j，jul，并采用不同的日志统一接口，commons-log或slf4j。程序或者系统选择或需要采用slf4j+logback，在不改动lib库依赖的包代码的前提下，通过log4j-over-slf4j这样的包，来将lib库中日志系统委托给slf4j+logback实现。
#### 5. log4j1和log4j2这种像同一套系统，为什么需要区分？
log4j 2是对log4j的升级，log4j已经停止维护。




