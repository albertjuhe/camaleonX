camaleonX
=========

This is a software that merge office documents ( docx and pptx) in one file.
Is in Beta state.

##Installation

You have to copy the content in a folder.
Configure the camaleonx.properties with the correct path.
base: Is the path base
results: Is the path where the application save the merged files

##Usage

Execute the camaleon.bat files.

java -jar camaleonx.jar -U camaleonx.properties docx_merge_sample.xml -Xms256m -Xmx1024m -Dlog4j.configuration=file:/./logs/camaleonx.log4j.xml -Dlog4j.debug=true -Dcamaleonx.local./home=logs  

-U: Config file
docx_merge_sample.xml The main file where are the files to merge.
-Dlog4j.configuration Log files

doc_merge_sample.xml its a file that contains what marge and in which format merge.

<?xml version="1.0" encoding="UTF-8" ?>
<profile>
    <metas>
        <meta key="project_name" value="result_file_name"/>
        <meta key="type" value="docx"/> <!-- docx, pptx -->
    </metas>
    <contents>
        <content include="1" href="C:\camaleonx\documents\1.docx"/>
        <content  include="1" href="C:\camaleonx\documents\2.docx"/>
    </contents>
</profile>


