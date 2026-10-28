#!/bin/sh
# Run the pure Java model tests without an Android device or external test libraries.
set -eu
PROJECT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
cd "$PROJECT_DIR"
TEST_OUTPUT_DIR=$(mktemp -d "${TMPDIR:-/tmp}/pixel-air-model-tests.XXXXXX")
trap 'rm -rf "$TEST_OUTPUT_DIR"' EXIT HUP INT TERM
JAVAC=javac
JAVA=java
if [ -n "${JAVA_HOME:-}" ]; then
    JAVAC="$JAVA_HOME/bin/javac"
    JAVA="$JAVA_HOME/bin/java"
fi
"$JAVAC" -d "$TEST_OUTPUT_DIR" \
    app/src/main/java/dev/pixelair/launcher/WorkspaceState.java \
    app/src/main/java/dev/pixelair/launcher/SwipePhysics.java \
    app/src/main/java/dev/pixelair/launcher/AppRanking.java \
    app/src/main/java/dev/pixelair/launcher/SpringMotion.java \
    app/src/main/java/dev/pixelair/launcher/IconPackMapping.java \
    app/src/main/java/dev/pixelair/launcher/AppCategories.java \
    app/src/main/java/dev/pixelair/launcher/WidgetGeometry.java \
    app/src/main/java/dev/pixelair/launcher/HomeReturnGate.java \
    app/src/main/java/dev/pixelair/launcher/UnlockGate.java \
    app/src/main/java/dev/pixelair/launcher/DrawerPages.java \
    app/src/main/java/dev/pixelair/launcher/DrawerSections.java \
    tests/*.java
for test in WorkspaceStateTest SwipePhysicsTest AppRankingTest SpringMotionTest IconPackMappingTest AppCategoriesTest WidgetGeometryTest UnlockGateTest DrawerSectionsTest; do
    "$JAVA" -ea -cp "$TEST_OUTPUT_DIR" "$test"
done

"$JAVA" -cp "$TEST_OUTPUT_DIR" DrawerPagesTest

"$JAVA" -ea -cp "$TEST_OUTPUT_DIR" HomeReturnGateTest
