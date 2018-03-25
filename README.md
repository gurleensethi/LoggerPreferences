[![](https://jitpack.io/v/gurleensethi/LoggerPreferences.svg)](https://jitpack.io/#gurleensethi/LoggerPreferences)
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

### Getting a Reference
There are two ways you can obtain a reference.
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
If you don't call `with()` then by default `LoggerPreferences` will logged as the file name, so make sure to call `with()`.

### API
LoggerPreferences is a wrapper around `SharedPreferences`. It has all the functions provided by `SharedPreferences` and works in exactly the same manner.

So if in the `MainActivity` you change a `String` value.
```java
preferences.edit()
        .putString("key_string", "new value")
        .apply();
```
In `logcat` you will get:
```
D/LoggerPreferences: [String] MainActivity changed the value of Key[key_string] from [old value] to [new value].
```
If you are retrieving a `String` value:
```java
String value = preferences.getString("key_string", null);
```
In `logcat` you will get:
```
D/LoggerPreferences: [String] MainActivity is retrieving the value of Key[key_string] which is set to [new value].
```

LoggerPreferences has all the functions provided by `SharedPreferences` and `SharedPreferences.Editor`.
```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoggerPreferences.init(true, true);

        LoggerPreferences preferences = LoggerPreferences.get(this, "sp_file", Context.MODE_PRIVATE)
                .with(this);

        HashSet<String> set = new HashSet<>();
        set.add("New Value");
        set.add("Oh!");
        set.add("I am in a set");

        preferences.edit()
                .putInt("key_int", 123)
                .putString("key_string", "new value")
                .putBoolean("key_boolean", false)
                .putFloat("key_float", 10.0f)
                .putLong("key_long", 112320L)
                .putStringSet("key_set", set)
                .apply();

        preferences.getString("key_string", null);
        preferences.getInt("key_int", 0);
        preferences.getBoolean("key_boolean", false);
        preferences.getLong("key_long", 1000L);
        preferences.getFloat("key_float", 10f);
        preferences.getStringSet("key_set", null);
        preferences.getAll();
        preferences.registerOnSharedPreferenceChangeListener(null);
        preferences.unregisterOnSharedPreferenceChangeListener(null);
    }
}
```
Logcat:
```
D/LoggerPreferences: [Int] MainActivity changed the value of Key[key_int] from [123] to [123].
D/LoggerPreferences: [String] MainActivity changed the value of Key[key_string] from [new value] to [new value].
D/LoggerPreferences: [Boolean] MainActivity changed the value of Key[key_boolean] from [true] to [false].
D/LoggerPreferences: [Float] MainActivity changed the value of Key[key_float] from [1.0] to [10.0].
D/LoggerPreferences: [Long] MainActivity changed the value of Key[key_long] from [10000] to [112320].
D/LoggerPreferences: [Set<String>] MainActivity changed the value of Key[key_set] from [[Test, Testing!]] to [I am in a set,Oh!,New Value].

D/LoggerPreferences: [String] MainActivity is retrieving the value of Key[key_string] which is set to [new value].
D/LoggerPreferences: [Int] MainActivity is retrieving the value of Key[key_int] which is set to [123].
D/LoggerPreferences: [Boolean] MainActivity is retrieving the value of Key[key_boolean] which is set to [false].
D/LoggerPreferences: [Long] MainActivity is retrieving the value of Key[key_long] which is set to [112320].
D/LoggerPreferences: [Float] MainActivity is retrieving the value of Key[key_float] which is set to [10.0].
D/LoggerPreferences: [Set<String>] MainActivity is retrieving the value of Key[key_set] which is set to [I am in a set,Oh!,New Value].
D/LoggerPreferences: MainActivity is retrieving all values.
D/LoggerPreferences: MainActivity has registered a OnSharedPreferenceChangeListener.
D/LoggerPreferences: MainActivity has unregistered a OnSharedPreferenceChangeListener.
```

### Tips
LoggerPreferences uses the same tag when logging: `LoggerPreferences`. In your logcat search `LoggerPreferences` to get all the logs.

<img src="https://raw.githubusercontent.com/gurleensethi/LoggerPreferences/master/images/logger_tag.png" width="350"/>

#### Never log in production!

## Support
If you have any idea or need a change in the library or found a bug, please [open an issue](https://github.com/gurleensethi/LoggerPreferences/issues/new).

## License
```txt
Copyright 2018 Gurleen Sethi

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
