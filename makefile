.POSIX:
.PHONY: all clean install uninstall test

LOCAL="$$HOME"/.local

all:
	mvn compile exec:exec

install:
	mvn package
	install target/chevy-jar-with-dependencies.jar -D $(LOCAL)/opt/chevy.jar
	install src/main/resources/chevy.desktop -D $(LOCAL)/share/applications/chevy.desktop
	install src/main/resources/Chevy.png -D $(LOCAL)/share/icons/hicolor/256x256/apps/Chevy.png

uninstall:
	rm -f $(LOCAL)/opt/chevy.jar
	rm -f $(LOCAL)/share/applications/chevy.desktop
	rm -f $(LOCAL)/share/icons/hicolor/256x256/apps/Chevy.png

clean:
	mvn clean
