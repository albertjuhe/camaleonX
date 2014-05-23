camaleonX
=========

This is a software that merge Microsoft Office Documents 2003, Word and PowerPoint ( docx and pptx) in one file.
Currently iss in Beta state.

##Development

This application has been developed in Java and XSLT 2.0 using Saxon.
Some merge operations has been developed in Java, files movements between two documents, file existence, files rename, etc.. the Other operations of merge has been developed in XSLT because docx format and pptx format are XML formats.

##Installation

Java 1.6 
You have to copy the content in a folder.
Configure the camaleonx.properties with the correct path.
base: Is the path base where the camaleonx search the xsl files
results: Is the path where the application save the merged files

##Usage

Execute the camaleon.bat files.

java -jar camaleonx.jar -U camaleonx.properties docx_merge_sample.xml -Xms1024m -Xmx1024m -Dlog4j.configuration=file:/./logs/camaleonx.log4j.xml -Dlog4j.debug=true -Dcamaleonx.local./home=logs  

-U: Config file
docx_merge_sample.xml The main file where are the files to merge.
-Dlog4j.configuration Log files

The files that you want to merge are stored in a XML file:
Example in doc_merge_sample.xml

```xml
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
```

Inside the XML file you define the documents to merge and the type of the documents.
The documents to merge are in the contents tag and the type of the document are in the meta with key type.
For each document that you want to merge you have to create a new tag content with the attribute href, this attribute is the document location.

