# FoodBookScreen Architecture - Visual Overview

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                      FOOD BOOK SCREEN                           │
│                   (Main Controller)                             │
│                                                                 │
│  ┌────────────────┐  ┌──────────────┐  ┌────────────────┐     │
│  │   UI State     │  │  Navigation  │  │   Data Layer   │     │
│  │                │  │              │  │                │     │
│  │ - background   │  │ - pageEntries│  │ - player       │     │
│  │ - pageNumLabel │  │ - currentPage│  │ - foodData     │     │
│  │ - modAttrLabel │  │ - buttons    │  │                │     │
│  └────────────────┘  └──────────────┘  └────────────────┘     │
│                                                                 │
│  Renders ↓           Navigates ↓        Queries ↓              │
└─────────────────────────────────────────────────────────────────┘
         │                  │                  │
         ↓                  ↓                  ↓
┌─────────────────┐  ┌─────────────┐  ┌──────────────────┐
│  UI Elements    │  │   Pages     │  │  Page Registry   │
│  (Reusable)     │  │  (Content)  │  │  (Management)    │
└─────────────────┘  └─────────────┘  └──────────────────┘
```

## Component Hierarchy

```
FoodBookScreen
├── UI Elements (elements/)
│   ├── UIElement (base class)
│   │   ├── UIImage
│   │   ├── UILabel
│   │   ├── UIBox
│   │   ├── UIStack
│   │   └── UIItemStack
│   └── ImageData (data class)
│
├── Pages (pages/)
│   ├── PageProvider (interface)
│   ├── Page (abstract base)
│   │   ├── StatsPage
│   │   └── ItemListPage
│   └── [External Pages] (from other mods)
│
├── Page Registry
│   ├── Internal Pages List
│   ├── External Pages Map
│   └── Page Ordering Logic
│
├── Data Layer
│   └── FoodData (helper)
│       └── PlayerFoodList (attachment)
│
└── Navigation
    ├── PageFlipButton
    └── Pageable (interface)
```

## Data Flow Diagram

```
┌──────────────┐
│  Player      │
│  opens book  │
└──────┬───────┘
       │
       ↓
┌──────────────────────────────────────┐
│  FoodBookScreen.init()               │
│  1. Load PlayerFoodList attachment   │
│  2. Create FoodData helper           │
│  3. Setup UI components              │
│  4. Initialize pages (lazy)          │
└──────┬───────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────┐
│  PageRegistry.createAllPages()       │
│  ┌────────────────────────────────┐  │
│  │ For each registered provider:  │  │
│  │   provider.createPage(...)     │  │
│  └────────────────────────────────┘  │
└──────┬───────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────┐
│  Page Ordering                       │
│  1. StatsPage (internal)             │
│  2. [External Pages] (ordered)       │
│  3. ItemListPage(s) (internal)       │
└──────┬───────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────┐
│  Render Loop                         │
│  - Render background                 │
│  - Render current page               │
│  - Render page number                │
│  - Render mod attribution (if ext)   │
│  - Render navigation buttons         │
└──────────────────────────────────────┘
```

## Page Registration Flow

```
External Mod                     PageRegistry                FoodBookScreen
     │                                │                            │
     │ registerExternalPage()         │                            │
     ├───────────────────────────────>│                            │
     │    ("modid", provider)         │                            │
     │                                │ Store in map               │
     │                                │                            │
     │                                │                            │
     │                                │    init() called           │
     │                                │<───────────────────────────┤
     │                                │                            │
     │                                │ createAllPages()           │
     │                                ├────────┐                   │
     │                                │        │                   │
     │  createPage(player, frame)     │<───────┘                   │
     │<───────────────────────────────┤                            │
     │                                │                            │
     │  return CustomPage             │                            │
     ├───────────────────────────────>│                            │
     │                                │                            │
     │                                │ return PageEntry list      │
     │                                ├───────────────────────────>│
     │                                │                            │
     │                                │                            │
```

## Page Layout Structure

```
┌────────────────────────────────────┐
│         Book Background            │
│  ┌──────────────────────────────┐  │
│  │      Page Title (header)     │  │
│  ├──────────────────────────────┤  │
│  │   ─────────────────────      │  │ ← Separator
│  ├──────────────────────────────┤  │
│  │                              │  │
│  │   Page Content (mainStack)   │  │
│  │                              │  │
│  │   - Stats                    │  │
│  │   - Separators               │  │
│  │   - Custom elements          │  │
│  │                              │  │
│  └──────────────────────────────┘  │
│                                    │
│  ┌────┐  Page 1/5  ┌────┐          │
│  │ ◄  │            │  ► │          │ ← Navigation
│  └────┘            └────┘          │
│                                    │
│  by: externalmod                   │ ← Attribution (ext only)
└────────────────────────────────────┘
```

## External Mod Integration Points

```
┌─────────────────────────────────────────────────────────┐
│  External Mod (e.g., "coolmod")                         │
│                                                         │
│  ┌────────────────────────────────────────────────┐    │
│  │  Mod Initialization                             │    │
│  │                                                 │    │
│  │  PageRegistry.registerExternalPage(            │    │
│  │      "coolmod",                    ← Mod ID    │    │
│  │      (player, frame) -> {          ← Provider  │    │
│  │          return new CoolPage(...);             │    │
│  │      }                                         │    │
│  │  );                                            │    │
│  └────────────────────────────────────────────────┘    │
│                                                         │
│  ┌────────────────────────────────────────────────┐    │
│  │  CoolPage extends Page                         │    │
│  │                                                 │    │
│  │  - Uses public UI elements                     │    │
│  │  - Uses protected helper methods               │    │
│  │  - Adds custom content                         │    │
│  │  - Calls updateMainStack()                     │    │
│  └────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
                          │
                          ↓
         ┌────────────────────────────────┐
         │  Food Book displays:           │
         │                                │
         │  Page 1: Stats (internal)      │
         │  Page 2: CoolPage (coolmod) ✓  │
         │  Page 3: Eaten Foods (internal)│
         └────────────────────────────────┘
```

## Class Relationship Diagram

```
┌─────────────────┐
│   UIElement     │ (abstract)
│   - frame       │
│   - tooltip     │
│   - children    │
└────────┬────────┘
         │ extends
         ├──────────────────┬──────────────┬────────────┬────────────┐
         │                  │              │            │            │
    ┌────▼────┐      ┌──────▼────┐  ┌─────▼─────┐ ┌───▼────┐  ┌───▼─────┐
    │UIImage  │      │UILabel    │  │UIBox      │ │UIStack │  │UIItem   │
    │         │      │           │  │           │ │        │  │Stack    │
    └─────────┘      └───────────┘  └───────────┘ └────────┘  └─────────┘
                              │
                              │ extends
                              ↓
                       ┌──────────────┐
                       │    Page      │ (abstract)
                       │ - mainStack  │
                       │ + helpers    │
                       └──────┬───────┘
                              │ extends
                 ┌────────────┼────────────┐
                 │            │            │
          ┌──────▼───┐  ┌─────▼──────┐  ┌─▼────────────┐
          │StatsPage │  │ItemList    │  │External      │
          │          │  │Page        │  │Pages         │
          └──────────┘  └────────────┘  └──────────────┘
```

## Access Control Map

```
┌──────────────────────────────────────────────────────────┐
│  PUBLIC (External Mods Can Use)                          │
├──────────────────────────────────────────────────────────┤
│  • PageRegistry.registerExternalPage()                   │
│  • PageProvider interface                                │
│  • Page (base class)                                     │
│  • All UI Elements (UIElement, UIImage, UILabel, etc.)   │
│  • ImageData                                             │
│  • PageFlipButton.Pageable interface                     │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│  PACKAGE-PRIVATE (Internal Only)                         │
├──────────────────────────────────────────────────────────┤
│  • PageRegistry.registerInternalPage()                   │
│  • PageRegistry.createAllPages()                         │
│  • FoodData                                              │
│  • PageRegistry.PageEntry                                │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│  PROTECTED (Page Subclasses Can Use)                     │
├──────────────────────────────────────────────────────────┤
│  • Page.mainStack                                        │
│  • Page.makeSeparatorLine()                              │
│  • Page.statWithIcon()                                   │
│  • Page.fraction()                                       │
│  • Page.FULL_BLACK, LESS_BLACK, LEAST_BLACK              │
└──────────────────────────────────────────────────────────┘
```

## Typical Usage Flow

### For permanentfood (Internal):

```
1. Static initializer registers internal pages
2. User opens food book
3. Screen creates pages via registry
4. Pages query PlayerFoodList via FoodData
5. Pages render with UI elements
6. User navigates with flip buttons
```

### For External Mods:

```
1. Mod registers page provider on init
2. User opens food book
3. Provider called with player & frame
4. Custom page created
5. Page appears with "by: modid" label
6. User sees custom content between stats and foods
```

## Thread Safety

```
Main Thread Only
    │
    ├─ PageRegistry (static, single-threaded access)
    ├─ FoodBookScreen (client-side GUI)
    ├─ Page creation (lazy, on screen init)
    └─ Rendering (every frame)

Note: No multi-threading needed or supported
```

## Memory Model

```
Persistent (Until Mod Unload)
    │
    └─ PageRegistry
        ├─ internalPages: List<PageProvider>
        └─ externalPages: Map<String, PageProvider>

Created on Screen Open
    │
    └─ FoodBookScreen
        ├─ UIElements (background, labels, etc.)
        ├─ PageEntries (List<PageEntry>)
        │   └─ Page instances (created lazily)
        └─ FoodData (wraps PlayerFoodList)

Released on Screen Close
    │
    └─ Entire FoodBookScreen instance
        (Pages, UI elements, etc. garbage collected)
```

## Performance Characteristics

```
Time Complexity:
    • Page Registration:    O(1)
    • Page Creation:        O(n) where n = number of registered pages
    • Page Navigation:      O(1)
    • Rendering:            O(m) where m = elements on current page

Space Complexity:
    • Registry:             O(p) where p = number of providers
    • Screen:               O(p + e) where e = UI elements per page
    • Per Page:             O(c) where c = content elements

Lazy Loading Benefits:
    • Pages only created when screen opens
    • Reduced memory usage when book is closed
    • Fresh data each time (queries current state)
```

---

This architecture provides:
✓ Clean separation of concerns
✓ Easy extensibility for external mods
✓ Maintainable codebase
✓ Good performance characteristics
✓ Clear public API boundaries
