@echo off
rem Shows timing of counting valid configurations using traditional algorithm

set CLASSPATH=%CLASSPATH%;./bin;./lib/*

java ieee.access.TimingCountWithWithoutRemovingFeatures2 %*
