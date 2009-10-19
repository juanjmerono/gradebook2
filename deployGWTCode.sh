#!/usr/bin/sh

# If we have both the unpacked and the packed version then check to see whose newer
if [ -d "gradebook" ] && [ -f "gradebook.tar.gz" ]; then 

	dirLastModified=`stat --format=%Y gradebook`
	tarballLastModified=`stat --format=%Y gradebook.tar.gz`

	# When the tarball is newer than the directory, we should overwrite the directory
	if [ $dirLastModified -lt $tarballLastModified ]; then
		echo "Overwriting compiled GWT code"
		rm -rf gradebook
		tar -xzf gradebook.tar.gz
	else
		echo "Recreating tarball"
		rm gradebook.tar.gz
		tar -czf gradebook.tar.gz gradebook
	fi
# Otherwise, if we just have the packed version, unpack it
elif [ -f "gradebook.tar.gz" ]
then
	echo "Unpacking compiled GWT code"
	tar -xzf gradebook.tar.gz	
fi
