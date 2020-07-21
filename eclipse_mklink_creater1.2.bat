@echo off



cd src\main\resources
md assets
md data
md META-INF
cd ..\..\..\bin\main

del /Q assets
for /D %%1 in (assets) do rmdir /S /Q "%%1"
del /Q data
for /D %%1 in (data) do rmdir /S /Q "%%1"
del /Q META-INF
for /D %%1 in (META-INF) do rmdir /S /Q "%%1"
del /Q pack.mcmeta

mklink /d/j data ..\..\src\main\resources\data
mklink /d/j assets ..\..\src\main\resources\assets
mklink /d/j META-INF ..\..\src\main\resources\META-INF
mklink pack.mcmeta ..\..\src\main\resources\pack.mcmeta
