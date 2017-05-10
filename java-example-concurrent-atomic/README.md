# Java - Concurrent Atomic 原子性

> 参考Jakob Jenkov的[java.util.concurrent](http://tutorials.jenkov.com/java-util-concurrent/index.html)

#### *AtomicBoolean*

- get()
- set(newValue)
- getAndSet(newValue)
- compareAndSet(expectedValue, newValue)

#### *AtomicInteger*

- get()
- set(newValue)
- getAndSet(newValue)
- compareAndSet(expectedValue, newValue)
- getAndIncrement()
- getAndDecrement()
- getAndAdd(delta)
- incrementAndGet()
- decrementAndGet()
- addAndGet(delta)
- getAndUpdate(function)
- updateAndGet(function)
- getAndAccumulate(delta, function)
- accumulateAndGet(delta, function)

#### *AtomicLong*

与AtomicInteger相同

#### *AtomicReference*

- get()
- set(newValue)
- getAndSet(newValue)
- compareAndSet(expectedValue, newValue)
- getAndUpdate(function)
- updateAndGet(function)
- getAndAccumulate(delta, function)
- accumulateAndGet(delta, function)

#### *AtomicMarkableReference*

- get(markHolders)
- getReference()
- isMarked()
- set(newReference, newMark)
- compareAndSet(expectedReference, newReference, expectedMark, newMark)
- attemptMark(expectedReference, newMark)

#### *AtomicStampedReference*

- get(stampHolders)
- getReference()
- getStamp()
- set(newReference, newStamp)
- compareAndSet(expectedReference, newReference, expectedStamp, newStamp)
- attemptStamp(expectedReference, newStamp)

#### *AtomicIntegerArray*

- length()
- get(index)
- set(index, newValue)
- getAndSet(index, newValue)
- compareAndSet(index, expectedValue, newValue)
- getAndIncrement(index)
- getAndDecrement(index)
- getAndAdd(index, delta)
- incrementAndGet(index)
- decrementAndGet(index)
- addAndGet(index, delta)
- getAndUpdate(index, function)
- updateAndGet(index, function)
- getAndAccumulate(index, delta, function)
- accumulateAndGet(index, delta, function)

#### *AtomicLongArray*

与AtomicIntegerArray相同

#### *AtomicReferenceArray*

- length()
- get(index)
- set(index, newValue)
- getAndSet(index, newValue)
- compareAndSet(index, expectedValue, newValue)
- getAndUpdate(index, function)
- updateAndGet(index, function)
- getAndAccumulate(index, delta, function)
- accumulateAndGet(index, delta, function)

*PS：本文使用的是java-1.8*