#!/bin/bash

# Demo: Delete All Git Tags Script
# This script demonstrates the tag deletion process without actually executing remote deletions

echo "🗑️  Delete All Git Tags - DEMO MODE"
echo "====================================="

# Get all tag names
echo "📋 Getting list of all tags..."
TAGS=$(git tag -l)

if [ -z "$TAGS" ]; then
    echo "✅ No tags found in the repository"
    exit 0
fi

echo "🔍 Found the following tags:"
echo "$TAGS"
echo ""

# Count total tags
TAG_COUNT=$(echo "$TAGS" | wc -l)
echo "📊 Total tags to delete: $TAG_COUNT"
echo ""

echo "🚀 DEMO: Commands that would be executed:"
echo "=========================================="
echo ""

# Show local tag deletion commands
echo "💻 LOCAL TAG DELETION COMMANDS:"
echo "git tag -l | xargs git tag -d"
echo ""
echo "Or individually:"
for tag in $TAGS; do
    echo "git tag -d $tag"
done

echo ""
echo "🌐 REMOTE TAG DELETION COMMANDS:"
echo "git tag -l | xargs -I {} git push origin :refs/tags/{}"
echo ""
echo "Or individually:"
for tag in $TAGS; do
    echo "git push origin :refs/tags/$tag"
done

echo ""
echo "🔍 VERIFICATION COMMANDS:"
echo "git tag -l                    # Should be empty after deletion"
echo "git ls-remote --tags origin   # Should be empty after deletion"
echo ""

echo "⚠️  NOTE: This is a demo. To actually delete tags, use the 'delete_all_tags.sh' script"
echo "or run the commands manually with proper confirmation."