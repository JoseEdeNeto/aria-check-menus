(function () {
    var fixtures_url = casper.cli.get("fixtures");
    casper.options.clientScripts = ["lib/Utils.js"];
    casper.options.onPageInitialized = function () {};
    casper.test.begin("SelectorFinder must return simple CSS selector", 1, function (test) {
        casper.start(fixtures_url + "selector_finder01.html", function () {
            var self = this,
                selector = self.evaluate(function () {
                    return Utils.getSelector(document.querySelector("h1"));
                });
            test.assertEquals(selector, "#aria-check-menus1");
        });
        casper.run(function () {
            test.done();
        });
    });

    casper.test.begin("SelectorFinder must not conflict the generation of IDs", 2, function (test) {
        casper.start(fixtures_url + "selector_finder01.html", function () {
            var self = this,
                selector = self.evaluate(function () {
                    var selector = Utils.getSelector(document.querySelectorAll("h1")[0]);
                    return document.querySelectorAll(selector)[0].textContent;
                });
            test.assertEquals(selector, "heading");

            selector = self.evaluate(function () {
                var selector = Utils.getSelector(document.querySelectorAll("h1")[1]);
                return document.querySelectorAll(selector)[0].textContent;
            });
            test.assertEquals(selector, "Heading 2");
        });
        casper.run(function () {
            test.done();
        });
    });

    casper.test.begin("SelectorFinder must return complex CSS selector", 1, function (test) {
        casper.start(fixtures_url + "selector_finder01.html", function () {
            var self = this,
                selector = self.evaluate(function () {
                    var selector = Utils.getSelector(document.querySelector("#procurar3"));
                    return document.querySelectorAll(selector)[0].textContent;
                });
            test.assertEquals(selector, "procurar 3");
        });
        casper.run(function () {
            test.done();
        });
    });

    casper.test.begin("SelectorFinder must return even more complex CSS selector", 2, function (test) {
        casper.start(fixtures_url + "selector_finder01.html", function () {
            var self = this,
                selector = self.evaluate(function () {
                    var selector = Utils.getSelector(document.querySelector("#procurar1"));
                    return document.querySelectorAll(selector)[0].textContent;
                });
            test.assertEquals(selector, "procurar 1");

            selector = self.evaluate(function () {
                var selector = Utils.getSelector(document.querySelector("#procurar2"));
                return document.querySelectorAll(selector)[0].textContent;
            });
            test.assertEquals(selector, "procurar 2");
        });
        casper.run(function () {
            test.done();
        });
    });

    casper.test.begin("SelectorFinder must return children nth element CSS selector", 1, function (test) {
        casper.start(fixtures_url + "selector_finder01.html", function () {
            var self = this,
                selector = self.evaluate(function () {
                    var selector = Utils.getSelector(document.querySelector("#procurar4"));

                    /* small issue that appears in PhantomJS only */
                    var selectors = selector.split(" > "),
                        target_element = document;
                    for (var i = 0; i < selectors.length; i++) {
                        target_element = target_element.querySelectorAll(selectors[i])[0];
                    };
                    return target_element.textContent;
                    /* END small issue */
                    // return document.querySelectorAll(selector)[0].textContent;
                });
            test.assertEquals(selector, "procurar 4");
        });
        casper.run(function () {
            test.done();
        });
    });

}());
