package arktekk.oppgave3

sealed trait TomlVal

case class TomlInteger(value: String)       extends TomlVal
case class TomlBoolean(value: String)       extends TomlVal
case class TomlDateLocal(value: String)     extends TomlVal
case class TomlDateTime(value: String)      extends TomlVal
case class TomlDateTimeLocal(value: String) extends TomlVal
case class TomlTimeLocal(value: String)     extends TomlVal
