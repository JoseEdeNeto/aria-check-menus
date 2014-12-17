var App = require("./lib/App"),
    LoggerApp = require("./lib/LoggerApp"),
    webdriver = require("selenium-webdriver"),
    fs = require("fs"),
    app, driver, url;

if (process.argv.length < 3)
    process.exit(1);

url = process.argv[2];

driver = new webdriver.Builder()
                      .withCapabilities(webdriver.Capabilities.firefox())
                      .build();

driver.get(url).then(function () {
    app = LoggerApp(App(driver, webdriver), driver, fs, "captured_widgets", console);
    console.log("Looking for widgets...");
    app.find_all_widgets().then(function (widgets) {
        console.log("- Found: " + widgets.length);
        process.exit(0);
    });
});
