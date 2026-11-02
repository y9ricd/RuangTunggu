package com.ruangtunggu.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Official Figma Design System Tokens for RuangTunggu.
 * Exact hex values matched 1:1 with Figma specification:
 * - #0A8F82 (Brand Primary Teal)
 * - #0F766E (Brand Dark Teal)
 * - #14B8A6 (Brand Light Teal / Mint)
 * - #0F1E36 (Navy Text / Headings)
 * - #F8FAFC (Input Field & Screen Background)
 * - #E2E8F0 (Input Field Border & Progress Track)
 */
object RTColors {
    val BrandPrimaryTeal = Color(0xFF0A8F82)      // #0A8F82 - Warna identitas utama
    val BrandDarkTeal = Color(0xFF0F766E)         // #0F766E - Aksen gradien atas
    val BrandMidTeal = Color(0xFF0D9488)          // #0D9488 - Transisi gradien tengah
    val BrandLightTeal = Color(0xFF14B8A6)        // #14B8A6 - Aksen dinamis bawah
    val NavyHeading = Color(0xFF0F1E36)           // #0F1E36 - Teks kontras tinggi / Headings
    val InputBackground = Color(0xFFF8FAFC)       // #F8FAFC - Input Field Background
    val InputBorder = Color(0xFFE2E8F0)           // #E2E8F0 - Input Field Border 1dp solid

    val MintSurfaceActive = Color(0xFFD1F4EC)     // #D1F4EC - Indikator kapsul aktif & Layanan
    val PastelTealCircle = Color(0xFFE0F7F4)      // #E0F7F4 - Lingkaran Empty State & Layanan seragam
    val PastelSlateCircle = Color(0xFFF1F5F9)     // #F1F5F9 - Lingkaran sekunder
    val SlateBodyText = Color(0xFF64748B)         // #64748B - Keterangan / sub-teks
    val MutedSlate = Color(0xFF94A3B8)            // #94A3B8 - Placeholder & icon non-aktif
    val ScreenBackground = Color(0xFFF8FAFC)      // #F8FAFC
    val PureWhite = Color(0xFFFFFFFF)             // #FFFFFF - Floating Card Surface
    val AvatarTeal = Color(0xFF38B2A5)            // #38B2A5 - Avatar lingkaran "NP"

    val ActiveBadgeBg = Color(0xFFDCFCE7)         // #DCFCE7 - Badge "● Antrean aktif"
    val ActiveBadgeDot = Color(0xFF16A34A)        // #16A34A - Titik hijau status aktif
    val ActiveBadgeText = Color(0xFF15803D)       // #15803D - Teks hijau status aktif
    val WarningAmberBg = Color(0xFFFEF3C7)        // #FEF3C7 - Fase 2 (Dekat) & Fase 4 (Dijeda)
    val WarningAmberBorder = Color(0xFFF59E0B)    // #F59E0B - Border peringatan dekat/dijeda
    val WarningAmberText = Color(0xFFB45309)      // #B45309 - Teks peringatan dekat/dijeda
    val PausedBadgeBg = Color(0xFFFEF3C7)         // #FEF3C7 - Badge "Dijeda"
    val PausedAmber = Color(0xFFD97706)           // #D97706 - Status antrean dijeda
    val ErrorRed = Color(0xFFDC2626)              // #DC2626 - Error border & tombol batalkan
    val ErrorSurface = Color(0xFFFEF2F2)          // #FEF2F2 - Error banner & slot penuh surface
    val NotificationRedDot = Color(0xFFEF4444)    // #EF4444 - Red dot lonceng notifikasi
    val StarGold = Color(0xFFF59E0B)              // #F59E0B - Bintang penilaian layanan

    // Consistent Quick Action Pastel Surfaces (Section "Layanan" - 100% Uniform Brand Mint)
    val QuickActionUniformBg = Color(0xFFE0F7F4)  // #E0F7F4 - Seragam untuk Cari RS, Jadwal, Riwayat, Profil
    val QuickActionCariRsBg = Color(0xFFE0F7F4)
    val QuickActionJadwalBg = Color(0xFFE0F7F4)
    val QuickActionRiwayatBg = Color(0xFFE0F7F4)
    val QuickActionProfilBg = Color(0xFFE0F7F4)

    val CardSpotShadow = Color(0x1F000000)
    val CardAmbientShadow = Color(0x10000000)

    val HeaderWaveGradient = Brush.verticalGradient(
        colors = listOf(
            BrandDarkTeal,
            BrandMidTeal,
            BrandLightTeal
        )
    )
}
