#!/bin/bash
# Executes tests for the comment to the BPNCC

export CLASSPATH=$CLASSPATH:./bin:./lib/*

echo "== Deleting output"
rm -f output/*.*

echo "== Comparing alternatives using long"

echo "IoT fm 1"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures models/example-IoT-fm-1.xml 10 >> output/data-fm-1.csv 2> output/messages-fm-1.txt
done 

echo "IoT fm 2"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures models/example-IoT-fm-2.xml 10 >> output/data-fm-2.csv 2> output/messages-fm-2.txt
done 


echo ""
echo "== Comparing alternatives using BigInteger"

echo "IoT fm 1"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/example-IoT-fm-1.xml 10 >> output/data-fm-1-bi.csv 2> output/messages-fm-1-bi.txt
done 

echo "IoT fm 2"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/example-IoT-fm-2.xml 10 >> output/data-fm-2-bi.csv 2> output/messages-fm-2-bi.txt
done

echo "android 510"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-android-510.xml 10 >> output/data-android-510.csv 2> output/messages-android-510.txt
done

echo "android 60"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-android-60.xml 10 >> output/data-android-60.csv 2> output/messages-android-60.txt
done

echo "ubuntu 1204"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-ubuntu-1204.xml 10 >> output/data-ubuntu-1204.csv 2> output/messages-ubuntu-1204.txt
done

echo "ubuntu 1410"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-ubuntu-1410.xml 10 >> output/data-ubuntu-1410.csv 2> output/messages-ubuntu-1410.txt
done

echo "windows 70"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-windows-70.xml 10 >> output/data-windows-70.csv 2> output/messages-windows-70.txt
done

echo "windows 80"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/splot-windows-80.xml 10 >> output/data-windows-80.csv 2> output/messages-windows-80.txt
done 

echo "Linux 2.6.33.3"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/linux-2.6.33.3-model.xml 10 >> output/data-linux.csv 2> output/messages-linux.txt
done

echo "BusyBox 1.18.0"
for ((i=1;i<=10;i++)); 
do 
	java ieee.access.TimingCountWithWithoutRemovingFeatures2 models/busybox-1.18.0-model.xml 10 >> output/data-busybox.csv 2> output/messages-busybox.txt
done 