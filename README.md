# gradle-template
I'm not sure how to configure input templates and generated output files. Maybe I should use the CopySpec type.

```kotlin
template {
  create("taskNamePart") {
    // mandatory  
    // 'from': File | Directory  
    from file("template1")
    // optional  
    // 'into': File | Directory
    // 'into' is File only when 'from' is a File  
    into dir("outputDir")
    // optional  
    // parameters is map: String -> (String| String[])
    parameters(p1: "v1", p2: 2, p3: ["v3","v4","v5"])
  }
}
```
