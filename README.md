packbin
=======

Ruby style byte-packed data interoperability for Scala


The aim of this is to be able to support the serialization and deserialization of binary data that is interoperable with Ruby pack/unpack encodings in Scala

Ruby Methods
- [Array#pack](http://www.ruby-doc.org/core-2.0.0/Array.html#method-i-pack)
- [String#unpack](http://ruby-doc.org/core-2.0.0/String.html#method-i-unpack)

This is a work in progress. right now only the C, S, L and Q directives are supported. Wildcard is not supported. Others should not be difficult to add.


Usage / Example
---

```
scala> import com.jakeokt.packbin._
import com.jakeokt.packbin._

scala> val data = BytePacking.pack(List(1,2,3,4), "CSLQ").array()
data: Array[Byte] = Array(1, 2, 0, 3, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0)

scala> BytePacking.unpack(data, "CSLQ")
res1: Seq[BigInt] = Vector(1, 2, 3, 4)
```
