#!/bin/bash
scriptDir=$(dirname "$0")
rm -rf "$scriptDir/html/"*
cp "$scriptDir/../04_angular_tour_of_heroes/angular-tour-of-heroes/dist/angular-tour-of-heroes/"* "$scriptDir/html/"
