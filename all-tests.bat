@echo off
rem Executes tests for the comment to the BPNCC

set CLASSPATH=%CLASSPATH%;./bin;./lib/*

echo == Deleting output
del /q output\*.*

rem echo == Comparing alternatives using long

echo IoT fm 1
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures models/example-IoT-fm-1.xml 10 >> output/data-fm-1.csv 2> output/messages-fm-1.txt
) 

echo IoT fm 2
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures models/example-IoT-fm-2.xml 10 >> output/data-fm-2.csv 2> output/messages-fm-2.txt
) 


echo .
echo == Comparing alternatives using BigInteger

echo IoT fm 1
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/example-IoT-fm-1.xml 10 >> output/data-fm-1-bi.csv 2> output/messages-fm-1-bi.txt
) 

echo IoT fm 2
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/example-IoT-fm-2.xml 10 >> output/data-fm-2-bi.csv 2> output/messages-fm-2-bi.txt
) 

echo android 510
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-android-510.xml 10 >> output/data-android-510.csv 2> output/messages-android-510.txt
) 

echo android 60
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-android-60.xml 10 >> output/data-android-60.csv 2> output/messages-android-60.txt
) 

echo ubuntu 1204
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-ubuntu-1204.xml 10 >> output/data-ubuntu-1204.csv 2> output/messages-ubuntu-1204.txt
) 

echo ubuntu 1410
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-ubuntu-1410.xml 10 >> output/data-ubuntu-1410.csv 2> output/messages-ubuntu-1410.txt
) 

echo windows 70
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-windows-70.xml 10 >> output/data-windows-70.csv 2> output/messages-windows-70.txt
) 

echo windows 80
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-windows-80.xml 10 >> output/data-windows-80.csv 2> output/messages-windows-80.txt
) 

echo Linux 2.6.33.3
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/linux-2.6.33.3-model.xml 10 >> output/data-linux.csv 2> output/messages-linux.txt
) 

echo BusyBox 1.18.0
for /L %%a in (1,1,10) do (
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/busybox-1.18.0-model.xml 10 >> output/data-busybox.csv 2> output/messages-busybox.txt
) 