# 视频工具箱

软件开发综合实训1项目

底层核心库：ffmpeg

UI库：JavaFx，绘制 UI 可采用 SceneBuilder

## 功能

### 转码

核心功能：用户选择要转成的格式，并提供文件，程序调用 ffmpeg 进行转换

### 合并

核心功能：将分段的视频切片进行合并，通过 ffmpeg 的 concat 实现

### 画面音轨分合

核心功能：

- 将已有音频和画面的视频进行音画分离
  
- 将纯视频画面的视频和音频轨道进行合成

## 构建

```
fetch-ffmpeg.bat
gradlew.bat
```

## 运行

```
gradlew.bat run
```