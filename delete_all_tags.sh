#!/bin/bash

# Delete All Git Tags Script
# This script deletes all local and remote Git tags from the repository

echo "🗑️  Delete All Git Tags Script"
echo "==============================="

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

# Ask for confirmation
read -p "⚠️  Are you sure you want to delete ALL tags? This action cannot be undone! (y/N): " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "❌ Operation cancelled by user"
    exit 1
fi

echo ""
echo "🚀 Starting tag deletion process..."
echo ""

# Delete all local tags
echo "🏠 Deleting local tags..."
for tag in $TAGS; do
    echo "  - Deleting local tag: $tag"
    git tag -d "$tag"
done

echo ""
echo "🌐 Deleting remote tags..."

# Delete all remote tags
for tag in $TAGS; do
    echo "  - Deleting remote tag: $tag"
    git push origin ":refs/tags/$tag"
done

echo ""
echo "✅ All tags have been deleted successfully!"
echo ""

# Verify deletion
echo "🔍 Verifying tag deletion..."
REMAINING_LOCAL=$(git tag -l)
REMAINING_REMOTE=$(git ls-remote --tags origin)

if [ -z "$REMAINING_LOCAL" ]; then
    echo "✅ Local tags: All deleted"
else
    echo "⚠️  Local tags remaining: $REMAINING_LOCAL"
fi

if [ -z "$REMAINING_REMOTE" ]; then
    echo "✅ Remote tags: All deleted"
else
    echo "⚠️  Remote tags remaining:"
    echo "$REMAINING_REMOTE"
fi

echo ""
echo "🎉 Tag deletion process completed!"