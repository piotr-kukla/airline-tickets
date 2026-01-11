package pl.algocode.airline.util

import pl.algocode.airline.util.Strings.{asId, Id}

trait IdGenerator:
  def nextId[U](): Id[U]

object DefaultIdGenerator extends IdGenerator:
  override def nextId[U](): Id[U] = SecureRandomIdGenerator.Strong.generate.asId[U]
