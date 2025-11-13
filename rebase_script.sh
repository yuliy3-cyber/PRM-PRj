#!/bin/bash
# Script để tự động sửa commit messages
# Sử dụng git filter-branch hoặc git rebase

git filter-branch -f --msg-filter '
    case "$GIT_COMMIT" in
        91d01a2*)
            echo "feat: Add data models and common utility classes"
            ;;
        43da797*)
            echo "feat: Add RecyclerView adapters for UI components"
            ;;
        4c325da*)
            echo "feat: Add authentication and profile management activities"
            ;;
        de542a6*)
            echo "feat: Add booking and checkout flow activities"
            ;;
        47bdaf6*)
            echo "feat: Add movie, ticket, wallet, and admin management activities"
            ;;
        6085bbe*)
            echo "feat: Add fragments, view holders, and Firebase integration"
            ;;
        c02e2a8*)
            echo "feat: Add XML layout files for all screens and components"
            ;;
        97eb79a*)
            echo "feat: Add drawable resources and UI assets"
            ;;
        45d948b*)
            echo "feat: Add fonts, colors, animations, and app resources"
            ;;
        94a7983*)
            echo "chore: Update build configuration and project settings"
            ;;
        4038c1d*)
            echo "fix: Resolve merge conflicts using prm392_cinemabooking versions"
            ;;
        13c80b7*)
            echo "fix: Resolve all merge conflicts with prm392_cinemabooking versions"
            ;;
        *)
            cat
            ;;
    esac
' HEAD~12..HEAD

