# laboratory

1.Yang file verification

By calling an external command using apache commons-exec

2.Node verification

By calling an external command using apache commons-exec

3.Parse xml by SAX parser







Notes:

Copy files:

On Windows10:

mvn clean

mvn install

pscp -C javayang-1.0-SNAPSHOT-jar-with-dependencies.jar root@192.168.1.43:/usr/shunyi/yang/ericsson-bsf



On CentOS:

cd /usr/shunyi/yang/ericsson-bsf
java -jar javayang-1.0-SNAPSHOT-jar-with-dependencies.jar pyang ericsson-bsf.yang