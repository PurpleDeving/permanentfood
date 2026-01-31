# Quick Reference - Adding Pages to Food Book

## 5-Minute Integration Guide

### Step 1: Add Dependency

In your `build.gradle`:

```gradle
dependencies {
    implementation "net.purple:solextended:1.0.0" // or appropriate version
}
```

### Step 2: Register Your Page

In your mod's constructor:

```java
import net.purple.solextended.client.gui.PageRegistry;

@Mod("yourmod")
public class YourMod {
    public YourMod(IEventBus modEventBus) {
        PageRegistry.registerExternalPage("yourmod", (player, frame) -> {
            return new YourStatsPage(player, frame);
        });
    }
}
```

### Step 3: Create Your Page

```java
import net.purple.solextended.client.gui.pages.Page;
import net.purple.solextended.client.gui.elements.*;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class YourStatsPage extends Page {
    public YourStatsPage(Player player, Rectangle frame) {
        super(frame, "Your Page Title");

        // Add content here
        UILabel label = new UILabel("Your content");
        mainStack.addChild(label);

        // Always call this at the end!
        updateMainStack();
    }
}
```

### Done!

Your page will appear in the Food Book with "by: yourmod" in the corner.

---

## Common Patterns

### Display a Stat with Icon

```java
UIElement stat = statWithIcon(
        iconData,           // ImageData for the icon
        "42",              // Value to display
        "Stat Name"        // Label below the stat
);
mainStack.

addChild(stat);
```

### Add a Separator Line

```java
mainStack.addChild(makeSeparatorLine());
```

### Show a Fraction

```java
String display = fraction(5, 10);  // Returns "5/10"
```

### Custom Layout

```java
UIStack horizontal = new UIStack();
horizontal.axis =UIStack.Axis.HORIZONTAL;
horizontal.spacing =10;
        horizontal.

addChild(element1);
horizontal.

addChild(element2);
mainStack.

addChild(horizontal);
```

### Display Items

```java
UIItemStack item = new UIItemStack(new ItemStack(Items.APPLE));
item.

setCenterX(getCenterX());
        item.

setCenterY(getCenterY());
        children.

add(item);  // Note: direct to children, not mainStack
```

---

## Available UI Components

### UILabel

```java
UILabel label = new UILabel("Text");
label.color =new

Color(128,128,128);

label.alignment =UILabel.TextAlignment.CENTER;
```

### UIImage

```java
ImageData icon = new ImageData(
        resourceLocation,
        new Rectangle(x, y, width, height)  // position in texture
);
UIImage image = new UIImage(icon);
```

### UIBox (Lines/Rectangles)

```java
UIBox hLine = UIBox.horizontalLine(x1, x2, y, Color.BLACK);
UIBox vLine = UIBox.verticalLine(x, y1, y2, Color.BLACK);
```

### UIStack (Auto-layout)

```java
UIStack stack = new UIStack();
stack.axis =UIStack.Axis.VERTICAL;  // or HORIZONTAL
stack.spacing =6;
        stack.

addChild(element);
```

---

## Positioning Helpers

```java
element.setCenterX(100);      // Center horizontally at x=100
element.

setCenterY(50);       // Center vertically at y=50
element.

setMinX(10);          // Left edge at x=10
element.

setMinY(20);          // Top edge at y=20
element.

setMaxX(200);         // Right edge at x=200
element.

setMaxY(150);         // Bottom edge at y=150

// Getters also available
int x = element.getCenterX();
int width = element.getWidth();
```

---

## Page Color Constants

```java
FULL_BLACK    // Color.BLACK - Main text
LESS_BLACK    // new Color(0,0,0,128) - Secondary text
LEAST_BLACK   // new Color(0,0,0,64) - Subtle elements/lines
```

---

## Common Mistakes

### ❌ Forgetting updateMainStack()

```java
public YourPage(Player player, Rectangle frame) {
    super(frame, "Title");
    mainStack.addChild(element);
    // Missing: updateMainStack();
}
```

### ✅ Always call updateMainStack()

```java
public YourPage(Player player, Rectangle frame) {
    super(frame, "Title");
    mainStack.addChild(element);
    updateMainStack();  // ✓ Correct!
}
```

### ❌ Adding items to wrong container

```java
// Wrong - items won't be positioned correctly
mainStack.addChild(new UIItemStack(stack));
```

### ✅ Add items to children directly

```java
// Correct - items positioned manually
UIItemStack item = new UIItemStack(stack);
item.

setCenterX(getCenterX());
        item.

setCenterY(getCenterY());
        children.

add(item);
```

---

## Full Example with Everything

```java
public class CompleteExamplePage extends Page {
    public CompleteExamplePage(Player player, Rectangle frame) {
        super(frame, "Example Page");

        // Get your data
        int myValue = 42;

        // Title/intro text
        UILabel intro = new UILabel("Welcome!");
        intro.color = FULL_BLACK;
        mainStack.addChild(intro);

        mainStack.addChild(makeSeparatorLine());

        // Stat with icon
        ImageData customIcon = new ImageData(
                ResourceLocation.fromNamespaceAndPath("yourmod", "textures/gui/icons.png"),
                new Rectangle(0, 0, 16, 16),
                14, 14
        );

        mainStack.addChild(statWithIcon(
                customIcon,
                String.valueOf(myValue),
                "Your Stat Name"
        ));

        mainStack.addChild(makeSeparatorLine());

        // Progress fraction
        mainStack.addChild(statWithIcon(
                customIcon,
                fraction(10, 20),
                "Progress"
        ));

        // Custom horizontal layout
        UIStack buttons = new UIStack();
        buttons.axis = UIStack.Axis.HORIZONTAL;
        buttons.spacing = 5;

        UILabel option1 = new UILabel("Option A");
        option1.color = LESS_BLACK;
        buttons.addChild(option1);

        UILabel option2 = new UILabel("Option B");
        option2.color = LESS_BLACK;
        buttons.addChild(option2);

        mainStack.addChild(buttons);

        // Item display (positioned manually)
        UIItemStack item = new UIItemStack(new ItemStack(Items.DIAMOND));
        item.setCenterX(getCenterX());
        item.setMinY(getMinY() + 120);
        children.add(item);

        // Always at the end!
        updateMainStack();
    }
}
```

---

## Need Help?

- See `FOODBOOK_ARCHITECTURE.md` for detailed documentation
- Check `ExternalPageExample.java` for more examples
- All UI components are in `net.purple.solextended.client.gui.elements`
- Page base class: `net.purple.solextended.client.gui.pages.Page`
