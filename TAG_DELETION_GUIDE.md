# Delete All Tags - Implementation Guide

This document provides instructions for deleting all Git tags from the Tasky repository.

## Current Situation

The repository currently contains **24 Git tags** from v1.5 to v3.0.1:

```
v1.5, v1.6, v1.7, v1.8, v1.9, v2.0, v2.1, v2.2, v2.3, v2.3.1, v2.3.2, 
v2.3.3, v2.3.4, v2.3.5, v2.3.6, v2.3.7, v2.3.8, v2.3.9, v2.4.0, v2.4.1, 
v2.4.2, v3.0.0, v3.0.1
```

## Automated Solution

### Using the Provided Script

A comprehensive script `delete_all_tags.sh` has been created that will:

1. ✅ List all existing tags
2. ✅ Ask for user confirmation
3. ✅ Delete all local tags
4. ✅ Delete all remote tags
5. ✅ Verify the deletion was successful

**To use the script:**

```bash
./delete_all_tags.sh
```

The script includes safety measures:
- Shows all tags before deletion
- Requires user confirmation
- Provides progress feedback
- Verifies successful deletion

## Manual Alternative

If you prefer to delete tags manually, use these commands:

### 1. List all tags first (verification)
```bash
git tag -l
git ls-remote --tags origin
```

### 2. Delete all local tags
```bash
git tag -l | xargs git tag -d
```

### 3. Delete all remote tags
```bash
git tag -l | xargs -I {} git push origin :refs/tags/{}
```

### 4. Verify deletion
```bash
git tag -l                    # Should be empty
git ls-remote --tags origin   # Should be empty
```

## Individual Tag Deletion (if needed)

To delete specific tags individually:

```bash
# Delete local tag
git tag -d v1.5

# Delete remote tag
git push origin :refs/tags/v1.5
```

## Important Notes

⚠️ **Warning**: Tag deletion is irreversible. Once tags are deleted from the remote repository, they cannot be recovered unless you have backups.

✅ **Safe**: This operation does not affect any code, commits, or branches. Only the tag references are removed.

📝 **Version Info**: The current app version is 3.0.2 (as defined in app/build.gradle), but no tag exists for this version yet.

## Verification Commands

After deletion, verify with:
```bash
# Check local tags (should be empty)
git tag -l

# Check remote tags (should be empty)  
git ls-remote --tags origin

# Check that commits are still intact
git log --oneline -10
```

## Rollback (if needed)

If you need to recreate tags later, you would need to:
1. Identify the commit SHA for each version
2. Create new tags pointing to those commits
3. Push the new tags to remote

Example:
```bash
git tag v3.0.1 <commit-sha>
git push origin v3.0.1
```