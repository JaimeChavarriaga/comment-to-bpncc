@echo off
rem Executes tests for the comment to the BPNCC

set CLASSPATH=%CLASSPATH%;./bin;./lib/*

for /L %%a in (1,1,10) do (
	java ieee.access.Test4 models/example-IoT-fm-1.xml 10 >> output/data-fm-1.csv 2> output/messages-fm-1.txt
) 