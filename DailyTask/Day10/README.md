# Day 10: JVM Memory Lab & Performance Benchmarking

## 📌 Summary
A hands-on JVM memory and performance laboratory exploring the inner workings of HotSpot JVM memory management:
- **Stack Memory**: Measuring recursive call frame depth before `StackOverflowError` across different `-Xss` thread stack sizes.
- **Heap Memory**: Exhausting heap space to trigger `OutOfMemoryError: Java heap space` under bounded `-Xmx64m`.
- **String Immutability vs StringBuilder**: Demonstrating quadratic $O(n^2)$ copying and garbage churn of `String +=` against amortized $O(n)$ `StringBuilder`.
- **Unified GC Logging (`-Xlog:gc`)**: Dissecting real-time garbage collector phases, pause times, and memory reclamation metrics.
- **Integer Cache Flyweight Pitfall**: Revealing why `==` fails for integers outside the `[-128, 127]` caching boundary.

---

## 📁 Project Structure

```text
DailyTask/
└── Day10/
    ├── pom.xml                                           # Maven configuration (Java 21, exec plugin)
    ├── README.md                                         # Revision guide & empirical results tables
    └── src/
        └── main/
            └── java/
                └── com/
                    └── hcl/
                        └── memory/
                            ├── StackDepthExperiment.java         # Recursion depth probe (-Xss)
                            ├── HeapOOMExperiment.java            # Heap memory exhaustion (-Xmx64m)
                            ├── StringConcatenationBenchmark.java # String += vs StringBuilder benchmark
                            ├── IntegerCachePitfall.java          # Integer cache [-128, 127] equality
                            └── MemoryLabApp.java                 # Master test coordinator & GC churn
```

---

## 📊 Recorded Experimental Results

### 1. StackOverflowError Depth vs. Thread Stack Size (`-Xss`)

Stack depth measured using infinite recursive calls until thread call stack exhaustion:

| JVM Flag | Configured Stack Size | Recursion Depth Reached | Empirical Observation |
|---|---|---|---|
| `-Xss256k` | 256 KB | **2,393 frames** | Smallest usable stack; exhausts rapidly under recursion. |
| `-Xss512k` | 512 KB | **5,365 frames** | Roughly $2.2\times$ depth of 256k. |
| *(Default)* | ~1024 KB (1 MB) | **17,800 frames** | Default 64-bit Windows JVM thread stack. |
| `-Xss1m` | 1024 KB (1 MB) | **21,045 frames** | Full 1 MB dedicated stack size. |

> **Key Takeaway**: Each Java method call allocates a **Stack Frame** (holding local variables, return address, and operand stack). `-Xss` controls memory per thread stack, not the shared heap.

---

### 2. OutOfMemoryError vs. Maximum Heap Size (`-Xmx`)

Tested by accumulating persistent 5 MB byte buffers into a collection until heap exhaustion:

| Configuration | Configured Max Heap | Allocated Before OOM | Result |
|---|---|---|---|
| `java -Xmx64m ...` | **64 MB** | **10 chunks (50 MB)** | `java.lang.OutOfMemoryError: Java heap space` |
| `java (Default)` | ~4,000 MB | **665 chunks (3,325 MB)** | `java.lang.OutOfMemoryError: Java heap space` |

> **Key Takeaway**: Under `-Xmx64m`, the JVM triggers OOM around 50 MB because JVM internal data structures, class metadata, and GC survivor spaces consume the remaining ~14 MB.

---

### 3. String Immutability (`+=`) vs. `StringBuilder` Performance

Benchmark appending 25,000 character tokens in a tight loop:

| Concatenation Approach | Time Complexity | Elapsed Time | GC Young Gen Collections | Performance Verdict |
|---|---|---|---|---|
| **`String +=`** | **$O(n^2)$** | **~77 ms** | **7 GC pauses** (~73 MB allocated) | ❌ Slow. Allocates a new String object and copies the buffer on every iteration. |
| **`StringBuilder`** | **$O(n)$ amortized** | **~5 ms** | **0 GC pauses** | 🏆 **~15.4x Faster**. Appends in-place to a dynamic internal byte buffer. |

---

### 4. Anatomy of `-Xlog:gc` Log Lines

Sample line produced by modern JVM unified logging (`-Xlog:gc`):

```text
[0.113s][info][gc] GC(0) Pause Young (Concurrent Start) (G1 Humongous Allocation) 2M->1M(10M) 3.959ms
```

| Log Component | Sample Value | Meaning / Technical Explanation |
|---|---|---|
| **Uptime** | `[0.113s]` | Elapsed time in seconds since the JVM process started. |
| **Log Level & Tag** | `[info][gc]` | Unified logging level (`info`) and subsystem tag (`gc`). |
| **GC Counter** | `GC(0)` | Sequential index of the garbage collection event. |
| **Pause Type** | `Pause Young` | Stop-The-World (STW) pause type (Young, Mixed, or Full). |
| **GC Cause** | `G1 Humongous Allocation` | Triggered because single allocation exceeded 50% of G1 region size. |
| **Memory Reclamation** | `2M->1M(10M)` | Heap used **before GC** (2M) $\rightarrow$ **after GC** (1M) out of **total committed heap** (10M). |
| **Pause Duration** | `3.959ms` | Exact Stop-The-World application thread pause duration. |

---

### 5. Integer Cache Equality (`==` vs `.equals()`)

Demonstrating Java's Autoboxing Flyweight Cache (`IntegerCache` range `[-128, 127]`):

| Expression Tested | `==` (Reference Equality) | `.equals()` (Value Equality) | Same Memory Reference? | Explanation |
|---|---|---|---|---|
| `Integer a=100, b=100` | **`true`** | **`true`** | ✅ Yes (`identityHashCode` identical) | Stored in `IntegerCache` flyweight pool. |
| `Integer c=128, d=128` | **`false`** ⚠️ | **`true`** | ❌ No (different heap addresses) | **Pitfall**: Values $> 127$ instantiate new distinct objects! |
| `Integer e=-128, f=-128` | **`true`** | **`true`** | ✅ Yes | Lower cache boundary included. |
| `Integer g=-129, h=-129` | **`false`** ⚠️ | **`true`** | ❌ No | Lower cache boundary exceeded. |

> **Best Practice Rule**: **NEVER** use `==` to compare object wrapper types (`Integer`, `Long`, `Short`). Always use `.equals()` or primitive types (`int`).

---

## 🛠️ Step-by-Step CLI Execution Commands

From `DailyTask/Day10`:

### 1. Compile All Classes
```powershell
javac -d target/classes src/main/java/com/hcl/memory/*.java
```

### 2. Test Stack Depth with Different `-Xss` Flags
```powershell
# 256 KB Stack
java -Xss256k -cp target/classes com.hcl.memory.StackDepthExperiment

# 512 KB Stack
java -Xss512k -cp target/classes com.hcl.memory.StackDepthExperiment

# 1 MB Stack
java -Xss1m -cp target/classes com.hcl.memory.StackDepthExperiment
```

### 3. Test OutOfMemoryError with `-Xmx64m`
```powershell
java -Xmx64m -cp target/classes com.hcl.memory.HeapOOMExperiment
```

### 4. Test String vs StringBuilder Benchmark
```powershell
java -cp target/classes com.hcl.memory.StringConcatenationBenchmark
```

### 5. Test Integer Cache Pitfall
```powershell
java -cp target/classes com.hcl.memory.IntegerCachePitfall
```

### 6. Run Complete Suite with Live GC Logging (`-Xlog:gc`)
```powershell
java -Xlog:gc -cp target/classes com.hcl.memory.MemoryLabApp
```

### 7. Run with Maven
```powershell
mvn clean compile exec:java
```

---

## 🚀 Push Day 10 to GitHub

From the repository root (`d:\HCLTech`):

```powershell
cd d:\HCLTech

# Stage Day 10 files
git add DailyTask/Day10

# Commit changes
git commit -m "feat: complete Day 10 JVM memory lab, GC logging, and benchmark tables"

# Push to GitHub
git push origin main
```
