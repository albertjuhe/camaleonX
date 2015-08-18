#!/bin/sh
java -jar camaleonx.jar -U camaleonx.properties docx_merge_sample.xml -Xms10874880m -Xmx10874880m -Dlog4j.configuration=file:/./camaleonx.log4j.xml -Dlog4j.debuf=true -Dcamaleonx.local./home=logs
