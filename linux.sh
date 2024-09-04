#!/bin/sh

set -e

LOCAL="$HOME"/.local

if [ "$1" = uninstall ]
then
  set -x
  rm -f "$LOCAL"/opt/chevy.jar
  rm -f "$LOCAL"/share/applications/chevy.desktop
  rm -f "$LOCAL"/share/icons/hicolor/256x256/apps/Chevy.png
elif [ "$1" = install ]
then
  set -x
  mvn package
  install -D target/chevy.jar "$LOCAL"/opt/
  install -D src/main/resources/chevy.desktop "$LOCAL"/share/applications
  install -D src/main/resources/Chevy.png "$LOCAL"/share/icons/hicolor/256x256/apps/
else
  echo "Usage:"
  echo "  $0 install - to install the app locally for the current user"
  echo "  $0 uninstall - to remove all the local app files for the current user"
fi
