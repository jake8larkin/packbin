package com.jakeokt.packbin

import java.nio.{ByteBuffer, ByteOrder}

object BytePacking {

  def unpack(data: String, template: String): Seq[BigInt] = {
    unpack(data.getBytes, template)
  }

  def unpack(data: Array[Byte], template: String): Seq[BigInt] = {
    unpack(ByteBuffer.wrap(data), template)
  }

  def unpack(buf: ByteBuffer, template: String): Seq[BigInt] = {
    // JVM is BigEndian, but Ruby encodes in platform native, Little Endian
    buf.order(java.nio.ByteOrder.nativeOrder)
    template.map { directive =>
      val bytePacker = BytePacker.getBytePackerFor(directive)
      bytePacker.unpack(buf, directive)
    }
  }

  def pack(data: Seq[Any], template: String): ByteBuffer = {
    // helper function
    def toBigInt(x: Any): BigInt = x match {
      case c: BigInt => c
      case c: Long => c
      case c: Int => c
      case c: Short => c
      case c: Byte => c
    }

    val buf = ByteBuffer.allocate(BytePacker.sizeOf(template))
    buf.order(java.nio.ByteOrder.LITTLE_ENDIAN)

    data.map(toBigInt(_)) zip template map { case(num, directive) =>
      val bytePacker = BytePacker.getBytePackerFor(directive)
      bytePacker.pack(num, directive, buf)
    }
    buf.flip()
    buf
  }

  def sizeOf(template: String): Int = BytePacker.sizeOf(template)
}

