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
                      app = App(driver, webdriver);
                      app.get_invisibles().then(function (invisibles) {
                          invisibles.should.be.an.instanceOf(Array).and.have.lengthOf(5);
                          var promises = [ invisibles[0].getTagName(),
                                           invisibles[1].getTagName(),
                                           invisibles[2].getTagName(),
                                           invisibles[3].getTagName(),
                                           invisibles[4].getTagName() ];
                          webdriver.promise.all(promises).then(function (htmls) {
                              htmls[0].should.be.equal("span");
                              htmls[1].should.be.equal("span");
                              htmls[2].should.be.equal("span");
                              htmls[3].should.be.equal("span");
                              htmls[4].should.be.equal("script");
                              driver.quit();
                              done();
                          });
                      });
                  });

        });
    });
});
