# Script tự động sửa commit messages bằng git rebase
# Loại bỏ từ "copy" và số thứ tự "1/10, 2/10..."

$commitMessages = @{
    "91d01a2" = "feat: Add data models and common utility classes"
    "43da797" = "feat: Add RecyclerView adapters for UI components"
    "4c325da" = "feat: Add authentication and profile management activities"
    "de542a6" = "feat: Add booking and checkout flow activities"
    "47bdaf6" = "feat: Add movie, ticket, wallet, and admin management activities"
    "6085bbe" = "feat: Add fragments, view holders, and Firebase integration"
    "c02e2a8" = "feat: Add XML layout files for all screens and components"
    "97eb79a" = "feat: Add drawable resources and UI assets"
    "45d948b" = "feat: Add fonts, colors, animations, and app resources"
    "94a7983" = "chore: Update build configuration and project settings"
    "4038c1d" = "fix: Resolve merge conflicts using prm392_cinemabooking versions"
    "13c80b7" = "fix: Resolve all merge conflicts with prm392_cinemabooking versions"
}

Write-Host "Đang tạo file rebase script..."

$rebaseFile = "rebase_todo.txt"
$rebaseContent = @"
pick 91d01a2 feat: Add data models and common utility classes
pick 43da797 feat: Add RecyclerView adapters for UI components
pick 4c325da feat: Add authentication and profile management activities
pick de542a6 feat: Add booking and checkout flow activities
pick 47bdaf6 feat: Add movie, ticket, wallet, and admin management activities
pick 6085bbe feat: Add fragments, view holders, and Firebase integration
pick c02e2a8 feat: Add XML layout files for all screens and components
pick 97eb79a feat: Add drawable resources and UI assets
pick 45d948b feat: Add fonts, colors, animations, and app resources
pick 94a7983 chore: Update build configuration and project settings
pick 4038c1d fix: Resolve merge conflicts using prm392_cinemabooking versions
pick 13c80b7 fix: Resolve all merge conflicts with prm392_cinemabooking versions
"@

$rebaseContent | Out-File -FilePath $rebaseFile -Encoding UTF8

Write-Host "File rebase script đã được tạo: $rebaseFile"
Write-Host ""
Write-Host "Để sử dụng, chạy lệnh sau:"
Write-Host "  git rebase -i HEAD~12"
Write-Host ""
Write-Host "Hoặc sử dụng script tự động:"
Write-Host "  .\rebase_auto.ps1 -execute"

