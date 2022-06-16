@echo off
echo Fetching ffmpeg executables...
cd %~dp0
curl -Lo src\main\resources\videobox\ffmpeg.exe https://fs.duifene.com/res/r2/u3770156/ffmpeg_ed6dc1e7b9911f05418a.exe
curl -Lo src\main\resources\videobox\ffprobe.exe https://fs.duifene.com/res/r2/u3770156/ffprobe_1005164a9b129efc2d31.exe
