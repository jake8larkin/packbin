package com.jakeokt.packbin

import java.nio.ByteBuffer

object UnsignedBytePacker extends BytePacker {
  val UINT64_LIMIT = BigInt("18446744073709551615")

  def pack(num: BigInt, directive: Char, buf: ByteBuffer): ByteBuffer = {
    directive match {
      case BytePacker.UNSIGNED_BYTE  => buf.put(num.toByte)
      case BytePacker.UNSIGNED_SHORT => buf.putShort(num.toShort)
      case BytePacker.UNSIGNED_INT   => buf.putInt(num.toInt)
      case BytePacker.UNSIGNED_LONG  => buf.putLong(num.toLong)
    }
    buf
  }

  def unpack(buf: ByteBuffer, directive: Char): BigInt = {
    directive match {
      case BytePacker.UNSIGNED_BYTE  => BigInt(buf.get) & 0xFF
      case BytePacker.UNSIGNED_SHORT => BigInt(buf.getShort) & 0xFFFF
      case BytePacker.UNSIGNED_INT   => BigInt(buf.getInt) & 0xFFFFFFFFl
      case BytePacker.UNSIGNED_LONG  => BigInt(buf.getLong) & UINT64_LIMIT
    }
  }
}


