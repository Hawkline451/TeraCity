#!/bin/bash

name="Stone"
first=0
second=0
limit=16
extension=".block"
separador="_"
while [ $first -lt $limit ]
do
	second=0
	while [ $second -lt $limit ]
	do
		printf '{\n"basedOn" : "core:rock"\n}' > $name$first$separador$second$extension
		second=$(( $second + 1 ))
	done
	first=$(( $first + 1 ))
done
