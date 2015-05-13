# SBT Scala script args parser

### 使用方式 Usage

script.scala
```scala
/***
  scalaVersion := "2.11.6"
  libraryDependencies ++= Seq(
    "org.mongodb" %% "casbah" % "2.8.1",
    "com.ehanlin" %% "sbt-sbt-script-args-parser" % "0.0.1-SNAPSHOT"
  )
*/

import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import com.ehanlin.argsParser.ArgsParser

implicit def stringToInt(value : String) = value.toInt
implicit def stringToDBObject(value : String) = JSON.parse(value).asInstanceOf[DBObject]

val ap = new ArgsParser(args)
val viewFlag : Boolean = ap.~("v", "看到執行過程")
val debugFlag : Boolean = ap.~("b", "看到 debug 過程")
val timeFlag : Boolean = ap.~("t", "看到時間綽記")
val durationFlag : Boolean = ap.~("d", "看到執行時間")
val srcHost : String = ap.~~("srcHost", "來源題庫的 db 位置", "10.13.104.149")
val srcPort : Int = ap.~~("srcPort", "來源題庫的 port 號", "27017")
val srcDb : String = ap.~~("srcDb", "來源題庫的 db 名稱", "hanlin")
val host : String = ap.~~("host", "db 位置", "127.0.0.1")
val port : Int = ap.~~("port", "port 號", "27017")
val db : String = ap.~~("db", "db 名稱", "ehanlin_104")
val query : DBObject = ap.~~!("query", "來源題庫要轉的題目的查詢式", "{}")


println(s"srcHost = $srcHost")
println(s"srcPort = $srcPort")
println(s"srcDb = $srcDb")
println(s"host = $host")
println(s"port = $port")
println(s"db = $db")
println(s"query = $query")
println(s"viewFlag = $viewFlag")
println(s"debugFlag = $debugFlag")
println(s"timeFlag = $timeFlag")
println(s"durationFlag = $durationFlag")
```

run script
```shell
./script.scala ~vt ~help ~~srcPort 1234 ~~srcHost 1.2.3.4 ~d '{a:"a",b:123}'
```

output
```shell
srcHost = 1.2.3.4
srcPort = 1234
srcDb = hanlin
host = 127.0.0.1
port = 27017
db = ehanlin_104
query = { "a" : "a" , "b" : 123}
viewFlag = true
debugFlag = false
timeFlag = true
durationFlag = true
```

run script help
```shell
./script.scala ~help
```

help output
```shell
Default Option is ~~query
~~query               default = {}                    來源題庫要轉的題目的查詢式
~~db                  default = ehanlin_104           db 名稱
~~port                default = 27017                 port 號
~~host                default = 127.0.0.1             db 位置
~~srcDb               default = hanlin                來源題庫的 db 名稱
~~srcPort             default = 27017                 來源題庫的 port 號
~~srcHost             default = 10.13.104.149         來源題庫的 db 位置
~d                    flag                            看到執行時間
~t                    flag                            看到時間綽記
~b                    flag                            看到 debug 過程
~v                    flag                            看到執行過程
~help                 flag                            HELP!
```