
#!/usr/bin/env sh

# $1 = path to input jar
# $2 = path to output binary
# Example usage: ./executable.sh target/project-1.0-SNAPSHOT.jar project

# This script has been customised for this particular project with jdk features enabled.

INPUT_JAR="$1"
OUTPUT_BIN="$2"

# Prepend launcher to the jar
(cat <<'SH'
#!/usr/bin/env sh
exec java --enable-preview --add-modules=jdk.incubator.vector -Djdk.incubator.vector.VECTOR_ACCESS_OOB_CHECK=0 -jar "$0" "$@"
exit 0
SH
cat "$INPUT_JAR") > "$OUTPUT_BIN"

# Make executable
chmod +x "$OUTPUT_BIN"