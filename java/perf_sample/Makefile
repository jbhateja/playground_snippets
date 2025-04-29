all:
	jextract --output . natives.h
	gcc -fPIC -shared natives.c -o libnatives.so
	javac natives_h.java
	javac test.java
   
run:
	java -cp . test

profile:
	jps | grep test | awk '{print $1}'
	cat .pid
	asprof -d 60 -f profile.html 

clean:
	rm -rf *.class
	rm -rf *.so
