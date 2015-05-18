# SBT Scala script args parser

### 使用方式 (Usage)

加入依賴 (Append dependency)
```scala
/***
  scalaVersion := "2.11.6"

  Resolver.sonatypeRepo("releases")
  
  libraryDependencies += "tw.com.ehanlin" %% "sbt-script-args-parser" % "0.0.1"
*/

import com.ehanlin.argsParser.ArgsParser
import com.ehanlin.argsParser.ArgsParserMethods._
```

定義傳入參數 (Define the parameters)
```scala
//定義開關型的參數 (Define Boolean type parameter)
val flag : Boolean = *("f") desc "flag description"

//定義傳入值型的參數，必須要有預設值 (Define String type parameter, must have a default value)
val option : String = **("option") desc "option description" default "default value"

//若所有參數都是開關型的參數，在定義最後一個參數時使用 *!
//(If all parameters are Boolean type, when defining the last parameter use *!)
val lastFlag : Boolean = *!("l") desc "last flag description"

//若有傳入值型參數，則將想當成預設參數的值入值型參數放在最後一個使用 **! 定義
//(Defining the last String type parameter use *!, become default parameter)
val lastOption : String = **("lastOption") desc "last option description" default "default value"
```

開關型的參數，開啟 f (Setting Boolean type parameter f is true)
```shell
script.scala *f
```

開關型的參數，開啟 a,b,c (Setting Boolean type parameters a, b, c are true)
```shell
script.scala *abc
```

傳入值型的參數，option 值設為 optionValue (Setting String type parameter option is optionValue)
```
script.scala *option optionValue
```

傳入值型的參數，lastOption 值設為 lastOptionValue (Setting String type parameter lastOption is lastOptionValue)
```
script.scala lastOptionValue
```

幫助參數，於執行完印出幫助選項後，會強制結束程式 (Help task, after print help, forced end process)
```shell
script.scala *help
```

### 例子 (Example)

script.scala
```scala
#!/usr/bin/env scalas

/***
  scalaVersion := "2.11.6"
  
  resolvers += Resolver.sonatypeRepo("snapshots")

  libraryDependencies ++= Seq(
    "org.mongodb" %% "casbah" % "2.8.1",
    "tw.com.ehanlin" %% "sbt-script-args-parser" % "0.0.1"
  )
*/

import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import com.ehanlin.argsParser.ArgsParser
import com.ehanlin.argsParser.ArgsParserMethods._


implicit def stringToInt(value : String) = value.toInt
implicit def stringToDBObject(value : String) = JSON.parse(value).asInstanceOf[DBObject]


implicit val ap = new ArgsParser(args)

val viewFlag : Boolean =     *("v")       desc "show log"
val debugFlag : Boolean =    *("b")       desc "show debug"
val timeFlag : Boolean =     *("t")       desc "show timestamp"
val durationFlag : Boolean = *("d")       desc "show run duration"
val srcHost : String =      **("srcHost") desc "source db ip"   default "10.13.104.149"
val srcPort : Int =         **("srcPort") desc "source db port" default "27017"
val srcDb : String =        **("srcDb")   desc "source db name" default "hanlin"
val host : String =         **("host")    desc "db ip"          default "127.0.0.1"
val port : Int =            **("port")    desc "db port"        default "27017"
val db : String =           **("db")      desc "db name"        default "ehanlin_104"
val query : DBObject =     **!("query")   desc "question query" default "{}"



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
./script.scala *vt *help **srcPort 1234 **srcHost 1.2.3.4 *d '{a:"a",b:123}'
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
./script.scala *help
```

help output
```shell
Default Option is **query
**query               default = {}                    question query
**db                  default = ehanlin_104           db name
**port                default = 27017                 db port
**host                default = 127.0.0.1             db ip
**srcDb               default = hanlin                source db name
**srcPort             default = 27017                 source db port
**srcHost             default = 10.13.104.149         source db ip
*d                    flag                            show run duration
*t                    flag                            show timestamp
*b                    flag                            show debug
*v                    flag                            show log
*help                 flag                            HELP!
```