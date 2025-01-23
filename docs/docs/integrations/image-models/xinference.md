---
sidebar_position: 6
---

# Xinference

- https://inference.readthedocs.io/


## Maven Dependency

`1.0.0-alpha1` and later:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-community-xinference</artifactId>
    <version>1.0.0-alpha1</version>
</dependency>
```

Or, you can use BOM to manage dependencies consistently:

```xml
<dependencyManagement>
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-community-bom</artifactId>
        <version>1.0.0-alpha1</version>
        <typ>pom</typ>
        <scope>import</scope>
    </dependency>
</dependencyManagement>
```

## APIs

- `XinferenceImageModel`


## Examples

- [XinferenceImageModelIT](https://github.com/langchain4j/langchain4j-community/blob/main/models/langchain4j-community-xinference/src/test/java/dev/langchain4j/community/model/xinference/XinferenceImageModelIT.java)