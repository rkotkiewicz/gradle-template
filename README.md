# gradle-template

```kotlin
template {
  create("taskNamePart") {
    // mandatory  
    // 'from': File | Directory  
    from file("template1")
    // optional  
    // 'into': Directory
    into dir("outputDir")
    // optional  
    // parameters is map: String -> Any
    parameters(p1: "v1", p2: 2, p3: ["v3","v4","v5"])
  }
}
```
