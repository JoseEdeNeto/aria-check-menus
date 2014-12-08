var App = require("../../lib/App"),
    webdriver = require("selenium-webdriver");

describe("App", function () {
    describe("#_get_invisibles",  function () {
        it("should find invisible elements", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"], "/tests/fixture/sanity_check01.html"].join(""))
                  .then(function () {
                      app = App(driver, webdriver);
                      app._get_invisibles().then(function (invisibles) {
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

    describe("#find_visible", function () {

        it("should find visible elements as well", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"], "/tests/fixture/sanity_check01.html"].join(""))
                  .then(function () {
                      app = App(driver, webdriver);
                      app.get_visibles().then(function (visibles) {
                          visibles.should.be.an.instanceOf(Array).and.have.lengthOf(3);
                          var promises = [ visibles[0].getTagName(),
                                           visibles[1].getTagName(),
                                           visibles[2].getTagName() ];
                          webdriver.promise.all(promises).then(function (htmls) {
                              htmls[0].should.be.equal("h1");
                              htmls[1].should.be.equal("a");
                              htmls[2].should.be.equal("a");
                              driver.quit();
                              done();
                          });
                      });
                  });

        });
    });

    describe("#find_widget", function () {
        it("should find a tooltip presented on mouse over in an specific element", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"], "/tests/fixture/sanity_check01.html"].join(""))
                  .then(function () {
                      var hover_target;
                      app = App(driver, webdriver);
                      hover_target = driver.findElement({css: "#link1"});
                      app.find_widget(hover_target).then(function (widgets) {
                          var promises;
                          widgets.length.should.be.equal(2);
                          widgets.should.have.lengthOf(2);
                          promises = [widgets[0].getTagName(), widgets[1].getTagName()];
                          webdriver.promise.all(promises).then(function (htmls) {
                              htmls[0].should.be.equal("span");
                              htmls[1].should.be.equal("span");
                              driver.quit();
                              done();
                          });
                      });
                  });
        });

        it("should find a tooltip on mouse over even if the element does not priorly exists", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"],
                        "/tests/fixture/menu_element_just_shows_on_hover01.html"].join(""))
                  .then(function () {
                      var hover_target;
                      app = App(driver, webdriver);
                      hover_target = driver.findElement({css: "#link2"});
                      app.find_widget(hover_target).then(function (widgets) {
                          var promises;
                          widgets.length.should.be.equal(2);
                          widgets.should.have.lengthOf(2);
                          promises = [widgets[0].getTagName(), widgets[1].getTagName()];
                          webdriver.promise.all(promises).then(function (htmls) {
                              htmls[0].should.be.equal("span");
                              htmls[1].should.be.equal("span");
                              driver.quit();
                              done();
                          });
                      });
                  });
        });
    });

    describe("#find_all_widgets", function () {
        it("should find all tooltips in the webpage", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"], "/tests/fixture/sanity_check02.html"].join(""))
                  .then(function () {
                      var hover_target;
                      app = App(driver, webdriver);
                      app.find_all_widgets().then(function (widgets) {
                          var tags;
                          widgets.should.have.property("length", 6);
                          tags = [widgets[0].getTagName(),
                                  widgets[1].getTagName(),
                                  widgets[2].getTagName(),
                                  widgets[3].getTagName(),
                                  widgets[4].getTagName(),
                                  widgets[5].getTagName()];
                          webdriver.promise.all(tags).then(function (tags) {
                              tags[0].should.be.equal("span");
                              tags[1].should.be.equal("span");
                              tags[2].should.be.equal("span");
                              tags[3].should.be.equal("span");
                              tags[4].should.be.equal("span");
                              tags[5].should.be.equal("span");
                          });
                      });
                  });
        });
    });
});
