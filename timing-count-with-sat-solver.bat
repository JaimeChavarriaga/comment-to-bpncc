@echo off
rem Shows timing of counting valid configurations using SAT solver

set CLASSPATH=%CLASSPATH%;./bin;./lib/*

java ieee.access.TimingCountWithSatSolver %*
