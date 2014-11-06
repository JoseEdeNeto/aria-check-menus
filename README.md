ARIA-CHECK-MENUS
================
This is a prototype of a tool for automatically identifying Flyout menus (http://www.welie.com/patterns/showPattern.php?patternID=fly-out-menu) and tooltips (http://test.cita.uiuc.edu/aria/tooltip/tooltip1.php).

Initially, the tool will solely identify the widgets using JavaScript DOM analysis and CSS rules analysis. Then, the tool's goal is to automatically analise the construction of these widgets (Flyout menus and tooltips) to present feedback about how to enhance the accessibility of these widgets in regards to the ARIA specification (http://www.w3.org/TR/wai-aria/).

TODO
====
- Migrating to SeleniumJS API, since not all websites implement support for CasperJS functionalities.
