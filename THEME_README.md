# Matte Black / Silver Neon Theme

A sleek, privacy-focused Android theme featuring matte black backgrounds with subtle metallic gradients, silver typography, and electric cyan/teal accents.

**Concept:** *Stealthy dashboard meets minimal sci-fi console* — matte black background, faint brushed-metal radial glow, silver typography, and electric blue/teal accents for buttons or highlights. No loud gradients, just texture + light play.

---

## 📁 File Structure

```
res/
├── values/
│   └── colors.xml
└── drawable/
    ├── bg_matte_silver.xml       (main background)
    ├── btn_silver_glow.xml       (primary button style)
    ├── edittext_dark.xml         (text input field)
    └── checkbox_dark.xml         (checkbox style - optional)
```

---

## 🎨 Color Palette

### Base Colors (`res/values/colors.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Background -->
    <color name="bg_matte_black">#0A0A0A</color>     <!-- true black with slight gray -->
    
    <!-- Text Colors -->
    <color name="silver_text">#D0D0D0</color>        <!-- polished silver (primary text) -->
    <color name="silver_dim">#8A8A8A</color>         <!-- dimmed silver (secondary text) -->
    
    <!-- Accent Colors -->
    <color name="accent_cyan">#00E5FF</color>        <!-- neon cyan (highlights, borders) -->
    <color name="accent_teal">#00C2A8</color>         <!-- teal fallback -->
    <color name="accent_glow">#1A00E5FF</color>       <!-- soft glow for gradients -->
    
    <!-- Action Colors (optional) -->
    <color name="btn_green">#00C853</color>           <!-- success states -->
    <color name="btn_blue">#2196F3</color>            <!-- share/action -->
    <color name="btn_orange">#FF9800</color>          <!-- warning/error -->
</resources>
```

### Color Usage Guidelines

| Element | Color | Usage |
|---------|-------|-------|
| **Background** | `bg_matte_black` (#0A0A0A) | Main app background |
| **Primary Text** | `silver_text` (#D0D0D0) | Headings, important text, button labels |
| **Secondary Text** | `silver_dim` (#8A8A8A) | Labels, hints, descriptions |
| **Accents** | `accent_cyan` (#00E5FF) | Button borders (pressed), highlights, checkboxes |
| **Glow Effects** | `accent_glow` (#1A00E5FF) | Subtle overlays, gradients |

---

## 🖼️ Drawables

### 1. Background: `bg_matte_silver.xml`

Main background with subtle metallic gradients.

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Base matte black -->
    <item>
        <shape>
            <solid android:color="@color/bg_matte_black" />
        </shape>
    </item>

    <!-- Subtle radial metallic sheen -->
    <item>
        <shape>
            <gradient
                android:type="radial"
                android:centerX="0.8"
                android:centerY="0.2"
                android:gradientRadius="900dp"
                android:startColor="#222222"
                android:endColor="@android:color/transparent" />
        </shape>
    </item>

    <!-- Gentle vertical reflection line (like brushed steel) -->
    <item>
        <shape>
            <gradient
                android:type="linear"
                android:angle="270"
                android:startColor="#15FFFFFF"
                android:centerColor="#05FFFFFF"
                android:endColor="@android:color/transparent" />
        </shape>
    </item>

</layer-list>
```

**Usage:**
```xml
<LinearLayout
    android:background="@drawable/bg_matte_silver"
    ... />
```

---

### 2. Button: `btn_silver_glow.xml`

Primary button style with dark gradient and cyan border on press.

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true">
        <shape android:shape="rectangle">
            <gradient
                android:type="linear"
                android:angle="90"
                android:startColor="#1F1F1F"
                android:endColor="#141414" />
            <corners android:radius="10dp" />
            <stroke android:width="1.5dp" android:color="@color/accent_cyan" />
        </shape>
    </item>
    <item>
        <shape android:shape="rectangle">
            <gradient
                android:type="linear"
                android:angle="90"
                android:startColor="#141414"
                android:endColor="#1F1F1F" />
            <corners android:radius="10dp" />
            <stroke android:width="1dp" android:color="#3A3A3A" />
        </shape>
    </item>
</selector>
```

**Usage:**
```xml
<Button
    android:background="@drawable/btn_silver_glow"
    android:textColor="@color/silver_text"
    android:textStyle="bold"
    android:minHeight="48dp"
    android:padding="12dp"
    android:textSize="16sp"
    ... />
```

---

### 3. Text Input: `edittext_dark.xml`

Dark input field style.

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#151515" />
    <corners android:radius="8dp" />
    <stroke android:width="1dp" android:color="#2A2A2A" />
</shape>
```

**Usage:**
```xml
<EditText
    android:background="@drawable/edittext_dark"
    android:textColor="@color/silver_text"
    android:textColorHint="@color/silver_dim"
    android:padding="12dp"
    ... />
```

---

### 4. Checkbox: `checkbox_dark.xml` (Optional)

Dark checkbox style with cyan accent when checked.

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_checked="true">
        <shape android:shape="rectangle">
            <solid android:color="@color/accent_cyan" />
            <corners android:radius="4dp" />
            <stroke android:width="1dp" android:color="@color/accent_teal" />
        </shape>
    </item>
    <item>
        <shape android:shape="rectangle">
            <solid android:color="#151515" />
            <corners android:radius="4dp" />
            <stroke android:width="1dp" android:color="#2A2A2A" />
        </shape>
    </item>
</selector>
```

**Usage:**
```xml
<CheckBox
    android:buttonTint="@color/accent_cyan"
    android:textColor="@color/silver_dim"
    ... />
```

---

## 📝 Typography Guidelines

### Text Sizes

| Element | Size | Color | Style |
|---------|------|-------|-------|
| **App Title / Heading** | 28sp | `silver_text` | Bold |
| **Section Labels** | 16sp | `silver_dim` | Normal |
| **Body Text** | 15sp | `silver_text` | Normal |
| **Button Text** | 16sp | `silver_text` | Bold |
| **Secondary Text** | 14sp | `silver_dim` | Normal |
| **Small Text / Footer** | 12sp | `silver_dim` | Normal |

### Line Spacing

For multi-line text blocks, use:
```xml
android:lineSpacingMultiplier="1.3"
```

---

## 🎯 Layout Examples

### Main Container

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:background="@drawable/bg_matte_silver">
    
    <!-- Your content here -->
    
</LinearLayout>
```

### Title Text

```xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Your App Title"
    android:textSize="28sp"
    android:textStyle="bold"
    android:textColor="@color/silver_text"
    android:gravity="center"
    android:layout_marginBottom="24dp" />
```

### Button

```xml
<Button
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:minHeight="48dp"
    android:text="Action Button"
    android:textColor="@color/silver_text"
    android:background="@drawable/btn_silver_glow"
    android:padding="12dp"
    android:textSize="16sp"
    android:textStyle="bold" />
```

### Text Input

```xml
<EditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/edittext_dark"
    android:padding="12dp"
    android:textSize="14sp"
    android:textColor="@color/silver_text"
    android:textColorHint="@color/silver_dim"
    android:hint="Enter text here..." />
```

---

## 🔧 AndroidManifest Theme

Update your `AndroidManifest.xml` to use a dark theme:

```xml
<application
    android:theme="@android:style/Theme.Material.Light"
    ... />
```

Or use Material Dark:

```xml
<application
    android:theme="@android:style/Theme.Material"
    ... />
```

---

## ✨ Design Principles

1. **Matte Black Base**: Deep black (#0A0A0A) provides the foundation
2. **Subtle Gradients**: Use radial and linear gradients sparingly for depth
3. **Silver Typography**: High contrast (#D0D0D0) for readability
4. **Cyan Accents**: Electric cyan (#00E5FF) for interactive elements
5. **Minimal Borders**: Thin strokes (#2A2A2A, #3A3A3A) for definition
6. **Rounded Corners**: 8-10dp radius for modern feel
7. **Consistent Spacing**: 16dp padding, 24dp margins between sections

---

## 🎨 Visual Hierarchy

```
Background:  bg_matte_black (darkest)
    ↓
Input Fields: #151515 (slightly lighter)
    ↓
Buttons: #141414 → #1F1F1F gradient
    ↓
Text: silver_dim → silver_text (lightest)
    ↓
Accents: accent_cyan (brightest)
```

---

## 📦 Quick Setup Checklist

1. ✅ Copy `colors.xml` to `res/values/`
2. ✅ Copy drawable files to `res/drawable/`
3. ✅ Update layouts to use theme colors and drawables
4. ✅ Set background to `@drawable/bg_matte_silver`
5. ✅ Apply `btn_silver_glow` to buttons
6. ✅ Apply `edittext_dark` to input fields
7. ✅ Use `silver_text` for primary text
8. ✅ Use `silver_dim` for secondary text
9. ✅ Use `accent_cyan` for highlights/borders

---

## 💡 Tips

- **Consistency**: Use the same button style (`btn_silver_glow`) throughout for uniformity
- **Contrast**: Ensure text contrast meets accessibility guidelines (WCAG AA)
- **Spacing**: Maintain consistent padding (12dp) and margins (16dp, 24dp)
- **Accents**: Use cyan sparingly — only for active states, highlights, or important elements
- **Dark Mode**: This theme is inherently dark; ensure it works well in all lighting conditions

---

## 🚀 Result

A sleek, privacy-focused interface that looks like a **private-mode cockpit** — dark, metallic, clean, slightly futuristic but not flashy. Perfect for privacy tools, security apps, or any app requiring a professional, minimal aesthetic.

---

**Theme Name:** Matte Black / Silver Neon  
**Style:** Privacy-focused, Minimal, Sci-fi Console  
**Best For:** Privacy tools, Security apps, Professional utilities

