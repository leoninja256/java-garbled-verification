import java.nio.charset.Charset

plugins {
    java
    application
}

group = "net.leoninja.javagarbledverification"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/com.google.guava/guava
    implementation("com.google.guava:guava:33.2.1-jre")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

val encodingJvmArgs = listOf(
    "-Dfile.encoding=UTF-8",
    "-Dsun.stdout.encoding=UTF-8",
    "-Dsun.stderr.encoding=UTF-8",
    "-Dstdout.encoding=UTF-8",
    "-Dstderr.encoding=UTF-8",
)

application {
    mainClass = "net.leoninja.javagarbledverification.gradlesample.GradleSampleMain"
    // build/distributionsに生成されるバッチ内でもデフォルトVM引数に指定したい場合は以下を設定する
    // applicationDefaultJvmArgs = encodingJvmArgs
}

tasks.withType<JavaExec> {
    // JavaExec型のタスクのVM引数にエンコーディング関連のプロパティを追加する
    jvmArgs(encodingJvmArgs)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}

tasks.test {
    // テスト時のVM引数にエンコーディング関連のプロパティを追加する
    jvmArgs(encodingJvmArgs)

    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}

tasks.wrapper {
    // gradlew.batの先頭に"chcp 65001"を追加する
    // powershellの場合もgradlew.batから実行する限りはこれで対応できる
    doLast {
        val charset = Charset.forName("MS932")
        val gradlewBat = file("gradlew.bat")
        gradlewBat.writeText("@echo off & chcp 65001 & @echo on\r\n" + gradlewBat.readText(charset), charset)
    }
}
