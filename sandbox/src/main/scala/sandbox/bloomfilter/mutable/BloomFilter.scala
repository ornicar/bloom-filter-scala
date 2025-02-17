package sandbox.bloomfilter.mutable

import bloomfilter.CanGenerateHashFrom

class BloomFilter[T] private (val numberOfBits: Long, val numberOfHashes: Int, private val bits: BitArray)
    (implicit canGenerateHash: CanGenerateHashFrom[T]):

  def this(numberOfBits: Long, numberOfHashes: Int)(implicit canGenerateHash: CanGenerateHashFrom[T]) =
    this(numberOfBits, numberOfHashes, new BitArray(numberOfBits))

  def add(x: T): Unit =
    val hash = canGenerateHash.generateHash(x)
    val hash1 = hash >>> 32
    val hash2 = (hash << 32) >> 32

    var i = 0
    while (i < numberOfHashes)
      val computedHash = hash1 + i * hash2
      bits.set((computedHash & Long.MaxValue) % numberOfBits)
      i += 1

  def union(that: BloomFilter[T]): BloomFilter[T] =
    require(this.numberOfBits == that.numberOfBits && this.numberOfHashes == that.numberOfHashes,
      s"Union works only on BloomFilters with the same number of hashes and of bits")
    new BloomFilter[T](this.numberOfBits, this.numberOfHashes, this.bits | that.bits)

  def intersect(that: BloomFilter[T]): BloomFilter[T] =
    require(this.numberOfBits == that.numberOfBits && this.numberOfHashes == that.numberOfHashes,
      s"Intersect works only on BloomFilters with the same number of hashes and of bits")
    new BloomFilter[T](this.numberOfBits, this.numberOfHashes, this.bits & that.bits)

  def mightContain(x: T): Boolean =
    val hash = canGenerateHash.generateHash(x)
    val hash1 = hash >>> 32
    val hash2 = (hash << 32) >> 32
    var i = 0
    while (i < numberOfHashes)
      val computedHash = hash1 + i * hash2
      if (!bits.get((computedHash & Long.MaxValue) % numberOfBits))
        return false
      i += 1
    true

  def expectedFalsePositiveRate(): Double =
    math.pow(bits.getBitCount.toDouble / numberOfBits, numberOfHashes.toDouble)


object BloomFilter:

  def apply[T](numberOfItems: Long, falsePositiveRate: Double)
      (implicit canGenerateHash: CanGenerateHashFrom[T]): BloomFilter[T] =

    val nb = optimalNumberOfBits(numberOfItems, falsePositiveRate)
    val nh = optimalNumberOfHashes(numberOfItems, nb)
    new BloomFilter[T](nb, nh)

  def optimalNumberOfBits(numberOfItems: Long, falsePositiveRate: Double): Long =
    math.ceil(-1 * numberOfItems * math.log(falsePositiveRate) / math.log(2) / math.log(2)).toLong

  def optimalNumberOfHashes(numberOfItems: Long, numberOfBits: Long): Int =
    math.ceil(numberOfBits / numberOfItems * math.log(2)).toInt

