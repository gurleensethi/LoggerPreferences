# LoggerPreferences
Get to know which class changed the value in SharedPreferences.

## Installation
In the root level `build.gradle` file add:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Now add the dependency:
```gradle
dependencies {
    compile 'com.github.gurleensethi:LoggerPreferences:v1.0.0'
}
```
## Usage
### Initialize
LoggerPreferences will log any action that is taken with `SharedPreferences`, be it changing values or retrieving them.
To enable logging, you have to initalize LoggerPreferences.
```java
LoggerPreferences.init(true, true);
```
The first parameter tells LoggerPreferences to log everytime when a value is changed in `SharedPreferences` and the second parameter tells to log everytime a value is retrieved.
###### Tip: Never log in production!

### Getting a Reference
LoggerPreferences is a wrapper around `SharedPreferences`. It has all the functions provided by `SharedPreferences` and works in exactly the same manner. There are two ways you can obtain a reference.
First is by providing a `Context`, filename (`Stirng`) and mode (`int`).
```java
//LoggerPreferences.get(Context, fileName, mode);
LoggerPreferences preferences = LoggerPreferences.get(this, "sp_file", Context.MODE_PRIVATE)
        .with(this);
```
Second is by providing an object of `SharedPreferences` directly.
```java
//LoggerPreferences.get(SharedPreferences);
LoggerPreferences preferences = LoggerPreferences.get(getSharedPreferences("sp_file", Context.MODE_PRIVATE))
        .with(this);
```
You also have to call the function `with(Object)` and pass in the object which is responsible for interacting(changing/retrieving) with `SharedPreferences`. `with` takes in a parameter of type `Object`.
