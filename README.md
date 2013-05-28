## lebooo-admin

[乐播](http://lebooo.com)后台管理系统。

代码中不包含乐播api地址等私有信息，所以实际上大部分功能是不可用的。

## 环境要求

- JDK 1.6
- Maven 3
- SpringSide4 4.1.0-SNAPSHOT

        git clone https://github.com/springside/springside4.git
        cd springside4/modules
        mvn install

## 启动

1. 执行 `mvn antrun:run -Prefresh-db` 初始化h2数据库。
2. 执行 `mvn jetty:run` 可以正常启动工程。

如果因为缺少某些依赖导致失败，请检查你的maven配置(或maven私服)，确保包含以下仓库：

    <repositories>
      <repository>
        <id>central</id>
        <name>Maven Central</name>
        <url>http://repo1.maven.org/maven2/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>

      <repository>
        <id>spy</id>
        <name>Spy Repository</name>
        <url>http://files.couchbase.com/maven2/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>

      <repository>
        <id>org.springframework.maven.milestone</id>
        <name>Spring Maven Milestone Repository</name>
        <url>http://maven.springframework.org/milestone/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>

      <repository>
        <id>JBoss</id>
        <name>JBoss Repositories</name>
        <url>http://repository.jboss.org/nexus/content/groups/public-jboss/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>

      <repository>
        <id>sonatype</id>
        <name>Sonatype Repository</name>
        <url>http://repository.sonatype.org/content/groups/public/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>

      <repository>
        <id>javanet</id>
        <name>java.net</name>
        <url>http://download.java.net/maven/2/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>

      <repository>
        <id>tokyotyrant-java</id>
        <name>tokyotyrant-java</name>
        <url>http://tokyotyrant-java.googlecode.com/svn/maven/repository/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>
    </repositories>
