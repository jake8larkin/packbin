package com.jakeokt.packbin

import org.scalatest.{FunSpec, Matchers}

class BytePackingSpec extends FunSpec with Matchers {
  describe("pack") {
    it("should pack directives in little endian") {
      val expectedBytes = Array(1, 2, 0, 3, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0)
      BytePacking.pack(List(1,2,3,4), "CSLQ").array() should be (expectedBytes)
    }

    it("should pack above the unsigned range") {
      val expectedBytes = Array(-127, 1, -128, 1, 0, 0, -128, 1, 0, 0, 0, 0, 0, 0, -128)
      val lst = List(129, 32769, 2147483649l, BigInt("9223372036854775809"))
      BytePacking.pack(lst, "CSLQ").array() should be (expectedBytes)
    }

    it("should pack out of signed range and overflow") {
      val expectedBytes = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
      val lst = List(256, 65536, BigInt("4294967296"), BigInt("18446744073709551616"))
      BytePacking.pack(lst, "CSLQ").array() should be (expectedBytes)
    }

    it("should pack negative integers") {
      val expectedBytes = Array(-1, -2, -1, -3, -1, -1, -1, -4, -1, -1, -1, -1, -1, -1, -1)
      BytePacking.pack(List(-1,-2,-3,-4), "CSLQ").array() should be (expectedBytes)
    }

    it("should pack negative integers out of signed range and overflow") {
      val expectedBytes = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
      val lst = List(-256, -65536, BigInt("-4294967296"), BigInt("-18446744073709551616"))
      BytePacking.pack(lst, "CSLQ").array() should be (expectedBytes)
    }
  }

  describe ("unpack") {
    it("should pack / unpack the simple case") {
      val orig = List(1,2,3,4)
      BytePacking.unpack(BytePacking.pack(orig, "CSLQ"), "CSLQ") should be (orig)
    }

    it("should pack / unpack in the unsigned range") {
      val orig = List(129, 32769, 2147483649l, BigInt("9223372036854775809"))
      BytePacking.unpack(BytePacking.pack(orig, "CSLQ"), "CSLQ") should be (orig)
    }

    it("should pack / unpack out of signed range with an expected overflow") {
      val orig = List(256, 65536, BigInt("4294967296"), BigInt("18446744073709551616"))
      val overflow = List(0,0,0,0)
      BytePacking.unpack(BytePacking.pack(orig, "CSLQ"), "CSLQ") should be (overflow)
    }

    it("should pack negative integers and unpack to their signed representation") {
      val orig = List(-1,-2,-3,-4)
      val signed = List(255, 65534, BigInt("4294967293"), BigInt("18446744073709551612"))
      BytePacking.unpack(BytePacking.pack(orig, "CSLQ"), "CSLQ") should be (signed)
    }
    it("should pack / unpack negative integers out of signed range with expected overflow") {
      val orig = List(-256, -65536, BigInt("-4294967296"), BigInt("-18446744073709551616"))
      val overflow = List(0,0,0,0)
      BytePacking.unpack(BytePacking.pack(orig, "CSLQ"), "CSLQ") should be (overflow)
    }
  }
}
