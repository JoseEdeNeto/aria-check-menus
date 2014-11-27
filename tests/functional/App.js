var App = require("../../lib/App"),
    webdriver = require("selenium-webdriver");

describe("App", function () {
    describe("#get_invisibles",  function () {
        it("should find invisible elements", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"], "/tests/fixture/sanity_check01.html"].join(""))
                  .then(function () {
                      app = App(driver);
                      app.get_invisibles().should.be.an.instanceOf(Array).and.have.lengthOf(2);
                      driver.quit();
                  });

        });
    });
});
