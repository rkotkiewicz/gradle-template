# Gradle template plugin

This Gradle plugin creates tasks that fill templates. The plugin uses the
[SimpleTemplateEngine](https://docs.groovy-lang.org/latest/html/api/groovy/text/SimpleTemplateEngine.html).

## How To Use

### Applying the plugin

Edit build.gradle file to apply the plugin:

```groovy
plugins {
  id 'com.github.rkotkiewicz.template' version '0.1.0'
}
```
 
### Configuration

Configure the plugin by adding `template` section in `build.gradle`:

```groovy
template {    
  // use `create` to add task
  // you can create many tasks
  create("taskNamePrefix") {
      
    // 'from': File | Directory  
    // mandatory  
    from.set(file("$projectDir/templates"))
      
    // 'into': Directory
    // optional
    // default = "$buildDir/taskNamePrefix" 
    into.set(file("$buildDir/outputDir"))

    // binding is map: String -> Any
    // optional, but you want to set it
    binding([b0: "value0", b1: [1, 2, 42]])
  }
}
```

The configuration will create task with name: `taskNamePrefixFillTemplate`.

The `b0: "value0", b1: [1, 2, 42]` bindings will be applied to all template files from `"$projectDir/templates"`.  

Result will be saved into `"$buildDir/outputDir"`.

### Example Usage

#### template file
The template file is in:
`$projectDir/src/file.txt`

with content:

```text
pi = $pi
```

#### configuration

```groovy
template {
    
  create("pi") {
    from.set(file("$projectDir/src/file.txt"))
    binding([pi: 3.14159265359])
  }
}
```

#### filling template

To generate template run task:

```shell
./gradlew piFillTemplate
```

#### result

The result with content:
```text 
pi = 3.14159265359
```
will be saved into `"$buildDir/template/pi/file.txt"`.



