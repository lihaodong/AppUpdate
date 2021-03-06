# UpdateApp #

安卓版本更新，适配6.0及7.0，是为公司开发的，如果需要集成，获取更新接口必须为如下格式。目前还不能自定义界面，期待下次更新！

----
## 截图
### 非强制更新
![](https://github.com/lihaodong/AppUpdate/blob/master/images/image0.gif)
### 强制更新
![](https://github.com/lihaodong/AppUpdate/blob/master/images/image1.gif)
## 使用方法
### Gradle
```groovy
compile 'com.lihaodong.appupdate:updatelibrary:1.0.0.3'
```
### Maven
```groovy
<dependency>
  <groupId>com.lihaodong.appupdate</groupId>
  <artifactId>updatelibrary</artifactId>
  <version>1.0.0.3</version>
  <type>pom</type>
</dependency>
```
### Eclipse ADT

放弃治疗。

### Android Studio

## 调用

```
APPUpdateAgent.forceUpdate(this,"获取版本号接口",new ExitInterface(){
            @Override
            public void exitApp() {
                //可以自定义关闭事件
                App.finishAllActivity();
            }
        });
接口返回格式必须为如下格式：因为是为公司开发，所以格式比较统一
{"ForcedUpdate":0,"Version":"3.3","UpdateMsg":"更新内容","Url":"http://clouddn.com/app/update.apk"}
```
## 需要的权限
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```
## 适配Android6.0

关于6.0适配，请自行在调用API时申请WRITE_EXTERNAL_STORAGE权限，可以参加demo中的代码
## 友好的调试模式

```
java
```
## License
```text
Copyright 2017 Li Haodong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


