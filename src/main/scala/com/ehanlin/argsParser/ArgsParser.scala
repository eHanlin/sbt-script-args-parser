package com.ehanlin.argsParser

class ArgsParser(args : Array[String]){

  val defaultOptionFlag = "~~defaultOption"
  val FlagRegex = "^~(.+)$".r
  val OptionRegex = "^~~(.+)$".r
  val optionTuples = optionsToTuple(args.toList)

  private var defaultOption : Option[String] = None


  def flagsToTuple(flags : List[Char]) : List[(String, Option[String])] = flags match {
    case flag :: other => (flag.toString, None) :: flagsToTuple(other)
    case Nil => Nil
  }

  def optionsToTuple(args : List[String]) : List[(String, Option[String])] = args match {
    case "~help" :: other => ("~help", None) :: optionsToTuple(other)
    case OptionRegex(option) :: value :: other => (option, Some(value)) :: optionsToTuple(other)
    case FlagRegex(flags) :: other => flagsToTuple(flags.toList) ::: optionsToTuple(other)
    case value :: Nil => (defaultOptionFlag, Some(value)) :: Nil
    case Nil => Nil
  }

  def findOptionTuple(option : String) = optionTuples.find{case(key, value) => key == option}


  case class OptionInfo(option : String, defaultValue : Option[String], description : String)
  private var optionInfoList = List[OptionInfo]()

  private def addOption(option : String, description : String, defaultValue : String) : String = {
    optionInfoList ::= OptionInfo(s"~~$option", Some(defaultValue), description)
    findOptionTuple(option) match {
      case Some((key, Some(value))) => value
      case Some((key, None)) => throw new IllegalArgumentException(s"~~$option lose value")
      case None => defaultValue
    }
  }

  def ~~ (option : String, description : String, defaultValue : String) : String = addOption(option, description, defaultValue)

  def ~~! (option : String, description : String, defaultValue : String) : String = {
    defaultOption = Some(s"~~$option")
    val result = addOption(option, description, defaultValue)
    this.!()
    findOptionTuple(defaultOptionFlag) match {
      case Some((key, Some(value))) => value
      case Some(_) => result
      case None => result
    }
  }

  private def addFlag(option : String, description : String) : Boolean = {
    optionInfoList ::= OptionInfo(s"~$option", None, description)
    findOptionTuple(option) match {
      case Some(_) => true
      case None => false
    }
  }

  def ~ (option : String, description : String) : Boolean = addFlag(option, description)

  def ~! (option : String, description : String) : Boolean = {
    val result = addFlag(option, description)
    this.!()
    result
  }

  private def end() : Unit = {
    if(optionTuples.contains(("~help", None))){
      defaultOption match {
        case Some(defaultOption) => println(s"Default Option is $defaultOption")
        case None =>
      }

      optionInfoList.foreach{
        case OptionInfo(option, Some(value), description) =>
          println(f"${option}%-20s  ${"default = " + value}%-30s  ${description}%s")
        case OptionInfo(option, None, description) =>
          println(f"${option}%-20s  ${"flag"}%-30s  ${description}%s")
      }
      println(f"${"~help"}%-20s  ${"flag"}%-30s  HELP!")
    }
  }

  def ! () : Unit = end()

}
