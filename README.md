This is a sample of how to write and build extensions for [SGDK](https://github.com/Stephane-D/SGDK)'s rescomp tool with Maven. For more information see [my blog post](https://www.radicaledward101.com/blag/2025/04/11/rescomp-extension-sgdk.html).

This is not a complete SGDK project, but is instead intended to act as an example of the minimal file changes needed to add a rescomp_ext.jar to your own project.

## Prerequisities
[Java](https://www.java.com/en/download/), [Maven](https://maven.apache.org/index.html), [SGDK](https://github.com/Stephane-D/SGDK) 2.10 or above[^1], and an SGDK project you're working on.

## Build
```
mvn install:install-file -Dfile=%GDK%/bin/rescomp.jar -DgroupId=sgdk -DartifactId=rescomp -Dversion=3.95 -Dpackaging=jar -DgeneratePom=true

cd res/rescomp_ext

mvn clean install

cd ../..

java -jar %GDK%/bin/rescomp.jar res/resources.res res/resources.s
```
where %GDK% is the path to your SGDK install.

[^1]: Older versions of rescomp had [an issue loading some classes in the extension jar](https://github.com/Stephane-D/SGDK/pull/405).