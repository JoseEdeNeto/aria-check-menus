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
            test.assertEquals(selector, "body:nth-child(2) > h1:nth-child(1)");
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
                    return document.querySelectorAll(selector)[0].textContent;
                });
            test.assertEquals(selector, "procurar 4");
        });
        casper.run(function () {
            test.done();
        });
    });
}());
