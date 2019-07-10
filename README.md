# jvm-tools
jvm-tools  

add this jar ,can get  jvm  Instrumentation  instance 
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
    <groupId>com.github.fireflyhoo</groupId>
    <artifactId>jvm-tools</artifactId>
    <version>Tag</version>
</dependency>
```

```
Instrumentation ins = JvmAngetTools.getInstance().getInstrumentation();
```

now you have one's full swing 
