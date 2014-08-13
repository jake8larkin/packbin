package com.jakeokt.packbin

import java.nio.{ByteBuffer, ByteOrder}

object BytePacker {
  val UNSIGNED_BYTE = 'C'
  val UNSIGNED_SHORT = 'S'
  val UNSIGNED_INT = 'L'
  val UNSIGNED_LONG = 'Q'

  val UNSIGNED = List(UNSIGNED_BYTE, UNSIGNED_SHORT, UNSIGNED_INT, UNSIGNED_LONG)

  val SIZES = Map[Char, Int](
    UNSIGNED_BYTE -> 1,
    UNSIGNED_SHORT -> 2,
    UNSIGNED_INT -> 4,
    UNSIGNED_LONG -> 8)

  def getBytePackerFor(directive: Char): BytePacker = {
    directive match {
      case d if UNSIGNED.contains(d) => UnsignedBytePacker
      // implement signed, others here
    }
  }

  def sizeOf(template: String): Int = {
    template.map{ SIZES(_) }.sum
  }
}

trait BytePacker {

  def pack(num: BigInt, directive: Char, buf: ByteBuffer): ByteBuffer

  def unpack(buf: ByteBuffer, directive: Char): BigInt
}

