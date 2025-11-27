
#!/usr/bin/env sh

# $1 = path to input jar
# $2 = path to output binary
# Example usage: ./executable.sh target/mp3tag-1.0-SNAPSHOT.jar mp3tag

INPUT_JAR="$1"
OUTPUT_BIN="$2"

# Prepend launcher to the jar
(cat <<'SH'
#!/usr/bin/env sh
exec java -jar "$0" "$@"
exit 0
SH
cat "$INPUT_JAR") > "$OUTPUT_BIN"

# Make executable
chmod +x "$OUTPUT_BIN"