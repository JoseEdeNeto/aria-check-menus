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
        it("should find a tooltip presented on mouse over in an specific element via CSS", function (done) {
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
                          widgets.should.have.lengthOf(1);
                          widgets[0].getOuterHtml().then(function (html) {
                              html.should.containDeep("Useful message 1")
                                  .and.containDeep("tooltip");
                              driver.quit();
                              done();
                          });
                      });
                  });
        });

        it("should find a tooltip presented on mouse over in an specific element via JS", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"], "/tests/fixture/sanity_check01.html"].join(""))
                  .then(function () {
                      var hover_target;
                      app = App(driver, webdriver);
                      hover_target = driver.findElement({css: "#link2"});
                      app.find_widget(hover_target).then(function (widgets) {
                          var promises;
                          widgets.should.have.lengthOf(1);
                          widgets[0].getOuterHtml().then(function (html) {
                              html.should.containDeep("Useful message 2")
                                  .and.containDeep("tooltip");
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
                          widgets.should.have.lengthOf(1);
                          widgets[0].getOuterHtml().then(function (html) {
                              html.should.containDeep("Useful").and
                                         .containDeep("tooltip open");
                              driver.quit();
                              done();
                          });
                      });
                  });
        });

        it("should find a tooltip with multiple mutations happening", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"], "/tests/fixture/multiple_mutations02.html"].join(""))
                  .then(function () {
                      var hover_target, promises = [];
                      app = App(driver, webdriver);
                      app.find_widget(driver.findElement({css: "#link2"})).then(function (widget) {
                          widget.should.have.lengthOf(1);
                          return widget[0].getOuterHtml();
                      }).then(function (html) {
                          html.should.containDeep("Useful 3").and
                                     .containDeep("<div");
                      }).then(function () {
                          return app.find_widget(driver.findElement({css: "#link3"}));
                      }).then(function (widget) {
                          widget.should.have.lengthOf(1);
                          return widget[0].getOuterHtml();
                      }).then(function (html) {
                          html.should.containDeep("Useful 4").and
                                     .containDeep("<div");
                          driver.quit();
                          done();
                      });
                  });
        });

        it("should not return mutations on log elements", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"], "/tests/fixture/multiple_mutations03.html"].join(""))
                  .then(function () {
                      var hover_target, promises = [];
                      app = App(driver, webdriver);
                      app.find_widget(driver.findElement({css: "#link2"})).then(function (widget) {
                          widget.should.have.lengthOf(1);
                          return widget[0].getOuterHtml();
                      }).then(function (html) {
                          html.should.containDeep("Useful 3").and
                                     .containDeep("<div");
                          driver.quit();
                          done();
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
                      var hover_target, promises = [];
                      app = App(driver, webdriver);
                      app.find_all_widgets().then(function (widgets) {
                          widgets.should.have.lengthOf(3);
                          for (var i = 0; i < widgets.length; i++) {
                              widgets[i].should.have.property("menu_activator");
                              widgets[i].should.have.property("menu");
                              widgets[i].menu.should.be.an.Array.and.have.lengthOf(1);
                              promises.push(widgets[i].menu[0].getOuterHtml());
                          };
                          return webdriver.promise.all(promises);
                      }).then(function (widgets_texts) {
                          widgets_texts[0].should.containDeep("Useful message 1");
                          widgets_texts[1].should.containDeep("Useful message 2");
                          widgets_texts[2].should.containDeep("Useful 3");
                          done();
                      });
                  });
        });

        it("should find all tooltips with multiple mutations happening", function (done) {
            var driver,
                app;
            driver = new webdriver.Builder()
                                  .withCapabilities(webdriver.Capabilities.firefox())
                                  .build();
            driver.get(["file://", process.env["PWD"], "/tests/fixture/multiple_mutations01.html"].join(""))
                  .then(function () {
                      var hover_target, promises = [];
                      app = App(driver, webdriver);
                      app.find_all_widgets().then(function (widgets) {
                          widgets.should.have.lengthOf(3);
                          for (var i = 0; i < widgets.length; i++) {
                              widgets[i].should.have.property("menu_activator");
                              widgets[i].should.have.property("menu");
                              widgets[i].menu.should.be.an.Array.and.have.lengthOf(1);
                              promises.push(widgets[i].menu[0].getOuterHtml());
                          };
                          return webdriver.promise.all(promises);
                      }).then(function (widgets_texts) {
                          widgets_texts[0].should.containDeep("Useful message 1");
                          widgets_texts[1].should.containDeep("Useful message 2");
                          widgets_texts[2].should.containDeep("Useful 3");
                          done();
                      });
                  });
        });
    });
});
