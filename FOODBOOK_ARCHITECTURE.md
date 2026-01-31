# Food Book Screen - Architecture Documentation

## Overview

The Food Book Screen system has been refactored to be modular, well-organized, and extensible. It allows external mods
to register custom pages while maintaining core functionality.

## Architecture

The system is organized into clear layers:

### 1. UI Elements (`client.gui.elements` package)

**Public API** - Reusable UI components for external mods

- **`UIElement`** - Base class for all UI components
    - Handles rendering, tooltips, frame management
    - Provides positioning helpers (getCenterX, setMinY, etc.)

- **`UIImage`** - Renders images from texture atlases
- **`UILabel`** - Renders text with alignment and color
- **`UIBox`** - Renders colored rectangles/lines
- **`UIStack`** - Stacks child elements horizontally or vertically
- **`UIItemStack`** - Renders item stacks with tooltips
- **`ImageData`** - Represents texture atlas data

### 2. Pages (`client.gui.pages` package)

**Mixed API** - Page system with public base class and internal implementations

- **`Page`** (public) - Base class for all pages
    - Provides helper methods: `makeSeparatorLine()`, `statWithIcon()`, `fraction()`
    - Common color constants: `FULL_BLACK`, `LESS_BLACK`, `LEAST_BLACK`

- **`PageProvider`** (public interface) - Functional interface for lazy page creation
  ```java
  Page createPage(Player player, Rectangle frame)
  ```

- **`StatsPage`** (public) - First page showing food statistics
- **`ItemListPage`** (public) - Grid display of food items with pagination

### 3. Page Registry (`client.gui.PageRegistry`)

**Public API** - Central registry for page management

**Key Features:**

- Manages both internal and external pages
- Ensures correct page ordering:
    1. First internal page (stats)
    2. External pages (in registration order)
    3. Last internal page (eaten foods list)

**Public API:**

```java
PageRegistry.registerExternalPage(String modId, PageProvider provider)
```

**Internal API:**

```java
PageRegistry.registerInternalPage(PageProvider provider)
PageRegistry.

createAllPages(Player player, Rectangle frame)
```

### 4. Food Book Screen (`client.gui.FoodBookScreen`)

**Main Controller** - Coordinates all components

**Sections:**

- **Constants & Resources** - Texture locations, image data
- **UI Components** - Background, labels, permanent elements
- **Navigation** - Page flip buttons, page entries, current page tracking
- **Data** - Player reference, FoodData helper

**Features:**

- Lazy page loading on screen open
- Mod attribution display for external pages
- Page navigation with flip buttons
- Automatic pagination for food lists

### 5. Supporting Classes

**`FoodData`** (package-private) - Encapsulates player food tracking

```java
int getFoodsEatenCount()

Set<Item> getEatenFoods()

PlayerFoodList getFoodList()
```

**`PageFlipButton`** - Navigation buttons

- `Pageable` interface (public) - For screens supporting pagination

## Usage Guide

### For External Mods - Adding Custom Pages

**1. Register your page during mod initialization:**

```java

@Mod("yourmod")
public class YourMod {
    public YourMod(IEventBus modEventBus) {
        // Register your custom page
        PageRegistry.registerExternalPage("yourmod", (player, frame) -> {
            return new YourCustomPage(player, frame);
        });
    }
}
```

**2. Create your custom page class:**

```java
import net.purple.solextended.client.gui.pages.Page;
import net.purple.solextended.client.gui.elements.*;

public class YourCustomPage extends Page {
    public YourCustomPage(Player player, Rectangle frame) {
        super(frame, "Your Page Title");

        // Add your custom content
        UILabel label = new UILabel("Custom content!");
        mainStack.addChild(label);

        mainStack.addChild(makeSeparatorLine());

        // Use helper methods
        UIElement stat = statWithIcon(
                someIconData,
                "42",
                "Some Stat"
        );
        mainStack.addChild(stat);

        updateMainStack();
    }
}
```

**3. Your page will automatically:**

- Appear between stats and eaten foods pages
- Show "by: yourmod" in the bottom-left corner
- Support page navigation
- Integrate with the book's visual style

### Page Ordering

Pages appear in this order:

1. **Stats Page** (internal) - Shows number of foods eaten
2. **External Pages** - Your custom pages, in registration order
3. **Eaten Foods List** (internal) - Grid of eaten food items (auto-paginated)

### Available UI Components

**Creating Elements:**

```java
// Labels
UILabel label = new UILabel("Text");
label.color =new

Color(128,128,128);

label.alignment =UILabel.TextAlignment.LEFT;

// Images
UIImage image = new UIImage(imageData);

// Stacks (auto-layout containers)
UIStack stack = new UIStack();
stack.axis =UIStack.Axis.VERTICAL;
stack.spacing =6;
        stack.

addChild(element1);
stack.

addChild(element2);

// Boxes/Lines
UIBox line = UIBox.horizontalLine(x1, x2, y, Color.BLACK);

// Item Stacks
UIItemStack item = new UIItemStack(new ItemStack(Items.APPLE));
```

**Positioning:**

```java
element.setCenterX(100);
element.

setMinY(50);
element.

setMaxX(200);
// And many more: getCenterY(), getWidth(), setHeight(), etc.
```

## Implementation Details

### Lazy Loading

Pages are created when the screen opens, not at mod initialization. This allows:

- Pages to query current player data
- Reduced memory usage
- Dynamic content based on game state

### Mod Attribution

External pages automatically display the mod ID in the bottom-left corner:

- Subtle gray color
- Only visible on external pages
- Helps users identify which mod added which page

### Automatic Pagination

The eaten foods list automatically splits into multiple pages if needed:

- 5 items per row
- 6 rows per page
- 30 items per page total
- Seamless navigation between pages

## Migration Notes

If you're migrating from the old FoodBookScreen:

1. All UI elements are now in the `elements` package
2. Pages extend the `Page` base class
3. Use `PageRegistry.registerExternalPage()` instead of direct page creation
4. FoodData encapsulates data access
5. PageFlipButton.Pageable is now public for external use

## Best Practices

1. **Keep pages focused** - Each page should show related information
2. **Use helper methods** - `makeSeparatorLine()`, `statWithIcon()`, etc.
3. **Follow color scheme** - Use `FULL_BLACK`, `LESS_BLACK`, `LEAST_BLACK`
4. **Call `updateMainStack()`** - After adding all elements to mainStack
5. **Register early** - In mod constructor or early initialization
6. **Test pagination** - If showing many items, test with large datasets

## Example: Complete Custom Page

```java
public class MyStatsPage extends Page {
    public MyStatsPage(Player player, Rectangle frame) {
        super(frame, "My Mod Stats");

        // Get your mod's data
        MyModData data = player.getData(MY_ATTACHMENT);

        // Add stats
        mainStack.addChild(statWithIcon(
                MY_ICON,
                String.valueOf(data.getCount()),
                "Items Collected"
        ));

        mainStack.addChild(makeSeparatorLine());

        mainStack.addChild(statWithIcon(
                ANOTHER_ICON,
                fraction(data.getCompleted(), data.getTotal()),
                "Progress"
        ));

        updateMainStack();
    }
}
```

## Future Extensions

The system is designed to support:

- Conditional pages (based on config or game state)
- Multi-page external contributions
- Custom page styling per mod
- Interactive elements (buttons, etc.)
- Dynamic page reloading
