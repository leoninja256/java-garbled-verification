# java-garbled-verification - Java21

## 前提条件

* ソースコード、コンパイル時のエンコーディングはUTF-8とする
* 実行時の`file.encoding`はUTF-8とする
* [gradle-sample](./gradle-sample/) または [maven-sample](./maven-sample/)プロジェクトを使用する
* IDEからのテスト実行、またはビルドツールのtestタスクの実行を行う

### ソフトウェアバージョン

| ソフトウェア | バージョン |
| --- | --- |
| Oracle JDK | 21.0.3+7-LTS-152 |
| Gradle (wrapper) | 8.8 |
| Maven (wrapper) | 3.9.6 |
| IntelliJ IDEA | 2024.1.4 |
| Eclipse (Pleiades) | 4.32.0 |
| Windows 11 Pro | 22631.3810 |
| cmd | 22631.3810 |
| Windows PowerShell | 5.1.22621.3810 |
| PowerShell 7 | 7.4.3 |

## 結論

Java21の場合は以下を設定しておく。

※wrapperで起動した際に`chcp 65001`させることで解決した。  
　wrapperのバッチファイルはコミット対象のファイルなのでメンバーに簡単に設定を共有できる。  
※Gradleはかなり面倒……。

### Gradle

* `gradle.properties`
  ```properties
  org.gradle.jvmargs=-Dfile.encoding=UTF-8
  ```
* `build.gradle.kts`
  ```gradle
  val encodingJvmArgs = listOf(
      "-Dfile.encoding=UTF-8",
      "-Dsun.stdout.encoding=UTF-8",
      "-Dsun.stderr.encoding=UTF-8",
      "-Dstdout.encoding=UTF-8",
      "-Dstderr.encoding=UTF-8",
  )

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
      ...
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
  ```
* `gradlew.bat`
  ```bat
  @echo off & chcp 65001 & @echo on
  @rem
  @rem Copyright 2015 the original author or authors.
  ...
  ```

### Maven

* `.mvn/jvm.config`
  ```
  -Dfile.encoding=UTF-8
  ```
* `pom.xml`
  ```xml
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      </properties>

      <build>
          <plugins>
              <plugin>
                  <artifactId>maven-surefire-plugin</artifactId>
                  <version>3.1.2</version>
                  <configuration>
                      <argLine>-Dfile.encoding=UTF-8</argLine>
  ```
* `mvnw.cmd`  
  ```cmd
  <# : batch portion
  @echo off & chcp 65001 & @echo on
  @REM ----------------------------------------------------------------------------
  ...
  ```

### IntelliJ IDEA

* IntelliJ IDEA - VMカスタムオプション
  * `-Dfile.encoding=UTF-8`

### Eclipse

* Eclipse - eclipse.ini
  * `-Dfile.encoding=UTF-8`

## 以下は検証実施のログ

### Gradle

#### cmd

文字化けなし。

* gradle.properties
  * `org.gradle.jvmargs=-Dfile.encoding=UTF-8`
* gradlew.bat
  * `chcp 65001`を最初の方に追加
* build.gradle.ktsのエンコーディング設定を色々追加
  * https://github.com/leoninja256/java-garbled-verification/blob/main/gradle-sample/build.gradle.kts

<details>
<summary>テスト実行時のコンソール</summary>

```
> Task :test

GradleSampleTest > test() STANDARD_OUT
    Java Runtime version      :21.0.3+7-LTS-152
    Charset.defaultCharset()  :UTF-8
    ----------------------------------------------
    "file.encoding"          = UTF-8
    "native.encoding"        = MS932
    "sun.jnu.encoding"       = MS932
    "sun.stdout.encoding"    = UTF-8
    "sun.stderr.encoding"    = UTF-8
    "stdout.encoding"    = UTF-8
    "stderr.encoding"    = UTF-8
    ----------------------------------------------
    こんにちは世界。
    "あいうえお".getByte()   = E38182E38184E38186E38188E3818A

GradleSampleTest > test() STANDARD_ERROR
    java.io.FileNotFoundException: NOT_FOUND (指定されたファイルが見つかりません。)
        at java.base/java.io.FileInputStream.open0(Native Method)
        ...
        at worker.org.gradle.process.internal.worker.GradleWorkerMain.main(GradleWorkerMain.java:74)

GradleSampleTest > test() STANDARD_OUT
    ----------------------------------------------
```
</details>

#### Windows PowerShell

文字化けなし。

* gradle.properties
  * `org.gradle.jvmargs=-Dfile.encoding=UTF-8`
* gradlew.bat
  * `chcp 65001`を最初の方に追加
* build.gradle.ktsのエンコーディング設定を色々追加
  * https://github.com/leoninja256/java-garbled-verification/blob/main/gradle-sample/build.gradle.kts

<details>
<summary>テスト実行時のコンソール</summary>

```
> Task :test

GradleSampleTest > test() STANDARD_OUT
    Java Runtime version      :21.0.3+7-LTS-152
    Charset.defaultCharset()  :UTF-8
    ----------------------------------------------
    "file.encoding"          = UTF-8
    "native.encoding"        = MS932
    "sun.jnu.encoding"       = MS932
    "sun.stdout.encoding"    = UTF-8
    "sun.stderr.encoding"    = UTF-8
    "stdout.encoding"    = UTF-8
    "stderr.encoding"    = UTF-8
    ----------------------------------------------
    こんにちは世界。
    "あいうえお".getByte()   = E38182E38184E38186E38188E3818A

GradleSampleTest > test() STANDARD_ERROR
    java.io.FileNotFoundException: NOT_FOUND (指定されたファイルが見つかりません。)
        at java.base/java.io.FileInputStream.open0(Native Method)
        ...
        at worker.org.gradle.process.internal.worker.GradleWorkerMain.main(GradleWorkerMain.java:74)

GradleSampleTest > test() STANDARD_OUT
    ----------------------------------------------
```
</details>

#### PowerShell 7

文字化けなし。

* gradle.properties
  * `org.gradle.jvmargs=-Dfile.encoding=UTF-8`
* gradlew.bat
  * `chcp 65001`を最初の方に追加
* build.gradle.ktsのエンコーディング設定を色々追加
  * https://github.com/leoninja256/java-garbled-verification/blob/main/gradle-sample/build.gradle.kts

<details>
<summary>テスト実行時のコンソール</summary>

```
> Task :test

GradleSampleTest > test() STANDARD_OUT
    Java Runtime version      :21.0.3+7-LTS-152
    Charset.defaultCharset()  :UTF-8
    ----------------------------------------------
    "file.encoding"          = UTF-8
    "native.encoding"        = MS932
    "sun.jnu.encoding"       = MS932
    "sun.stdout.encoding"    = UTF-8
    "sun.stderr.encoding"    = UTF-8
    "stdout.encoding"    = UTF-8
    "stderr.encoding"    = UTF-8
    ----------------------------------------------
    こんにちは世界。
    "あいうえお".getByte()   = E38182E38184E38186E38188E3818A

GradleSampleTest > test() STANDARD_ERROR
    java.io.FileNotFoundException: NOT_FOUND (指定されたファイルが見つかりません。)
        at java.base/java.io.FileInputStream.open0(Native Method)
        ...
        at worker.org.gradle.process.internal.worker.GradleWorkerMain.main(GradleWorkerMain.java:74)

GradleSampleTest > test() STANDARD_OUT
    ----------------------------------------------
```
</details>

### Maven

#### cmd

文字化けなし。

* Maven - jvm.config
  * `-Dfile.encoding=UTF-8`
* Maven - maven-surefire-plugin - configuration
  * `<argLine>-Dfile.encoding=UTF-8</argLine>`
  * テスト実施時のfile.encodingはここで指定しないと効果なし
* Maven - mvnw.cmd
  * `chcp 65001`をmvnw.cmdの最初の方に追加

<details>
<summary>テスト実行時のコンソール</summary>

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running net.leoninja.javagarbledverification.gradlesample.MavenSampleTest
Java Runtime version      :21.0.3+7-LTS-152
Charset.defaultCharset()  :UTF-8
----------------------------------------------
"file.encoding"          = UTF-8
"native.encoding"        = MS932
"sun.jnu.encoding"       = MS932
"sun.stdout.encoding"    = null
"sun.stderr.encoding"    = null
"stdout.encoding"    = MS932
"stderr.encoding"    = MS932
----------------------------------------------
こんにちは世界。
"あいうえお".getByte()   = E38182E38184E38186E38188E3818A
java.io.FileNotFoundException: NOT_FOUND (指定されたファイルが見つかりません。)
        at java.base/java.io.FileInputStream.open0(Native Method)
        ...
        at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:495)
----------------------------------------------
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.040 s -- in net.leoninja.javagarbledverification.gradlesample.MavenSampleTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.470 s
[INFO] Finished at: 2024-07-27T17:07:46+09:00
[INFO] ------------------------------------------------------------------------
```
</details>

#### Windows PowerShell

文字化けなし。

* Maven - jvm.config
  * `-Dfile.encoding=UTF-8`
* Maven - maven-surefire-plugin - configuration
  * `<argLine>-Dfile.encoding=UTF-8</argLine>`
  * テスト実施時のfile.encodingはここで指定しないと効果なし
* Maven - mvnw.cmd
  * `chcp 65001`をmvnw.cmdの最初の方に追加

<details>
<summary>テスト実行時のコンソール</summary>

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running net.leoninja.javagarbledverification.gradlesample.MavenSampleTest
Java Runtime version      :21.0.3+7-LTS-152
Charset.defaultCharset()  :UTF-8
----------------------------------------------
"file.encoding"          = UTF-8
"native.encoding"        = MS932
"sun.jnu.encoding"       = MS932
"sun.stdout.encoding"    = null
"sun.stderr.encoding"    = null
"stdout.encoding"    = MS932
"stderr.encoding"    = MS932
----------------------------------------------
こんにちは世界。
"あいうえお".getByte()   = E38182E38184E38186E38188E3818A
java.io.FileNotFoundException: NOT_FOUND (指定されたファイルが見つかりません。)
        at java.base/java.io.FileInputStream.open0(Native Method)
        ...
        at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:495)
----------------------------------------------
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.041 s -- in net.leoninja.javagarbledverification.gradlesample.MavenSampleTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.467 s
[INFO] Finished at: 2024-07-27T17:09:14+09:00
[INFO] ------------------------------------------------------------------------
```
</details>

#### PowerShell 7

文字化けなし。

* Maven - jvm.config
  * `-Dfile.encoding=UTF-8`
* Maven - maven-surefire-plugin - configuration
  * `<argLine>-Dfile.encoding=UTF-8</argLine>`
  * テスト実施時のfile.encodingはここで指定しないと効果なし
* Maven - mvnw.cmd
  * `chcp 65001`をmvnw.cmdの最初の方に追加

<details>
<summary>テスト実行時のコンソール</summary>

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running net.leoninja.javagarbledverification.gradlesample.MavenSampleTest
Java Runtime version      :21.0.3+7-LTS-152
Charset.defaultCharset()  :UTF-8
----------------------------------------------
"file.encoding"          = UTF-8
"native.encoding"        = MS932
"sun.jnu.encoding"       = MS932
"sun.stdout.encoding"    = null
"sun.stderr.encoding"    = null
"stdout.encoding"    = MS932
"stderr.encoding"    = MS932
----------------------------------------------
こんにちは世界。
"あいうえお".getByte()   = E38182E38184E38186E38188E3818A
java.io.FileNotFoundException: NOT_FOUND (指定されたファイルが見つかりません。)
        at java.base/java.io.FileInputStream.open0(Native Method)
        ...
        at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:495)
----------------------------------------------
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.037 s -- in net.leoninja.javagarbledverification.gradlesample.MavenSampleTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.499 s
[INFO] Finished at: 2024-07-27T17:16:36+09:00
[INFO] ------------------------------------------------------------------------
```
</details>

### IntelliJ IDEA + Gradle

文字化けなし。

* gradle.properties
  * `org.gradle.jvmargs=-Dfile.encoding=UTF-8`
* gradlew.bat
  * `chcp 65001`を最初の方に追加
* build.gradle.ktsのエンコーディング設定を色々追加
  * https://github.com/leoninja256/java-garbled-verification/blob/main/gradle-sample/build.gradle.kts

<details>
<summary>テスト実行時のコンソール</summary>

```
> Task :testClasses UP-TO-DATE
Java Runtime version      :21.0.3+7-LTS-152
Charset.defaultCharset()  :UTF-8
----------------------------------------------
"file.encoding"          = UTF-8
"native.encoding"        = MS932
"sun.jnu.encoding"       = MS932
"sun.stdout.encoding"    = UTF-8
"sun.stderr.encoding"    = UTF-8
"stdout.encoding"    = UTF-8
"stderr.encoding"    = UTF-8
----------------------------------------------
こんにちは世界。
"あいうえお".getByte()   = E38182E38184E38186E38188E3818A
java.io.FileNotFoundException: NOT_FOUND (指定されたファイルが見つかりません。)
	at java.base/java.io.FileInputStream.open0(Native Method)
  ...
	at worker.org.gradle.process.internal.worker.GradleWorkerMain.main(GradleWorkerMain.java:74)
----------------------------------------------
> Task :test
```
</details>

### IntelliJ IDEA + Maven

文字化けなし。  
Mavenと関係なく実行されている様子。

* IntelliJ IDEA - VMカスタムオプション
  * `-Dfile.encoding=UTF-8`

<details>
<summary>テスト実行時のコンソール</summary>

```
Java Runtime version      :21.0.3+7-LTS-152
Charset.defaultCharset()  :UTF-8
----------------------------------------------
"file.encoding"          = UTF-8
"native.encoding"        = MS932
"sun.jnu.encoding"       = MS932
"sun.stdout.encoding"    = UTF-8
"sun.stderr.encoding"    = UTF-8
"stdout.encoding"    = UTF-8
"stderr.encoding"    = UTF-8
----------------------------------------------
こんにちは世界。
"あいうえお".getByte()   = E38182E38184E38186E38188E3818A
java.io.FileNotFoundException: NOT_FOUND (指定されたファイルが見つかりません。)
	at java.base/java.io.FileInputStream.open0(Native Method)
  ...
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:55)
----------------------------------------------
```
</details>

### Eclipse

文字化けなし。  
GradleもMavenも関係なく実行されている様子。

* Eclipse - eclipse.ini
  * `-Dfile.encoding=UTF-8`

<details>
<summary>テスト実行時のコンソール</summary>

```
Java Runtime version      :21.0.3+7-LTS-152
Charset.defaultCharset()  :UTF-8
----------------------------------------------
"file.encoding"          = UTF-8
"native.encoding"        = MS932
"sun.jnu.encoding"       = MS932
"sun.stdout.encoding"    = null
"sun.stderr.encoding"    = null
"stdout.encoding"    = UTF-8
"stderr.encoding"    = UTF-8
----------------------------------------------
こんにちは世界。
"あいうえお".getByte()   = E38182E38184E38186E38188E3818A
java.io.FileNotFoundException: NOT_FOUND (指定されたファイルが見つかりません。)
	at java.base/java.io.FileInputStream.open0(Native Method)
  ...
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:210)
----------------------------------------------
```
</details>
